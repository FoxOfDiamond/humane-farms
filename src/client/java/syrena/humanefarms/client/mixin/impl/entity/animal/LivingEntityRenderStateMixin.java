package syrena.humanefarms.client.mixin.impl.entity.animal;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import syrena.humanefarms.client.mixin.access.AnimalRenderStateAccess;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements AnimalRenderStateAccess {
    @Unique
    private float headEatPositionScale;
    @Unique
    private float headEatAngleScale;

    @Override
    public float humanefarms$getHeadEatPositionScale() {
        return this.headEatPositionScale;
    }
    @Override
    public void humanefarms$setHeadEatPositionScale(float value) {
        this.headEatPositionScale = value;
    }
    @Override
    public float humanefarms$getHeadEatAngleScale() {
        return this.headEatAngleScale;
    }
    @Override
    public void humanefarms$setHeadEatAngleScale(float value) {
        this.headEatAngleScale = value;
    }
}
