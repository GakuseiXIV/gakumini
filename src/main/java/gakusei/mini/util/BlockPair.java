package gakusei.mini.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockPair {
    public Block block;
    public Item item;

    public BlockPair(Block block)
    {
        this.block = block;
        item = matchingItem(block);
    }
    public static Item matchingItem(Block block)
    {
        Identifier id = Registries.BLOCK.getId(block);
        BlockItem blockItem = new BlockItem(block, new FabricItemSettings());
        return Registry.register(Registries.ITEM, id, blockItem);
    }
}