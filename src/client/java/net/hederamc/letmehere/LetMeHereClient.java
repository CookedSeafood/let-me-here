package net.hederamc.letmehere;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hederamc.letmehere.network.protocol.common.SitC2SPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class LetMeHereClient implements ClientModInitializer {
    public static final KeyMapping.Category MAIN_CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath(LetMeHere.MOD_NAMESPACE, "main"));
    public static final KeyMapping SIT_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.let_me_here.sit",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Z,
                    MAIN_CATEGORY));
    public static final KeyMapping ALIGN_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.let_me_here.align",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_CONTROL,
                    MAIN_CATEGORY));

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as
        // rendering.

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean sit = false;
            while (SIT_KEY.consumeClick()) {
                sit = true;
            }

            boolean align = false;
            while (ALIGN_KEY.consumeClick()) {
                align = true;
            }

            if (sit) {
                Vec3 pos = client.player.getPos();

                if (align) {
                    ClientPlayNetworking.send(new SitC2SPayload(Math.floor(pos.x) + 0.5, pos.y, Math.floor(pos.z) + 0.5));
                    return;
                }

                ClientPlayNetworking.send(new SitC2SPayload(pos));
            }
        });
    }
}
