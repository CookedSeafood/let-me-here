package net.hederamc.letmehere.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntityMixin {
    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.discard();
    }
}
