package gakusei.mini.client;

import gakusei.mini.Gakumini;
import gakusei.mini.mixin.coin_models.SpriteAtlasTextureMixin;
import gakusei.mini.screen.BrandingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GakuminiClient implements ClientModInitializer {

    public static ModularCoinRenderer modularCoinRenderer = new ModularCoinRenderer();
    public static Identifier brandAtlas = Identifier.of("gakumini", "textures/atlas/coin_brands.png");

    public static final RenderLayer COIN_LAYER = RenderLayer.getItemEntityTranslucentCull(brandAtlas);

    static final List<String> gakumessages = List.of(
            "gakugooey gakugooey gakugooey",
            "she gaku on my mini until i eventually fix coin code",
            "this method was created for an entirely different purpose LMAOOO",
            "can't see california without marlon brando's eyes",
            "fistful of dillar",
            "the minedustrial reboblution and its consequences",
            "why are you even looking at this, it's not important");

    @Override
    public void onInitializeClient() {

        HandledScreens.register(
                Gakumini.BRANDING_SCREEN_HANDLER_TYPE, BrandingScreen::new
        );

        for (Item item : Gakumini.coins) {
            BuiltinItemRendererRegistry.INSTANCE.register(item, modularCoinRenderer);
            Gakumini.LOGGER.info("Registered " + item.getTranslationKey() + " with a ModularCoinRenderer");
        }
    }

    public static void stitchCoinBrands()
    {
        int randomNum = ThreadLocalRandom.current().nextInt(0, gakumessages.size()-1);
        Gakumini.LOGGER.info(gakumessages.get(randomNum));
    }
}
