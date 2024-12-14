package gakusei.mini.client;

import gakusei.mini.Gakumini;
import gakusei.mini.screen.BrandingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class GakuminiClient implements ClientModInitializer {

    public static ModularCoinRenderer modularCoinRenderer = new ModularCoinRenderer();


    @Override
    public void onInitializeClient() {

        HandledScreens.register(
                Gakumini.BRANDING_SCREEN_HANDLER_TYPE, BrandingScreen::new
        );
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.COPPER_COIN, modularCoinRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.DIAMOND_COIN, modularCoinRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.EMERALD_COIN, modularCoinRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.GOLD_COIN, modularCoinRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.IRON_COIN, modularCoinRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(Gakumini.NETHERITE_COIN, modularCoinRenderer);


    }
}
