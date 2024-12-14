package gakusei.mini.mixin.coin_models;

import gakusei.mini.Gakumini;
import gakusei.mini.GakuminiTags;
import gakusei.mini.client.GakuminiClient;
import gakusei.mini.client.ModularCoinRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements SynchronousResourceReloader {
    @ModifyVariable(method = "getModel", at=@At("STORE"), ordinal = 0)
    private BakedModel getCoinBakedModel(BakedModel original, ItemStack stack)
    {
        if (stack.isIn(GakuminiTags.GAKUMINI_COINS)) {
            return GakuminiClient.modularCoinRenderer.getOrCreateBakedModel(stack.getOrCreateNbt().getString("coin_brands"), stack.getItem());
        }
        return original;
    }
}
