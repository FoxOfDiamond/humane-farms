package syrena.humanefarms.ai.goal;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import syrena.humanefarms.HumaneFarms;
import syrena.humanefarms.registry.HumaneFarmsRegistries;

public class StarvingPanicGoal extends PanicGoal {
    public StarvingPanicGoal(Animal animal, double speedModifier) {
        super(animal, speedModifier, HumaneFarmsRegistries.IS_STARVE);
    }

}
