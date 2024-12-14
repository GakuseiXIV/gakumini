package gakusei.mini.recipe;

import gakusei.mini.Gakumini;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;

public interface BrandingRecipeType extends Recipe<Inventory> {
    default RecipeType<?> getType() {
        return Gakumini.BRANDING_RECIPE_TYPE;
    }

    default ItemStack createIcon() {
        return new ItemStack(Gakumini.GOLD_COIN);
    }
}
