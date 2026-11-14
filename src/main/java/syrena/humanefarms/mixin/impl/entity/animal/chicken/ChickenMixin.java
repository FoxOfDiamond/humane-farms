package syrena.humanefarms.mixin.impl.entity.animal.chicken;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.chicken.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import syrena.humanefarms.HumaneFarmsAttributes;

@Mixin(Chicken.class)
public class ChickenMixin {
    @ModifyReturnValue(method = "createAttributes",at=@At("RETURN"))
    private static AttributeSupplier.Builder injectAttribute(AttributeSupplier.Builder original){
        original.add(HumaneFarmsAttributes.MAX_FOOD_LEVEL,4);
        original.add(HumaneFarmsAttributes.EXHAUSTION_MULTIPLIER,1);
        return original;
    }
}