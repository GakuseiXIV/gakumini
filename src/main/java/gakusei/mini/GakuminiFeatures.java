package gakusei.mini;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;

public class GakuminiFeatures {
    public static final RegistryKey<PlacedFeature> SALTPETER_DEPOSIT_FEATURE = getKey("saltpeter_deposit");
    public static final RegistryKey<PlacedFeature> NETHER_SALTPETER_DEPOSIT_FEATURE = getKey("nether_saltpeter_deposit");
    public static RegistryKey<PlacedFeature> getKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Gakumini.MOD_ID, name));
    }
    public static void init() {}
}
