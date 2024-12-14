package gakusei.mini.mixin.coin_models;

import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ItemModelGenerator.class)
public interface ItemModelGeneratorMixin {
    @Invoker("addLayerElements")
    List<ModelElement> addLayerElementsHijack(int layer, String key, SpriteContents sprite);
}
