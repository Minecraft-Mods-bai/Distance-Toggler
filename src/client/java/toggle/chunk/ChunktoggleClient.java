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
    private int oldViewDistance = -1;
    private int oldSimulationDistance = -1;
    private static final int MAX_DISTANCE = 32;

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
        int currentViewDistance = client.options.getViewDistance().getValue();
        int currentSimulationDistance = client.options.getSimulationDistance().getValue();

        if (oldViewDistance == -1 && oldSimulationDistance == -1) {
            oldViewDistance = currentViewDistance;
            oldSimulationDistance = currentSimulationDistance;
            client.options.getViewDistance().setValue(MAX_DISTANCE);
            client.options.getSimulationDistance().setValue(MAX_DISTANCE);

            client.options.write();
            client.worldRenderer.reload();
            if (client.player != null) {
                client.inGameHud.setOverlayMessage(Text.literal("Distance is now " + MAX_DISTANCE), false);
            }

        } else {
            client.options.getViewDistance().setValue(oldViewDistance);
            client.options.getSimulationDistance().setValue(oldSimulationDistance);
            client.options.write();
            client.worldRenderer.reload();
            if (client.player != null) {
                client.inGameHud.setOverlayMessage(Text.literal("Distance is now again: " + oldViewDistance), false);
            }
            oldViewDistance = -1;
            oldSimulationDistance = -1;

        }
    }
}
