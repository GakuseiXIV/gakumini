package gakusei.mini.mixin.coin_models;

import gakusei.mini.Gakumini;
import gakusei.mini.client.GakuminiClient;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SpriteAtlasManager.class)
public class SpriteAtlasManagerMixin {
    @Inject(method="<init>",at=@At("HEAD"))
    private static void startCoinStitching(Map<Identifier, Identifier> loaders, TextureManager textureManager, CallbackInfo ci)
    {
        GakuminiClient.stitchCoinBrands();
    }
}
