package gakusei.mini.screen;

import gakusei.mini.Gakumini;
import gakusei.mini.GakuminiSounds;
import gakusei.mini.GakuminiTags;
import gakusei.mini.item.CoinItem;
import gakusei.mini.recipe.BrandingRecipe;
import gakusei.mini.recipe.BrandingRecipeType;
import gakusei.mini.util.NBTUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BrandingScreenHandler extends ScreenHandler {
    private final SimpleInventory input = new SimpleInventory(3);
    private final SimpleInventory output = new SimpleInventory(1);
    PlayerEntity player;

    ScreenHandlerContext context;

    public BrandingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public BrandingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(Gakumini.BRANDING_SCREEN_HANDLER_TYPE, syncId);
        context = ctx;
        this.player = playerInventory.player;

        //coin
        Slot coinSlot = addSlot(new BrandingInputSlot(input, 0, 65, 17));
        //brand
        Slot brandSlot = addSlot(new BrandingInputSlot(input, 1, 30, 35));
        //material
        Slot materialSlot = addSlot(new BrandingInputSlot(input, 2, 65, 53));
        //output
        Slot outputSlot = addSlot(new BrandingOutputSlot(playerInventory.player,output, 0, 124, 35));

        int i;
        int j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

    }
    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, Inventory in, Inventory out) {
        if (!world.isClient) {

            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<BrandingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(Gakumini.BRANDING_RECIPE_TYPE, in, world);
            if (optional.isPresent()) {
                BrandingRecipe recipe = optional.get();

                ItemStack itemStack2 = recipe.craft(in, world.getRegistryManager());
                if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                    itemStack = itemStack2;
                }
            }
            else if ( in.getStack(0).isIn(GakuminiTags.GAKUMINI_COINS)
                    && in.getStack(1).isIn(GakuminiTags.BRANDING_BRANDS)
                    && in.getStack(2).isIn(GakuminiTags.BRANDING_MATERIALS))
            {
                itemStack = in.getStack(0).copyWithCount(1);
                ItemStack brandStack = in.getStack(1).copy();
                ItemStack materialStack = in.getStack(2).copy();
                String putString = "";
                //TODO: fix this terrible code
                if (materialStack.isOf(Items.COPPER_INGOT)) putString = "copper";
                if (materialStack.isOf(Items.DIAMOND)) putString = "diamond";
                if (materialStack.isOf(Items.EMERALD)) putString = "emerald";
                if (materialStack.isOf(Items.GOLD_INGOT)) putString = "gold";
                if (materialStack.isOf(Items.IRON_INGOT)) putString = "iron";
                if (materialStack.isOf(Items.NETHERITE_INGOT)) putString = "netherite";

                if (brandStack.isIn(GakuminiTags.BRANDING_BRANDS)) {
                    putString = putString + "_" + Gakumini.coinBrandMap.get(brandStack.getItem());
                }

                itemStack.getOrCreateNbt().putString("coin_brands", NBTUtil.addToStringlist(
                        itemStack.getNbt().getString("coin_brands"), putString, true
                ));
            }

            if(itemStack.isIn(GakuminiTags.GAKUMINI_COINS))
            {
                itemStack.getOrCreateNbt().putString("signer", player.getName().getString());
            }

            out.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(3, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 3, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run(((world, blockPos) -> updateResult(this, world, player, input, output)));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        Slot theSlotInQuestion = slots.get(slot);
        ItemStack itemStack;
        if (theSlotInQuestion.hasStack())
        {
            itemStack = theSlotInQuestion.getStack().copy();
            if (slot <= 2)
            {
                tryPutInInventory(itemStack,theSlotInQuestion);
            }
            else if (slot != 3)
            {
                if (itemStack.isIn(GakuminiTags.BRANDING_MATERIALS))
                {
                    tryPutInSlot(itemStack, slots.get(2), theSlotInQuestion);
                }
                else if (itemStack.isIn(GakuminiTags.GAKUMINI_COINS))
                {
                    tryPutInSlot(itemStack, slots.get(0), theSlotInQuestion);
                }
                else if (itemStack.isIn(GakuminiTags.BRANDING_BRANDS))
                {
                    tryPutInSlot(itemStack, slots.get(1), theSlotInQuestion);
                }
            }
            else
            {
                itemStack.setCount(getSmallestInputStack());
                tryPutInInventory(itemStack,theSlotInQuestion);
                decrementInput(getSmallestInputStack()-1);
            }
            updateResult(this, player.getWorld(), player, input, output);
            theSlotInQuestion.onTakeItem(player, itemStack);
        }

        return ItemStack.EMPTY;
    }

    public void tryPutInSlot(ItemStack itemStack, Slot slotToPutIn, Slot original)
    {
        if ((slotToPutIn.getStack().itemMatches(itemStack.getRegistryEntry()) && slotToPutIn.getStack().getNbt()==itemStack.getNbt())
                || slotToPutIn.getStack().isEmpty())
        {
            int total = slotToPutIn.getStack().getCount() + itemStack.getCount();
            ItemStack stack2 = itemStack.copy();
            stack2.setCount(Math.min(64, total));
            slotToPutIn.setStack(stack2);
            int leftover = total - 64;
            itemStack.setCount(Math.max(0,leftover));
            original.getStack().setCount(Math.max(0,leftover));
        }
    }

    public void tryPutInInventory(ItemStack itemStack, Slot theSlotInQuestion)
    {
        int j = 39;
        boolean lookingForMatchingItemSlots = true;
        while (j>3)
        {
            Slot s = slots.get(j);
            if (!lookingForMatchingItemSlots && s.getStack().isEmpty())
            {
                s.setStack(itemStack);
                theSlotInQuestion.setStack(ItemStack.EMPTY);
                j=-1;
            }
            else if (lookingForMatchingItemSlots &&
                    s.getStack().itemMatches(itemStack.getRegistryEntry()) && s.getStack().getNbt() == itemStack.getNbt())
            {
                int total = s.getStack().getCount() + itemStack.getCount();
                s.getStack().setCount(Math.min(64, total));
                int leftover = total - 64;
                ItemStack stack2 = itemStack.copy();
                stack2.setCount(Math.min(64, total));
                s.setStack(stack2);
                itemStack.setCount(Math.max(0,leftover));
            }
            if (itemStack.isEmpty()) j = -1;
            j--;
            if (lookingForMatchingItemSlots && j == 3)
            {
                j=39;
                lookingForMatchingItemSlots=false;
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public int getSmallestInputStack()
    {
        //never do this code this sucks ass
        int i = input.getStack(0).getCount();
        if (i>input.getStack(2).getCount()) i=input.getStack(2).getCount();
        return i;
    }

    public void decrementInput(int i)
    {
        input.removeStack(0, i);
        input.removeStack(2, i);

        this.context.run(((world, blockPos) -> updateResult(this, world, player, input, output)));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.input);
        });
    }

    public class BrandingInputSlot extends Slot
    {
        public BrandingInputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            BrandingScreenHandler.this.onContentChanged(inventory);
        }
    }
    public class BrandingOutputSlot extends Slot
    {
        private final PlayerEntity player;
        public BrandingOutputSlot(PlayerEntity player, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.player = player;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            super.onTakeItem(player, stack);
            BrandingScreenHandler.this.decrementInput(1);
            MinecraftClient.getInstance().getSoundManager().play(
                    PositionedSoundInstance.master(GakuminiSounds.CRAFT_BRANDING, 1,4)
            );
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
