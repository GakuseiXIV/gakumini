package gakusei.mini.mixin.totem_sickness;

import gakusei.mini.Gakumini;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {

    public MilkBucketItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "finishUsing", at=@At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    public boolean rejectTotemSicknessRemoval(LivingEntity user)
    {
        if (user.hasStatusEffect(Gakumini.TOTEM_SICKNESS)) {
            boolean bl = user.hasStatusEffect(Gakumini.TOTEM_SICKNESS);
            StatusEffectInstance sicknessInstance = null;
            if (bl) sicknessInstance = user.getStatusEffect(Gakumini.TOTEM_SICKNESS);

            user.clearStatusEffects();

            if (bl) {
                user.addStatusEffect(sicknessInstance);
                if (user instanceof ServerPlayerEntity)
                {
                    ((ServerPlayerEntity) user).sendMessageToClient(
                            Text.translatable("gakumini.totem_sickness_remove"),
                            true
                    );
                }

            }

            return true;
        }
        return user.clearStatusEffects();
    }
}
