package gakusei.mini.client;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import gakusei.mini.Gakumini;
import gakusei.mini.GakuminiTags;
import gakusei.mini.mixin.coin_models.ItemModelGeneratorMixin;
import gakusei.mini.mixin.coin_models.SpriteAtlasTextureMixin;
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
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModularCoinRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static final BakedQuadFactory QUAD_FACTORY = new BakedQuadFactory();
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
            modelKey += stack.getNbt().getString("coin_brands");
        }

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();

        itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, getOrCreateBakedModel(modelKey, stack.getItem()));
    }

    public static BakedModel getOrCreateBakedModel(String key, ItemConvertible item)
    {
        key = "gakumini:coins/" + Gakumini.coinMaterialMap.get(item.asItem()) + "_coin;" + key;

        if (models.containsKey(key)) return models.get(key);
        else {
            Gakumini.LOGGER.info("Creating coin model for coin: " + key);
            BakedModel baked = createBakedModel(key,item);
            models.put(key,baked);
            return baked;
        }
    }

    public static BakedModel createBakedModel(String key, ItemConvertible item)
    {
        Function<SpriteIdentifier, Sprite> textureGetter = getSpriteFunction();

        Map<String, Either<SpriteIdentifier, String>> map = Maps.newHashMap();

        List<String> layerTextures = NBTUtil.getStringList(key);

        List<SpriteIdentifier> layerTex2 = new ArrayList<>();

        List<ModelElement> modelElements = new ArrayList<>();

        for (int i = 0; i < layerTextures.size(); i++) {
            if (!layerTextures.get(i).contains(":")) layerTextures.set(i, "coin_brands/" + layerTextures.get(i));

            layerTex2.add(i,new SpriteIdentifier(GakuminiClient.brandAtlas,
                    Gakumini.identifier(layerTextures.get(i))));

            SpriteIdentifier textureId = layerTex2.get(i);
            Sprite sprite = textureGetter.apply(textureId);

            String otherPut = "layer" + i;

            map.put(otherPut, Either.left(textureId));
            modelElements.addAll(((ItemModelGeneratorMixin) itemModelGenerator).addLayerElementsHijack(i, layerTextures.get(i), sprite.getContents()));
        }
        map.put("particle", map.get("layer0"));

        String newKey = "";
        for (String s : key.split(";"))
        {
            if (!s.contains(":")) newKey = newKey + s + ";";
        }

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
        return unbaked.bake(null, null, textureGetter, modelBakeSettings, Gakumini.identifier(newKey), false);
    }

    public static Function<SpriteIdentifier, Sprite> getSpriteFunction() {
        SpriteAtlasTexture atlas = MinecraftClient.getInstance().getBakedModelManager()
                .getAtlas(GakuminiClient.brandAtlas);
        return spriteIdentifier -> atlas.getSprite(spriteIdentifier.getTextureId());
    }

    public static void clearModels()
    {
        Gakumini.LOGGER.info("Clearing out {} models", models.size());
        models.clear();
    }

    static class CoinBakeSettings implements ModelBakeSettings
    {
        @Override
        public boolean isUvLocked() {
            return true;
        }
    }
}