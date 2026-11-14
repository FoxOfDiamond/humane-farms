package syrena.humanefarms.mixin.access;

import syrena.humanefarms.AnimalFoodData;
import syrena.humanefarms.ai.goal.SeekFoodGoal;

public interface AnimalAccess {
    AnimalFoodData humanefarms$foodData();
    int humanefarms$getEatAnimationTick();
    void humanefarms$setEatAnimationTick(int tick);
    void humanefarms$setSeekFoodGoals(SeekFoodGoal normal, SeekFoodGoal hungry, SeekFoodGoal starving);
    SeekFoodGoal humanefarms$seekFoodGoal();
    SeekFoodGoal humanefarms$hungrySeekFoodGoal();
    SeekFoodGoal humanefarms$starvingSeekFoodGoal();
    float humanefarms$getHeadEatPositionScale(float a);
    float humanefarms$getHeadEatAngleScale(float a);
}
