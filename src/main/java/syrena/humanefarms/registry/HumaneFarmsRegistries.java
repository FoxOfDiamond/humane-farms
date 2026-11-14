package syrena.humanefarms.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import syrena.humanefarms.HumaneFarms;

public class HumaneFarmsRegistries {
    public static TagKey<DamageType> IS_STARVE = TagKey.create(Registries.DAMAGE_TYPE, HumaneFarms.id("is_starve"));
    public static TagKey<EntityType<?>> IS_ANIMAL = TagKey.create(Registries.ENTITY_TYPE, HumaneFarms.id("is_animal"));
    final public static void registerAll(){

    }
}
