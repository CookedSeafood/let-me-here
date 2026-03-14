package net.hederamc.letmehere;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hederamc.letmehere.command.SitCommand;
import net.hederamc.letmehere.network.protocol.common.SitC2SPayload;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LetMeHere implements ModInitializer {
    public static final String MOD_ID = "let-me-here";
    public static final String MOD_NAMESPACE = "let_me_here";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        PayloadTypeRegistry.serverboundPlay().register(SitC2SPayload.ID, SitC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SitC2SPayload.ID, (payload, context) -> {
            ServerPlayer player = context.player();
            if (player == null) {
                return;
            }

            player.sit(payload.pos());
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SitCommand.register(dispatcher, registryAccess));
    }
}
