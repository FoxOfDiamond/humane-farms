package syrena.humanefarms.mixin.impl.entity.animal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import syrena.humanefarms.AnimalFoodData;
import syrena.humanefarms.HumaneFarmsAttributes;
import syrena.humanefarms.ai.goal.SeekFoodGoal;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(Animal.class)
public abstract class AnimalMixin implements AnimalAccess {
    @Shadow
    public abstract void handleEntityEvent(@EntityEvent.Value byte id);

    @Shadow
    public abstract void aiStep();

    @Unique
    private AnimalFoodData foodData = new AnimalFoodData();
    @Unique
    private int animalEatAnimationTick;
    @Unique
    private SeekFoodGoal seekFoodGoal;
    @Unique
    private SeekFoodGoal hungrySeekFoodGoal;
    @Unique
    private SeekFoodGoal starvingSeekFoodGoal;
    @Override
    public AnimalFoodData humanefarms$foodData(){
        return foodData;
    }
    @Override
    public int humanefarms$getEatAnimationTick() {
        return animalEatAnimationTick;
    }
    @Override
    public void humanefarms$setEatAnimationTick(int tick) {
        this.animalEatAnimationTick = tick;
    }

    @Override
    public void humanefarms$setSeekFoodGoals(SeekFoodGoal normal, SeekFoodGoal hungry, SeekFoodGoal starving) {
        this.seekFoodGoal = normal;
        this.hungrySeekFoodGoal = hungry;
        this.starvingSeekFoodGoal = starving;
    }

    @Override
    public SeekFoodGoal humanefarms$seekFoodGoal() {
        return seekFoodGoal;
    }
    @Override
    public SeekFoodGoal humanefarms$hungrySeekFoodGoal() {
        return hungrySeekFoodGoal;
    }
    @Override
    public SeekFoodGoal humanefarms$starvingSeekFoodGoal() {
        return starvingSeekFoodGoal;
    }

    @ModifyReturnValue(method = "createAnimalAttributes",at=@At("RETURN"))
    private static AttributeSupplier.Builder injectAttribute(AttributeSupplier.Builder original){
        original.add(HumaneFarmsAttributes.EXHAUSTION_MULTIPLIER);
        original.add(HumaneFarmsAttributes.MAX_FOOD_LEVEL);
        return original;
    }
    @Inject(method="addAdditionalSaveData",at=@At("TAIL"))
    public void injectSaveData(ValueOutput output, CallbackInfo ci){
        foodData.addAdditionalSaveData(output);
    }
    @Inject(method="readAdditionalSaveData",at=@At("TAIL"))
    public void injectReadData(ValueInput input, CallbackInfo ci){
        foodData.readAdditionalSaveData(input);
    }

    @WrapMethod(method = "customServerAiStep")
    protected void injectCustomServerAiStep(ServerLevel level, Operation<Void> original) {
        this.animalEatAnimationTick = Math.max(Math.max(this.hungrySeekFoodGoal.getEatAnimationTick(),this.starvingSeekFoodGoal.getEatAnimationTick()),this.seekFoodGoal.getEatAnimationTick());
        original.call(level);
    }


    @WrapMethod(method = "aiStep")
    public void handleAiStep(Operation<Void> original) {
        if (((Animal)((Object)this)).level().isClientSide()) {
            this.animalEatAnimationTick = Math.max(0, this.animalEatAnimationTick - 1);
        }

        original.call();
    }
    @WrapMethod(method = "handleEntityEvent")
    public void handleEntityEvent(byte id, Operation<Void> original) {
        if (id == 10) {
            this.animalEatAnimationTick = 40;
        } else {
            original.call(id);
        }
    }

    @Override
    public float humanefarms$getHeadEatPositionScale(final float a) {
        if (this.animalEatAnimationTick <= 0) {
            return 0.0F;
        } else if (this.animalEatAnimationTick >= 4 && this.animalEatAnimationTick <= 36) {
            return 1.0F;
        } else {
            return this.animalEatAnimationTick < 4 ? (this.animalEatAnimationTick - a) / 4.0F : -(this.animalEatAnimationTick - 40 - a) / 4.0F;
        }
    }

    @Override
    public float humanefarms$getHeadEatAngleScale(final float a) {
        if (this.animalEatAnimationTick > 4 && this.animalEatAnimationTick <= 36) {
            float scale = (this.animalEatAnimationTick - 4 - a) / 32.0F;
            return (float) (Math.PI / 5) + 0.21991149F * Mth.sin(scale * 28.7F);
        } else {
            return this.animalEatAnimationTick > 0 ? (float) (Math.PI / 5) : ((Entity)(Object)this).getXRot(a) * (float) (Math.PI / 180.0);
        }
    }
    @Inject(method = "customServerAiStep",at=@At("HEAD"))
    private void injectCustomServerAi(ServerLevel level, CallbackInfo ci){
        this.animalEatAnimationTick = Math.max(Math.max((this.hungrySeekFoodGoal.getEatAnimationTick()),this.starvingSeekFoodGoal.getEatAnimationTick()),this.seekFoodGoal.getEatAnimationTick());
    }
    @Inject(method = "aiStep",at=@At("HEAD"))
    private void injectAiStep(CallbackInfo ci){
        if (((LivingEntity)(Object)this).level().isClientSide()) {
            this.animalEatAnimationTick = Math.max(0, this.animalEatAnimationTick - 1);
        }
    }
}
