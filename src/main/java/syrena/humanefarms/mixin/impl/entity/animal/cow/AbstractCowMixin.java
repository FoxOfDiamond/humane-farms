package syrena.humanefarms.mixin.impl.entity.animal.cow;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import syrena.humanefarms.HumaneFarmsAttributes;
import syrena.humanefarms.ai.goal.SeekFoodGoal;
import syrena.humanefarms.ai.goal.StarvingPanicGoal;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(AbstractCow.class)
public class AbstractCowMixin {
    @ModifyReturnValue(method = "createAttributes",at=@At("RETURN"))
    private static AttributeSupplier.Builder injectAttribute(AttributeSupplier.Builder original){
        original.add(HumaneFarmsAttributes.MAX_FOOD_LEVEL,10);
        return original;
    }

    @Inject(method = "registerGoals",at=@At("HEAD"))
    private void setupGoals(CallbackInfo ci){
        ((AnimalAccess)this).humanefarms$setSeekFoodGoals(
                new SeekFoodGoal((AbstractCow)(Object) this,1,3,0.9,0),
                new SeekFoodGoal((AbstractCow)(Object) this,1.12,5,0.7,0.2),
                new SeekFoodGoal((AbstractCow)(Object) this,1.25,7,0.2,0.1)
        );
    }
    @WrapWithCondition(method="registerGoals",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 5))
    private boolean registerNormalGoal(GoalSelector instance, int prio, Goal goal){
        instance.addGoal(5,((AnimalAccess)this).humanefarms$seekFoodGoal());
        return false;
    }
    @WrapWithCondition(method="registerGoals",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 4))
    private boolean registerHungryGoal(GoalSelector instance, int prio, Goal goal){
        instance.addGoal(4,((AnimalAccess)this).humanefarms$hungrySeekFoodGoal());
        return true;
    }
    @WrapWithCondition(method="registerGoals",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean registerStarvingGoal(GoalSelector instance, int prio, Goal goal){
        instance.addGoal(1,((AnimalAccess)this).humanefarms$starvingSeekFoodGoal());
        instance.addGoal(2, new StarvingPanicGoal((AbstractCow)((Object) this),1.25));
        return true;
    }
}