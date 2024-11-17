package gakusei.mini;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gakumini implements ModInitializer {
	public static final String MOD_ID = "gakumini";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Item SALTPETER = makeBoringItem("saltpeter");
	public static final Item WITHERBONE = makeBoringItem("witherbone");

	public static Identifier identifier(String path)
	{
		return Identifier.of(MOD_ID, path);
	}
	public static Item makeBoringItem(String path)
	{
		return Registry.register(Registries.ITEM,
				identifier(path),
				new Item(new FabricItemSettings()));
	}

	@Override
	public void onInitialize() {
		GakuminiBlocks.init();
		GakuminiFeatures.init();
		GakuminiGamerules.init();

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