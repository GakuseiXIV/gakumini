package gakusei.mini;

import gakusei.mini.block.GakuminiBlocks;
import gakusei.mini.effect.GenericEffect;
import gakusei.mini.item.CoinItem;
import gakusei.mini.recipe.BrandRecipeSerializer;
import gakusei.mini.recipe.BrandingRecipe;
import gakusei.mini.screen.BrandingScreenHandler;
import gakusei.mini.util.ColorUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gakumini implements ModInitializer {
	public static final String MOD_ID = "gakumini";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static List<Item> coins = new ArrayList<>();

	public static Map<Item, String> coinBrandMap = new HashMap<>();
	public static Map<Item, String> coinMaterialMap = new HashMap<>();

	public static final StatusEffect TOTEM_SICKNESS = Registry.register(
			Registries.STATUS_EFFECT, identifier("totem_sickness"), new GenericEffect(StatusEffectCategory.HARMFUL,
					ColorUtil.IntFromHex("15247b"))
	);

	public static final Item SALTPETER = makeBoringItem("saltpeter");
	public static final Item WITHERBONE = makeBoringItem("witherbone");
	
	public static final Item CIRCLE_BRAND = makeBrandItem("circle_brand","circle");
	public static final Item PLUS_BRAND = makeBrandItem("plus_brand","plus");
	public static final Item RING_BRAND = makeBrandItem("ring_brand","ring");

	public static final Item COPPER_COIN = makeCoinItem("copper_coin","copper");
	public static final Item DIAMOND_COIN = makeCoinItem("diamond_coin","diamond");
	public static final Item EMERALD_COIN = makeCoinItem("emerald_coin","emerald");
	public static final Item GOLD_COIN = makeCoinItem("gold_coin","gold");
	public static final Item IRON_COIN = makeCoinItem("iron_coin","iron");
	public static final Item NETHERITE_COIN = makeCoinItem("netherite_coin","netherite");


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
	public static Item makeCoinItem(String path, String material) {
		Item i = Registry.register(Registries.ITEM,
				identifier(path),
				new CoinItem(new FabricItemSettings()));
		coins.add(i);
		coinMaterialMap.put(i, material);
		return i;
	}
	public static Item makeBoringItem(String path) {
		return Registry.register(Registries.ITEM,
				identifier(path),
				new Item(new FabricItemSettings()));
	}
	public static Item makeBrandItem(String path, String brand) {
		Item item = Registry.register(Registries.ITEM,
				identifier(path),
				new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).maxCount(1)));
		coinBrandMap.put(item, brand);
		return item;
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
				.register((itemGroup) -> {
					itemGroup.addAfter(Items.RAW_GOLD, SALTPETER);
					itemGroup.addAfter(Items.BONE, WITHERBONE);
					for (Item item : coins) {
						itemGroup.add(item);
					}
					for (Item item : coinBrandMap.keySet()) {
						itemGroup.add(item);
					}
				});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
				.register((itemGroup) -> {
					itemGroup.addAfter(Items.DEEPSLATE_GOLD_ORE,GakuminiBlocks.SALTPETER_DEPOSIT.item);
					itemGroup.addAfter(GakuminiBlocks.SALTPETER_DEPOSIT.item, GakuminiBlocks.NETHERRACK_SALTPETER_DEPO.item);
				});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
				.register((itemGroup) -> {
					itemGroup.add(GakuminiBlocks.BRANDING_BLOCK.item);
				});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (!source.isBuiltin()) return;

			if (isValidForBrandLoot(id)) tableBuilder.pool(coinBrandEntries());
		});
	}

	public static LootPool.Builder coinBrandEntries()
	{
		LootPool.Builder f = LootPool.builder();
		int i = 0;
		for (Item item : coinBrandMap.keySet()) {
			i++;
			f = f.with(ItemEntry.builder(item).weight(5));
		}
		return f.with(ItemEntry.builder(Items.AIR).weight(i*5));
	}

	public static boolean isValidForBrandLoot(Identifier id) {
		List<Identifier> lootTables = List.of(
				LootTables.SHIPWRECK_TREASURE_CHEST,
				LootTables.BURIED_TREASURE_CHEST,
				LootTables.DESERT_PYRAMID_CHEST,
				LootTables.JUNGLE_TEMPLE_CHEST,
				LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST,
				LootTables.ABANDONED_MINESHAFT_CHEST,
				LootTables.RUINED_PORTAL_CHEST,
				LootTables.SIMPLE_DUNGEON_CHEST,
				LootTables.VILLAGE_TOOLSMITH_CHEST);
		for (Identifier i : lootTables) {
			if (id.equals(i)) return true;
		}
		return false;
	}
}