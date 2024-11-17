package gakusei.mini.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockPair {
    public Block block;
    public Item item;

    public BlockPair(Block block, Item item)
    {
        this.block = block;
        this.item = item;
    }
}