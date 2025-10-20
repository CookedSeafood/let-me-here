package net.cookedseafood.letmehere.mixin;

import net.cookedseafood.letmehere.api.EntityApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityApi {
    @Shadow
    private World world;
    @Shadow
    private float yaw;

    @Override
    public void sit(Vec3d pos) {
        sit(pos.x, pos.y, pos.z);
    }

    @Override
    public void sit(double x, double y, double z) {
        if (world instanceof ServerWorld serverWorld) {
            ArmorStandEntity armorStand = EntityType.ARMOR_STAND.create(serverWorld, SpawnReason.COMMAND);
            if (armorStand != null) {
                armorStand.refreshPositionAndAngles(x, y, z, yaw, 0.0F);
                armorStand.setMarker(true);
                armorStand.setInvisible(true);
                serverWorld.spawnEntity(armorStand);
                this.startRiding(armorStand);
            }
        }
    }

    @Inject(
        method = "Lnet/minecraft/entity/Entity;removePassenger(Lnet/minecraft/entity/Entity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V",
            shift = At.Shift.AFTER
        )
    )
    private void discardVehicleArmorStand(Entity passenger, CallbackInfo ci) {
        if (((Entity)(Object)this) instanceof ArmorStandEntity armorStand) {
            armorStand.discard();
        }
    }

    @Shadow
    public abstract boolean startRiding(Entity entity);
}
