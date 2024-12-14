package gakusei.mini.recipe;

import gakusei.mini.Gakumini;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BrandingRecipe implements BrandingRecipeType {

    private final Ingredient coin_input;
    private final Ingredient brand_input;
    private final Ingredient material_input;
    private final ItemStack output;
    private final Identifier id;

    public BrandingRecipe(Identifier id, ItemStack result, Ingredient coin_input, Ingredient brand_input,
                          Ingredient material_input)

    {
        this.id = id;
        this.output = result;
        this.coin_input = coin_input;
        this.brand_input = brand_input;
        this.material_input = material_input;
    }

    public Ingredient getCoinInput()
    {
        return coin_input;
    }
    public Ingredient getBrandInput()
    {
        return brand_input;
    }
    public Ingredient getMaterialInput()
    {
        return material_input;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (inventory.size() < 3) return false;
        return coin_input.test(inventory.getStack(0)) && brand_input.test(inventory.getStack(1))
                && material_input.test(inventory.getStack(2));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BrandRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Gakumini.BRANDING_RECIPE_TYPE;
    }
}
