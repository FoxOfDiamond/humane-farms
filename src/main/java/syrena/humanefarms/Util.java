package syrena.humanefarms;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import syrena.humanefarms.registry.HumaneFarmsRegistries;

public class Util {

    public static boolean isAnimal(LivingEntity entity){
        return entity.typeHolder().is(HumaneFarmsRegistries.IS_ANIMAL);
    }
}
