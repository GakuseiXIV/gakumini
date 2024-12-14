package gakusei.mini.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BrandRecipeSerializer implements RecipeSerializer<BrandingRecipe> {

    private BrandRecipeSerializer() {}

    public static final BrandRecipeSerializer INSTANCE = new BrandRecipeSerializer();

    public static final Identifier ID = new Identifier("gakumini:branding_recipe");

    @Override
    public BrandingRecipe read(Identifier id, JsonObject json) {
        BrandingRecipeJson recipeJson = new Gson().fromJson(json, BrandingRecipeJson.class);
        if (recipeJson.coinInput == null || recipeJson.brandInput == null || recipeJson.output == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }

        return new BrandingRecipe(
            id,
            new ItemStack(Registries.ITEM.getOrEmpty(new Identifier(recipeJson.output))
                    .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.output)), 1),
            Ingredient.fromJson(recipeJson.coinInput),
            Ingredient.fromJson(recipeJson.brandInput),
            Ingredient.fromJson(recipeJson.materialInput)
        );
    }

    @Override
    public BrandingRecipe read(Identifier id, PacketByteBuf buf) {
        return new BrandingRecipe(
                id,
                buf.readItemStack(),
                Ingredient.fromPacket(buf),
                Ingredient.fromPacket(buf),
                Ingredient.fromPacket(buf)
        );
    }

    @Override
    public void write(PacketByteBuf buf, BrandingRecipe recipe) {
        buf.writeItemStack(recipe.getOutput());
        recipe.getCoinInput().write(buf);
        recipe.getBrandInput().write(buf);
        recipe.getMaterialInput().write(buf);
    }
}
