package syrena.humanefarms;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AnimalFoodData {
    private static final int DEFAULT_TICK_TIMER = 0;
    private static final float DEFAULT_EXHAUSTION_LEVEL = 0.0F;
    public int foodLevel = 20;
    private float saturationLevel = 5.0F;
    private float exhaustionLevel;
    private int tickTimer;

    private void add(final int food, final float saturation) {
        this.foodLevel = Mth.clamp(food + this.foodLevel, 0, 20);
        this.saturationLevel = Mth.clamp(saturation + this.saturationLevel, 0.0F, this.foodLevel);
    }

    public void eat(final int food, final float saturationModifier) {
        this.add(food, FoodConstants.saturationByModifier(food, saturationModifier));
    }
    public void addAdditionalSaveData(final ValueOutput output) {
        output.putInt("foodLevel", this.foodLevel);
        output.putInt("foodTickTimer", this.tickTimer);
        output.putFloat("foodSaturationLevel", this.saturationLevel);
        output.putFloat("foodExhaustionLevel", this.exhaustionLevel);
    }
    
    public void readAdditionalSaveData(final ValueInput input) {
        this.foodLevel = input.getIntOr("foodLevel", 20);
        this.tickTimer = input.getIntOr("foodTickTimer", 0);
        this.saturationLevel = input.getFloatOr("foodSaturationLevel", 5.0F);
        this.exhaustionLevel = input.getFloatOr("foodExhaustionLevel", 0.0F);
    }

    public void addExhaustion(final float amount) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + amount, 40.0F);
    }

    public int maxFoodLevel(final Animal animal){
        return (int) animal.getAttributeValue(HumaneFarmsAttributes.MAX_FOOD_LEVEL);
    }
    public void tick(final Animal animal, final ServerLevel serverLevel) {
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        int maxFoodLevel = maxFoodLevel(animal);
        double exhaustionMultiplier = animal.getAttributeValue(HumaneFarmsAttributes.EXHAUSTION_MULTIPLIER);
        boolean isHurt = animal.getHealth() > 0.0F && animal.getHealth() < animal.getMaxHealth();
        this.foodLevel = Math.min(this.foodLevel, maxFoodLevel);
        this.addExhaustion((float)(serverLevel.getGameRules().get(HumaneFarmsGameRules.ANIMAL_EXHAUSTION_RATE) / 20 * exhaustionMultiplier));
        if (this.saturationLevel > 0.0F && isHurt && this.foodLevel >= maxFoodLevel) {
            this.tickTimer++;
            if (this.tickTimer >= 10) {
                float saturationSpent = Math.min(this.saturationLevel, 6.0F);
                animal.heal(saturationSpent / 6.0F);
                this.addExhaustion(saturationSpent * (float) exhaustionMultiplier);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel >= maxFoodLevel * 0.5 && isHurt) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                animal.heal(1.0F);
                this.addExhaustion(6.0F * (float) exhaustionMultiplier);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                animal.hurtServer(serverLevel, animal.damageSources().starve(), 1.0F);

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }
}
