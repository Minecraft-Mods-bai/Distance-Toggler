package toggle.distance;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.of("Distance Toggle Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new SliderWidget(
                this.width / 2 - 100, this.height / 2 - 25, 200, 20,
                getSliderText(DistancetoggleClient.maxDistance),
                (DistancetoggleClient.maxDistance - 2) / 30.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(getSliderText(getValueAsDistance()));
            }

            @Override
            protected void applyValue() {
                DistancetoggleClient.maxDistance = getValueAsDistance();
            }

            private int getValueAsDistance() {
                return 2 + (int)Math.round(this.value * 30); // 2–32
            }
        });

        // Done butonu
        this.addDrawableChild(ButtonWidget.builder(Text.of("Done"), b -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height / 2 + 10, 200, 20).build());
    }

    private Text getSliderText(int value) {
        return Text.of("Max Distance: " + value);
    }
}
