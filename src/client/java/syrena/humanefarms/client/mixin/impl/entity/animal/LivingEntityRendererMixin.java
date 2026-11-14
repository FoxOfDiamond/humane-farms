package syrena.humanefarms.client.mixin.impl.entity.animal;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.cow.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import syrena.humanefarms.Util;
import syrena.humanefarms.client.mixin.access.AnimalRenderStateAccess;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",at=@At("TAIL"))
    private void injectProperties(T entity, S state, float partialTicks, CallbackInfo ci){
        if (Util.isAnimal(entity)){
            AnimalRenderStateAccess stateAccess = (AnimalRenderStateAccess) state;
            AnimalAccess animalAccess = (AnimalAccess) entity;
            stateAccess.humanefarms$setHeadEatAngleScale(animalAccess.humanefarms$getHeadEatAngleScale(partialTicks));
            stateAccess.humanefarms$setHeadEatPositionScale(animalAccess.humanefarms$getHeadEatPositionScale(partialTicks));
        }
    }
}
