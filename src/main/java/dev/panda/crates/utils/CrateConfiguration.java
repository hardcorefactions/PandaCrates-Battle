// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.gson.Serializer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

@Getter
public class CrateConfiguration {
    private final JavaPlugin plugin;
    private final String name;
    private final File file;

    @SneakyThrows
    public CrateConfiguration(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder(), name);
        if (!this.file.exists()) {
            this.file.createNewFile();
        }

    }

    public JsonArray load() {
        try {
            BufferedReader reader = Files.newBufferedReader(this.file.toPath(), StandardCharsets.UTF_8);
            return (new JsonParser()).parse(reader).getAsJsonArray();
        } catch (IllegalStateException var2) {
            return null;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public void save(Collection<Crate> crates) {
        JsonArray array = new JsonArray();
        crates.forEach((crate) -> {
            JsonElement object = (new JsonParser()).parse(crate.toDocument().toJson());
            array.add(object);
        });
        BufferedWriter writer = Files.newBufferedWriter(this.file.toPath(), StandardCharsets.UTF_8);

        try {
            writer.write(Serializer.GSON.toJson(array));
            writer.flush();
        } catch (Throwable var7) {
            try {
                writer.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        writer.close();
    }

}