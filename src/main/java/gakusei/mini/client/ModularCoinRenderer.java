package gakusei.mini.client;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import gakusei.mini.Gakumini;
import gakusei.mini.mixin.coin_models.ItemModelGeneratorMixin;
import gakusei.mini.util.NBTUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModularCoinRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    public static HashMap<String, BakedModel> models = new HashMap<>();
    static CoinBakeSettings modelBakeSettings = new CoinBakeSettings();

    public static ModelTransformation TRANSFORM = new ModelTransformation(
            //left hand third person
            new Transformation(new Vector3f(),new Vector3f(0,0.2f,0.075f),new Vector3f(0.55f)),
            //right hand third person
            new Transformation(new Vector3f(),new Vector3f(0,0.2f,0.075f),new Vector3f(0.55f)),
            //left hand first person
            new Transformation(new Vector3f(0,-90,25),new Vector3f(0,0.25f,0.15f),new Vector3f(0.68f)),
            //right hand first person
            new Transformation(new Vector3f(0,-90,25),new Vector3f(0,0.25f,0.15f),new Vector3f(0.68f)),
            //head (you dont really need this lmao)
            new Transformation(new Vector3f(0,180,0),new Vector3f(0,0.85f,0),new Vector3f(1)),
            //gui!
            new Transformation(new Vector3f(),new Vector3f(),new Vector3f(1)),
            //ground
            new Transformation(new Vector3f(),new Vector3f(0,0.15f,0),new Vector3f(0.5f)),
            //fixed
            new Transformation(new Vector3f(0,180,0),new Vector3f(),new Vector3f(1))
    );

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        String modelKey = "";
        if (stack.hasNbt())
        {
            assert stack.getNbt() != null;
            modelKey = stack.getNbt().getString("coin_brands");
        }

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();

        itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, getOrCreateBakedModel(modelKey, stack.getItem()));
    }

    public static BakedModel getOrCreateBakedModel(String key, ItemConvertible item)
    {
        if (models.containsKey(key)) return models.get(key);
        else {
            BakedModel baked = createBakedModel(key,item);
            if (baked == null) {
                Gakumini.LOGGER.info("cryyy");
            } else {
                Gakumini.LOGGER.info("dont cry");
            }
            models.put(key,baked);
            return baked;
        }
    }

    public static BakedModel createBakedModel(String key, ItemConvertible item)
    {
        Function<SpriteIdentifier, Sprite> textureGetter = getSpriteFunction();

        Map<String, Either<SpriteIdentifier, String>> map = Maps.newHashMap();

        List<String> layerTextures = NBTUtil.getStringList(key);
        //TODO: this is a bad way to do this. too bad!
        if (item.asItem() == Gakumini.COPPER_COIN) layerTextures.add(0,"gakumini:item/copper_coin");
        if (item.asItem() == Gakumini.DIAMOND_COIN) layerTextures.add(0,"gakumini:item/diamond_coin");
        if (item.asItem() == Gakumini.EMERALD_COIN) layerTextures.add(0,"gakumini:item/emerald_coin");
        if (item.asItem() == Gakumini.GOLD_COIN) layerTextures.add(0,"gakumini:item/gold_coin");
        if (item.asItem() == Gakumini.IRON_COIN) layerTextures.add(0,"gakumini:item/iron_coin");
        if (item.asItem() == Gakumini.NETHERITE_COIN) layerTextures.add(0,"gakumini:item/netherite_coin");
        Gakumini.LOGGER.info("Got textures for coin: " + layerTextures);
        List<SpriteIdentifier> layerTex2 = new ArrayList<>();

        List<ModelElement> modelElements = new ArrayList<>();

        for (int i = 0; i < layerTextures.size(); i++) {
            if (!layerTextures.get(i).contains(":")) layerTextures.set(i, "gakumini:item/" + layerTextures.get(i));
            layerTex2.add(i,new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
                    Gakumini.identifier(layerTextures.get(i))));
            SpriteIdentifier textureId = layerTex2.get(i);
            Sprite sprite = textureGetter.apply(textureId);

            String otherPut = "layer" + i;

            map.put(otherPut, Either.left(textureId));
            modelElements.addAll(((ItemModelGeneratorMixin) itemModelGenerator).addLayerElementsHijack(i, layerTextures.get(i), sprite.getContents()));
        }
        map.put("particle", map.get("layer0"));

        Gakumini.LOGGER.info("Check out this map " + map);

        JsonUnbakedModel unbaked = itemModelGenerator.create(textureGetter, new JsonUnbakedModel(
                null,
                modelElements,
                map,
                false,
                JsonUnbakedModel.GuiLight.ITEM,
                TRANSFORM,
                new ArrayList<>()
        ));
        unbaked.id=key;
        return unbaked.bake(null, null, textureGetter, modelBakeSettings, Gakumini.identifier(key), false);
    }

    public static Function<SpriteIdentifier, Sprite> getSpriteFunction() {

        SpriteAtlasTexture atlas = MinecraftClient.getInstance().getBakedModelManager()
                .getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

        return spriteIdentifier -> atlas.getSprite(spriteIdentifier.getTextureId());
    }

    static class CoinBakeSettings implements ModelBakeSettings
    {
        @Override
        public boolean isUvLocked() {
            return true;
        }
    }
}