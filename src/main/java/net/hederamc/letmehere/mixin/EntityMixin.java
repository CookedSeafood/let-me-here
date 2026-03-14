package net.hederamc.letmehere.mixin;

import net.hederamc.letmehere.api.EntityApi;
import net.hederamc.letmehere.api.Sitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements Sitter, EntityApi {
    @Shadow private Level level;
    @Shadow private float xRot;

    @Override
    public void sit(double x, double y, double z) {
        if (this.level.isClientSide() || this.isRiding()) {
            return;
        }

        BlockPos blockPos = new BlockPos((int) x, (int) Math.floor(y - 1.0e-5f), (int) z);
        if (this.level.getBlockState(blockPos)
                .getCollisionShape(this.level, blockPos, CollisionContext.of(((Entity) (Object) this)))
                .isEmpty()) {
            return;
        }

        ArmorStand armorStand = EntityType.ARMOR_STAND.create(this.level, EntitySpawnReason.COMMAND);
        if (armorStand == null) {
            return;
        }

        armorStand.setPos(x, y, z);
        armorStand.setXRot(xRot);
        armorStand.setMarker(true);
        armorStand.setInvisible(true);
        this.level.addFreshEntity(armorStand);
        this.startRiding(armorStand);
    }

    @Shadow
    public abstract void discard();

    @Shadow
    public abstract boolean startRiding(Entity entity);

    @Shadow
    public abstract void stopRiding();
}
