package syrena.humanefarms.client.mixin.impl.entity.animal;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import syrena.humanefarms.client.mixin.access.AnimalRenderStateAccess;
import syrena.humanefarms.registry.HumaneFarmsRegistries;

@Mixin(QuadrupedModel.class)
public class QuadrupedModelMixin<T extends LivingEntityRenderState> {
    @Shadow
    @Final
    protected ModelPart head;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)V",at=@At("TAIL"))
    public void injectEatAnim(T state, CallbackInfo ci){
        if (state.entityType.builtInRegistryHolder().is(HumaneFarmsRegistries.IS_ANIMAL)){
            AnimalRenderStateAccess access = (AnimalRenderStateAccess) state;
            this.head.y += access.humanefarms$getHeadEatPositionScale() * 9f * state.ageScale;
            this.head.xRot = access.humanefarms$getHeadEatAngleScale();
        }
    }
}
