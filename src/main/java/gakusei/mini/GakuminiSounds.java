package gakusei.mini;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class GakuminiSounds {

    public static final SoundEvent CRAFT_BRANDING = register("craft_branding");

    public static SoundEvent register(String id)
    {
        return Registry.register(Registries.SOUND_EVENT,
                Gakumini.identifier(id), SoundEvent.of(Gakumini.identifier(id)));
    }

    public static void init () {}
}
