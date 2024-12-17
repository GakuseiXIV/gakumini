package gakusei.mini.mixin.coin_models;

import gakusei.mini.client.GakuminiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static gakusei.mini.client.GakuminiClient.brandAtlas;

@Mixin(BakedModelManager.class)
public abstract class BakedModelManagerMixin {
    @Mutable
    @Shadow @Final private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

    @Inject(method = "<init>", at=@At("HEAD"))
    private static void addCoinAtlas(TextureManager textureManager, BlockColors colorMap, int mipmap, CallbackInfo ci)
    {
        HashMap<Identifier, Identifier> map = new HashMap<>(LAYERS_TO_LOADERS);
        map.put(brandAtlas, Identifier.of("gakumini", "coin_brands"));
        LAYERS_TO_LOADERS = map;
    }
}
