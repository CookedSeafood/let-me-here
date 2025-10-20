package net.cookedseafood.letmehere;

import org.lwjgl.glfw.GLFW;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class LetMeHereClient implements ClientModInitializer {
    private static KeyBinding sitKey;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        sitKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.letmehere.sit",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.letmehere.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean sitKeyPressed = false;
            while (sitKey.wasPressed()) {
                sitKeyPressed = true;
            }

            if (sitKeyPressed) {
                client.player.networkHandler.sendChatCommand("sit");
            }
        });
    }
}
