package syrena.humanefarms.mixin.impl.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import syrena.humanefarms.AnimalFoodData;
import syrena.humanefarms.HumaneFarms;
import syrena.humanefarms.HumaneFarmsAttributes;
import syrena.humanefarms.HumaneFarmsGameRules;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "baseTick",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V"))
    public void applyTickExhaustion(CallbackInfo ci){
        if ((Object)this instanceof Animal animal && animal.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(HumaneFarmsGameRules.ANIMAL_STARVES).booleanValue()){
            AnimalFoodData foodData = ((AnimalAccess) animal).humanefarms$foodData();
            foodData.tick(animal,serverLevel);
            //HumaneFarms.LOGGER.info("Ticking");
        }
    }

    @Inject(method = "actuallyHurt",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    public void applyDamageExhaustion(CallbackInfo ci, @Local(argsOnly = true, name = "source") DamageSource source){
        if ((Object)this instanceof Animal animal && animal.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(HumaneFarmsGameRules.ANIMAL_STARVES).booleanValue()){
            AnimalFoodData foodData = ((AnimalAccess) animal).humanefarms$foodData();
            foodData.addExhaustion(source.getFoodExhaustion() * (float)animal.getAttributeValue(HumaneFarmsAttributes.EXHAUSTION_MULTIPLIER));
        }
    }
}
