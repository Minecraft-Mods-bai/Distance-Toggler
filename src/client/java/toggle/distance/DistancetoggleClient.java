package toggle.distance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DistancetoggleClient implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private int oldViewDistance = -1;
    private int oldSimulationDistance = -1;
    public static int maxDistance = 32;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.distancetoggle",
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
            client.options.getViewDistance().setValue(maxDistance);
            client.options.getSimulationDistance().setValue(maxDistance);

            client.options.write();
            client.worldRenderer.reload();
            client.inGameHud.setOverlayMessage(Text.translatable("distancetoggle.changed", maxDistance), false);
        } else {
            client.options.getViewDistance().setValue(oldViewDistance);
            client.options.getSimulationDistance().setValue(oldSimulationDistance);
            client.options.write();
            client.worldRenderer.reload();
            client.inGameHud.setOverlayMessage(Text.translatable("distancetoggle.changed_again", oldViewDistance), false);
            oldViewDistance = -1;
            oldSimulationDistance = -1;

        }
    }
}