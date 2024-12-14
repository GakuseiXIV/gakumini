package gakusei.mini.client;

import gakusei.mini.Gakumini;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.util.ModelIdentifier;

public class ModelLoading implements ModelLoadingPlugin {
    public static final ModelIdentifier COIN_MODEL = new ModelIdentifier(
            Gakumini.identifier("coin_model"), ""
    );

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        /*BuiltinItemRendererRegistry.INSTANCE.register(
            Gakumini.COIN, new CoinRenderer()
        );*/
    }
}
