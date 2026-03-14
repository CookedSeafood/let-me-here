package net.hederamc.letmehere.api;

import net.hederamc.fishbonetrehalose.api.PosHolder;
import net.minecraft.world.phys.Vec3;

public interface Sitter extends PosHolder {
    default void sit() {
        this.sit(this.getPos());
    }

    default void sit(Vec3 pos) {
        this.sit(pos.x, pos.y, pos.z);
    }

    default void sit(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }
}
