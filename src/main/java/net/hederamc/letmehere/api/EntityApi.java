package net.hederamc.letmehere.api;

import net.minecraft.world.entity.Entity;

public interface EntityApi {
    default void removePassenger(Entity passenger) {
        throw new UnsupportedOperationException();
    }

    default boolean isRiding() {
        throw new UnsupportedOperationException();
    }
}
