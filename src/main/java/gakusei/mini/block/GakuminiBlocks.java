package gakusei.mini.block;

import gakusei.mini.Gakumini;
import gakusei.mini.block.BrandingBlock;
import gakusei.mini.util.BlockPair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class GakuminiBlocks {

    public static final BlockPair SALTPETER_DEPOSIT = registerBlockPair(
            new ExperienceDroppingBlock(FabricBlockSettings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .requiresTool()
                    .strength(4.5F, 3.0F)),
            "deepslate_saltpeter_deposit"
    );

    public static final BlockPair NETHERRACK_SALTPETER_DEPO = registerBlockPair(
            new ExperienceDroppingBlock(FabricBlockSettings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(0.6F)
                    .sounds(BlockSoundGroup.NETHERRACK)
            ),
            "netherrack_saltpeter_deposit"
    );

    public static final BlockPair BRANDING_BLOCK = registerBlockPair(
            new BrandingBlock(FabricBlockSettings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
                    .instrument(Instrument.COW_BELL)
                    .requiresTool()
                    .strength(4.5f, 4.5f)),
            "branding_block"
    );

    public static BlockPair registerBlockPair(Block block, String name)
    {
        return new BlockPair(register(block, name));
    }

    public static Block register(Block block, String name) {
        Identifier id = Gakumini.identifier(name);
        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init()
    {

    }
}
