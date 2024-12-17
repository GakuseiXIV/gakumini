package gakusei.mini.mixin.coin_models;

import gakusei.mini.client.GakuminiClient;
import gakusei.mini.client.ModularCoinRenderer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
    public void coinReload(CallbackInfoReturnable<CompletableFuture<Void>> cir)
    {
        ModularCoinRenderer.clearModels();
    }

    @Inject(method = "disconnect()V", at = @At("HEAD"))
    public void exitWorldCoinReload(CallbackInfo ci)
    {
        ModularCoinRenderer.clearModels();
    }
}
