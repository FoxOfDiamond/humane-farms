package syrena.humanefarms.mixin.impl.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import syrena.humanefarms.mixin.access.AnimalAccess;

@Mixin(Mob.class)
public class MobMixin {
    @WrapMethod(method = "ate")
    private void overrideAte(Operation<Void> original){
        if ((Object)this instanceof Animal animal){
            ((AnimalAccess) animal).humanefarms$foodData().eat(2,1);
        }
    }
}
