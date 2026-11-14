package syrena.humanefarms;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;


public class HumaneFarmsAttributes {
    public static final Holder<Attribute> MAX_FOOD_LEVEL = register("max_food_level",
            new RangedAttribute(HumaneFarms.id("max_food_level").toLanguageKey(),20.0,0.0,1024.0));
    public static final Holder<Attribute> EXHAUSTION_MULTIPLIER = register("exhaustion_multiplier",
            new RangedAttribute(HumaneFarms.id("exhaustion_multiplier").toLanguageKey(),1.0,0.0,1024.0));
    private static Holder<Attribute> register(final String name, final Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, HumaneFarms.id(name), attribute);
    }
    public static void initialize(){}
}
