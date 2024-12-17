package gakusei.mini.mixin.coin_models;

import gakusei.mini.GakuminiTags;
import gakusei.mini.client.GakuminiClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public abstract class RenderLayersMixin {
    @Inject(method = "getItemLayer", at=@At("HEAD"), cancellable = true)
    private static void getCoinLayer(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> cir)
    {
        if (stack.isIn(GakuminiTags.GAKUMINI_COINS)) {
            cir.setReturnValue(GakuminiClient.COIN_LAYER);
        }
    }
}
