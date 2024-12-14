package gakusei.mini;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class GakuminiTags {

    public static final TagKey<Item> BRANDING_MATERIALS = TagKey.of(
            RegistryKeys.ITEM, Gakumini.identifier("branding_materials")
    );
    public static final TagKey<Item> BRANDING_BRANDS = TagKey.of(
            RegistryKeys.ITEM, Gakumini.identifier("branding_brands")
    );
    public static final TagKey<Item> GAKUMINI_COINS = TagKey.of(
            RegistryKeys.ITEM, Gakumini.identifier("gakumini_coins")
    );

    public static void init(){}
}
