package syrena.humanefarms;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class HumaneFarmsGameRules {
    public static final GameRule<Boolean> ANIMAL_STARVES = GameRuleBuilder
            .forBoolean(true)
            .category(GameRuleCategory.MOBS)
            .buildAndRegister(HumaneFarms.id("animal_starves"));
    public static final GameRule<Double> ANIMAL_EXHAUSTION_RATE = GameRuleBuilder
            .forDouble(0.005)
            .category(GameRuleCategory.MOBS)
            .buildAndRegister(HumaneFarms.id("animal_exhaustion_rate"));
    public static void initialize() {

    }
}
