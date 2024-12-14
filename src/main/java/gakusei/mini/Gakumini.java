package gakusei.mini;

import gakusei.mini.block.GakuminiBlocks;
import gakusei.mini.effect.GenericEffect;
import gakusei.mini.item.CoinItem;
import gakusei.mini.recipe.BrandRecipeSerializer;
import gakusei.mini.recipe.BrandingRecipe;
import gakusei.mini.screen.BrandingScreenHandler;
import gakusei.mini.util.ColorUtil;
import io.wispforest.owo.ui.core.Color;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gakumini implements ModInitializer {
	public static final String MOD_ID = "gakumini";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final StatusEffect TOTEM_SICKNESS = Registry.register(
			Registries.STATUS_EFFECT, identifier("totem_sickness"), new GenericEffect(StatusEffectCategory.HARMFUL,
					ColorUtil.IntFromHex("15247b"))
	);

	public static final Item SALTPETER = makeBoringItem("saltpeter");
	public static final Item WITHERBONE = makeBoringItem("witherbone");
	
	public static final Item CIRCLE_BRAND = makeBoringItem("circle_brand");
	public static final Item PLUS_BRAND = makeBoringItem("plus_brand");
	public static final Item RING_BRAND = makeBoringItem("ring_brand");

	public static final Item COPPER_COIN = makeCoinItem("copper_coin");
	public static final Item DIAMOND_COIN = makeCoinItem("diamond_coin");
	public static final Item EMERALD_COIN = makeCoinItem("emerald_coin");
	public static final Item GOLD_COIN = makeCoinItem("gold_coin");
	public static final Item IRON_COIN = makeCoinItem("iron_coin");
	public static final Item NETHERITE_COIN = makeCoinItem("netherite_coin");


	public static final ScreenHandlerType<BrandingScreenHandler> BRANDING_SCREEN_HANDLER_TYPE = Registry.register(
			Registries.SCREEN_HANDLER, identifier("branding_sht"), new ScreenHandlerType<>(BrandingScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

	public static final RecipeType<BrandingRecipe> BRANDING_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, identifier("branding"), new RecipeType<BrandingRecipe>() {
				public String toString() {
					return "branding";
				}
			}
	);

	public static Identifier identifier(String path) {
		if (path.contains(":")) return new Identifier(path);
		return Identifier.of(MOD_ID, path);
	}
	public static Item makeCoinItem(String path) {
		return Registry.register(Registries.ITEM,
				identifier(path),
				new CoinItem(new FabricItemSettings()));
	}
	public static Item makeBoringItem(String path) {
		return Registry.register(Registries.ITEM,
				identifier(path),
				new Item(new FabricItemSettings()));
	}


	@Override
	public void onInitialize() {
		GakuminiBlocks.init();
		GakuminiFeatures.init();
		GakuminiGamerules.init();
		GakuminiStats.init();
		GakuminiTags.init();
		GakuminiSounds.init();

		Registry.register(Registries.RECIPE_SERIALIZER, BrandRecipeSerializer.ID,
				BrandRecipeSerializer.INSTANCE);

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_ORES, GakuminiFeatures.SALTPETER_DEPOSIT_FEATURE);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.UNDERGROUND_ORES, GakuminiFeatures.NETHER_SALTPETER_DEPOSIT_FEATURE);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
				.register((itemGroup) -> itemGroup.addAfter(Items.RAW_GOLD, SALTPETER));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
				.register((itemGroup) -> itemGroup.addAfter(Items.BONE, WITHERBONE));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
				.register((itemGroup) -> itemGroup.addAfter(Items.DEEPSLATE_GOLD_ORE,GakuminiBlocks.SALTPETER_DEPOSIT.item));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
				.register((itemGroup) -> itemGroup.addAfter(GakuminiBlocks.SALTPETER_DEPOSIT.item, GakuminiBlocks.NETHERRACK_SALTPETER_DEPO.item));
	}
}