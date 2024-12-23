// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.utils.fancy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class TextualComponent implements Cloneable {
    static TextualComponent deserialize(Map<String, Object> map) {
        if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
            return TextualComponent.ArbitraryTextTypeComponent.deserialize(map);
        } else {
            return map.size() >= 2 && map.containsKey("key") && !map.containsKey("value") ? TextualComponent.ComplexTextTypeComponent.deserialize(map) : null;
        }
    }

    static boolean isTextKey(String key) {
        return key.equals("translate") || key.equals("text") || key.equals("score") || key.equals("selector");
    }

    static boolean isTranslatableText(TextualComponent component) {
        return component instanceof TextualComponent.ComplexTextTypeComponent && component.getKey().equals("translate");
    }

    public static TextualComponent rawText(String textValue) {
        return new TextualComponent.ArbitraryTextTypeComponent("text", textValue);
    }

    public static TextualComponent localizedText(String translateKey) {
        return new TextualComponent.ArbitraryTextTypeComponent("translate", translateKey);
    }

    private static void throwUnsupportedSnapshot() {
        throw new UnsupportedOperationException("This feature is only supported in snapshot releases.");
    }

    public static TextualComponent objectiveScore(String scoreboardObjective) {
        return objectiveScore("*", scoreboardObjective);
    }

    public static TextualComponent objectiveScore(String playerName, String scoreboardObjective) {
        throwUnsupportedSnapshot();
        return new TextualComponent.ComplexTextTypeComponent(
                "score",
                ImmutableMap.<String, String>builder()
                        .put("name", playerName)
                        .put("objective", scoreboardObjective)
                        .build()
        );
    }

    public static TextualComponent selector(String selector) {
        throwUnsupportedSnapshot();
        return new TextualComponent.ArbitraryTextTypeComponent("selector", selector);
    }

    public String toString() {
        return this.getReadableString();
    }

    public abstract String getKey();

    public abstract String getReadableString();

    public abstract TextualComponent clone();

    public abstract void writeJson(JsonWriter var1) throws IOException;

    static {
        ConfigurationSerialization.registerClass(TextualComponent.ArbitraryTextTypeComponent.class);
        ConfigurationSerialization.registerClass(TextualComponent.ComplexTextTypeComponent.class);
    }

    private static final class ArbitraryTextTypeComponent extends TextualComponent implements ConfigurationSerializable {
        private String _key;
        private String _value;

        public ArbitraryTextTypeComponent(String key, String value) {
            this.setKey(key);
            this.setValue(value);
        }

        public static TextualComponent.ArbitraryTextTypeComponent deserialize(Map<String, Object> map) {
            return new TextualComponent.ArbitraryTextTypeComponent(map.get("key").toString(), map.get("value").toString());
        }

        public String getKey() {
            return this._key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            this._key = key;
        }

        public String getValue() {
            return this._value;
        }

        public void setValue(String value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            this._value = value;
        }

        public TextualComponent clone() {
            return new TextualComponent.ArbitraryTextTypeComponent(this.getKey(), this.getValue());
        }

        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(this.getKey()).value(this.getValue());
        }

        public Map<String, Object> serialize() {
            return new HashMap<String, Object>() {
                {
                    this.put("key", ArbitraryTextTypeComponent.this.getKey());
                    this.put("value", ArbitraryTextTypeComponent.this.getValue());
                }
            };
        }

        public String getReadableString() {
            return this.getValue();
        }
    }

    private static final class ComplexTextTypeComponent extends TextualComponent implements ConfigurationSerializable {
        private String _key;
        private Map<String, String> _value;

        public ComplexTextTypeComponent(String key, Map<String, String> values) {
            this.setKey(key);
            this.setValue(values);
        }

        public static TextualComponent.ComplexTextTypeComponent deserialize(Map<String, Object> map) {
            String key = null;
            Map<String, String> value = new HashMap<>();

            for(Entry<String, Object> valEntry : map.entrySet()) {
                if (valEntry.getKey().equals("key")) {
                    key = (String)valEntry.getValue();
                } else if (valEntry.getKey().startsWith("value.")) {
                    value.put(valEntry.getKey().substring(6), valEntry.getValue().toString());
                }
            }

            return new TextualComponent.ComplexTextTypeComponent(key, value);
        }

        public String getKey() {
            return this._key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            this._key = key;
        }

        public Map<String, String> getValue() {
            return this._value;
        }

        public void setValue(Map<String, String> value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            this._value = value;
        }

        public TextualComponent clone() {
            return new TextualComponent.ComplexTextTypeComponent(this.getKey(), this.getValue());
        }

        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(this.getKey());
            writer.beginObject();

            for(Entry<String, String> jsonPair : this._value.entrySet()) {
                writer.name(jsonPair.getKey()).value(jsonPair.getValue());
            }

            writer.endObject();
        }

        public Map<String, Object> serialize() {
            return new HashMap<String, Object>() {
                {
                    this.put("key", ComplexTextTypeComponent.this.getKey());

                    for(Entry<String, String> valEntry : ComplexTextTypeComponent.this.getValue().entrySet()) {
                        this.put("value." + valEntry.getKey(), valEntry.getValue());
                    }

                }
            };
        }

        public String getReadableString() {
            return this.getKey();
        }
    }
}