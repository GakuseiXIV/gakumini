package gakusei.mini.mixin.shield_rework;

import gakusei.mini.GakuminiGamerules;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method="takeShieldHit", at=@At("HEAD"), cancellable = true)
    public void disableShieldOnHit(LivingEntity attacker, CallbackInfo ci) {
        super.takeShieldHit(attacker);

        int shieldDisableTicks = getWorld().getGameRules().getInt(GakuminiGamerules.SHIELD_DISABLE_TICKS);

        if (attacker.disablesShield()) shieldDisableTicks = getWorld().getGameRules().getInt(GakuminiGamerules.STRONG_SHIELD_DISABLE_TICKS);

        if (shieldDisableTicks > 0)
        {
            getItemCooldownManager().set(Items.SHIELD, shieldDisableTicks);
            clearActiveItem();
            getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
        }

        ci.cancel();
    }
}
