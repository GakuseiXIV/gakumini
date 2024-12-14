package gakusei.mini.block;

import gakusei.mini.GakuminiStats;
import gakusei.mini.screen.BrandingScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrandingBlock extends Block {

    private static final Text TITLE = Text.translatable("gakumini.container.branding");

    public BrandingBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)
        {
            return ActionResult.SUCCESS;
        }
        else
        {
            state.updateNeighbors(world, pos, 0);
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            player.incrementStat(GakuminiStats.INTERACT_WITH_BRANDING_BLOCK);
            return ActionResult.CONSUME;
        }
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
            return new BrandingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
        }, TITLE);
    }
}
