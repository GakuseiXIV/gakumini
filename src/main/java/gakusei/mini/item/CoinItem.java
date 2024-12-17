package gakusei.mini.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class CoinItem extends SignableItem {

    public CoinItem(Settings settings) {
        super(settings, "gakumini.coinMadeBy");
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains("signer")) return Rarity.COMMON;
        return Rarity.UNCOMMON;
    }
}
