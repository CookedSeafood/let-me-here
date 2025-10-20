package net.cookedseafood.letmehere.api;

import net.minecraft.util.math.Vec3d;

public interface EntityApi {
    default void sit(Vec3d pos) {
    }

    default void sit(double x, double y, double z) {
    }
}
