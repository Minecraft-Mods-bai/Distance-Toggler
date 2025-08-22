package toggle.distance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DistanceConfig {
    public int maxDistance = 32;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/distancetoggle.json");

    private static DistanceConfig instance;

    public static DistanceConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static DistanceConfig load() {
        try {
            if (CONFIG_FILE.exists()) {
                return GSON.fromJson(new FileReader(CONFIG_FILE), DistanceConfig.class);
            }
        } catch (IOException e) {
            System.err.println(" " + e);
        }
        return new DistanceConfig();
    }

    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println(" " + e);
        }
    }
}
