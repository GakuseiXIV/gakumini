package gakusei.mini.mixin.totem_sickness;

import gakusei.mini.Gakumini;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tryUseTotem", at=@At("HEAD"), cancellable = true)
    public void testForTotemSickness(DamageSource source, CallbackInfoReturnable<Boolean> cir)
    {
        if (((LivingEntity)(Object) this).hasStatusEffect(Gakumini.TOTEM_SICKNESS))
        {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "tryUseTotem", at=@At("RETURN"))
    public void applyTotemSickness(DamageSource source, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue()) {
            ((LivingEntity) (Object) this).addStatusEffect(new StatusEffectInstance(Gakumini.TOTEM_SICKNESS, 3600, 0));
        }
    }
}
