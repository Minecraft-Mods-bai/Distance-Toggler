package toggle.chunk;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ChunktoggleClient implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private int oldDistance = -1;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chunktoggle.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                toggleDistance(client);
            }
        });
    }

    private void toggleDistance(MinecraftClient client) {
        int current = client.options.getViewDistance().getValue();


        if (oldDistance == -1) {
            oldDistance = current;
            client.options.getViewDistance().setValue(32);
            client.options.write();
            client.worldRenderer.reload();
            if (client.player != null) {
                client.inGameHud.setOverlayMessage(Text.literal("Render is now 32"), false);
            }

        } else {
            client.options.getViewDistance().setValue(oldDistance);
            client.options.write();
            client.worldRenderer.reload();
            if (client.player != null) {
                client.inGameHud.setOverlayMessage(Text.literal("Render is now again: " + oldDistance), false);
            }
            oldDistance = -1;

        }
    }
}
