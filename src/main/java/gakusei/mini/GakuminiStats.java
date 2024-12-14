package gakusei.mini;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class GakuminiStats {

    public static final Identifier INTERACT_WITH_BRANDING_BLOCK = register(
            "interact_with_branding", StatFormatter.DEFAULT
    );

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = Gakumini.identifier(id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public static void init() {}
}
