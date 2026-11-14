package syrena.humanefarms.mixin.impl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.HungerMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import syrena.humanefarms.Util;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(HungerMobEffect.class)
public class HungerMobEffectMixin {
    @Inject(method = "applyEffectTick",at=@At("HEAD"))
    private void injectAnimalTick(ServerLevel serverLevel, LivingEntity mob, int amplification, CallbackInfoReturnable<Boolean> cir){
        if (Util.isAnimal(mob) && !mob.level().isClientSide() && !mob.isInvulnerable()){
            ((AnimalAccess)mob).humanefarms$foodData().addExhaustion(0.005f * (amplification + 1));
        }
    }
}
