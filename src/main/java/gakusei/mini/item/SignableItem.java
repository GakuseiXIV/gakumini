package gakusei.mini.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SignableItem extends Item {
    public static final String SIGNER = "signer";
    public static String SIGNED_BY_TRANSLATION_KEY = "gakumini.signedBy";

    public SignableItem(Item.Settings settings) {
        super(settings);
    }public SignableItem(Item.Settings settings, String signedByTranslationKey) {
        super(settings);
        SIGNED_BY_TRANSLATION_KEY = signedByTranslationKey;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()) {
            NbtCompound nbtCompound = stack.getNbt();
            String string = nbtCompound.getString("signer");
            if (!StringHelper.isEmpty(string)) {
                tooltip.add(Text.literal(I18n.translate(SIGNED_BY_TRANSLATION_KEY) + string).formatted(Formatting.GRAY));
            }
        }
    }
}
