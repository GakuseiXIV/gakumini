package gakusei.mini;

import gakusei.mini.util.BlockPair;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;

public class GakuminiBlocks {

    public static final BlockPair SALTPETER_DEPOSIT = registerBlockPair(
            new ExperienceDroppingBlock(ConstantIntProvider.create(0), AbstractBlock.Settings.create()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Gakumini.identifier("deepslate_saltpeter_deposit")))
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .requiresTool()
                    .strength(4.5F, 3.0F)),
            "deepslate_saltpeter_deposit"
    );

    public static final BlockPair NETHERRACK_SALTPETER_DEPO = registerBlockPair(
            new ExperienceDroppingBlock(ConstantIntProvider.create(0), AbstractBlock.Settings.create()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Gakumini.identifier("netherrack_saltpeter_deposit")))
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(0.6F)
                    .sounds(BlockSoundGroup.NETHERRACK)
            ),
            "netherrack_saltpeter_deposit"
    );

    public static BlockPair registerBlockPair(Block block, String name)
    {
        return new BlockPair(register(block, name), matchingItem(block));
    }
    public static Item matchingItem(Block block)
    {
        Identifier id = Registries.BLOCK.getId(block);
        BlockItem blockItem = new BlockItem(block, new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, id)));
        return Registry.register(Registries.ITEM, id, blockItem);
    }

    public static Block register(Block block, String name) {
        Identifier id = Gakumini.identifier(name);
        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init()
    {

    }
}
