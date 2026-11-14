package syrena.humanefarms.client.mixin.impl.entity.animal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepModel.class)
public class SheepModelMixin<T extends LivingEntityRenderState> {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/SheepRenderState;)V", at= @At(value = "INVOKE", shift = At.Shift.AFTER,target = "Lnet/minecraft/client/model/QuadrupedModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)V"), cancellable = true)
    private void disableDefaultEatAnimPos(SheepRenderState state, CallbackInfo ci){
        ci.cancel();
    }
}
