/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package dev.panda.crates.crate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import dev.panda.crates.Crates;
import dev.panda.crates.airdrop.Airdrop;
import dev.panda.crates.crate.particle.ParticleEffect;
import dev.panda.crates.crate.particle.ParticleShape;
import dev.panda.crates.hologram.Hologram;
import dev.panda.crates.loader.CratesLoader;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.RewardType;
import dev.panda.crates.supplydrop.SupplyDrop;
import dev.panda.crates.utils.*;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.compatibility.particle.ParticleType;
import dev.panda.crates.utils.gson.Serializer;
import dev.panda.crates.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
public class Crate {
    private static final CrateConfiguration configuration = new CrateConfiguration(Crates.getInstance(), "crates.json");
    @Getter
    private static final Map<String, Crate> crates = Maps.newHashMap();
    public static CratesLoader loader;
    private final String name;
    @Setter
    private boolean enable;
    @Setter
    private List<Reward> rewards = Lists.newArrayList();
    @Setter
    private List<CratePlaceholder> placeholders = new CopyOnWriteArrayList<>();
    @Setter
    private String displayName;
    @Setter
    private ItemStack key;
    @Setter
    private Sound openSound;
    @Setter
    private int minimumReward = 1;
    @Setter
    private int maximumReward = 2;
    @Setter
    private Location chestLocation;
    @Setter
    private boolean virtual = false;
    @Setter
    private List<String> hologramLines;
    @Setter
    private List<String> keyLoreLines;
    private final Map<Location, Hologram> holograms = Maps.newHashMap();
    @Setter
    private ParticleEffect effect;
    @Setter
    private ItemStack hologramItem;
    private final Inventory inventory;

    public Crate(String name) {
        this.name = name;
        this.enable = true;
        this.displayName = CC.BLUE + name;
        this.key = new ItemBuilder(Material.TRIPWIRE_HOOK).name(CC.translate(this.displayName + " Key")).build();
        this.inventory = Bukkit.createInventory(null, 54, CC.translate("&eEdit Rewards of " + name));
        if (!name.equalsIgnoreCase("Airdrop")) {
            crates.put(name, this);
        }
        this.keyLoreLines = loader.getDefaultKeyLoreLines(this);
        this.hologramLines = loader.getDefaultHologramLines(this);
    }

    public static void saveAll() {
        configuration.save(crates.values());
    }

    public void destroyHolograms() {
        this.holograms.values().forEach(Hologram::remove);
        this.holograms.clear();
    }

    public List<String> getHologramsLines() {
        return this.hologramLines;
    }

    public void sendHolograms() {
        if (this.chestLocation != null) {
            double y = this.isHologramItemNull() ? 0.6D : (this.isHologramItemHumanSkull() ? 0.9D : 0.85D);
            if (this.chestLocation.getBlock().getType() == Material.BEACON || this.chestLocation.getBlock().getType() == Material.ENDER_CHEST || this.chestLocation.getBlock().getType() == CompatibleMaterial.ENDER_PORTAL.getMaterial() || this.chestLocation.getBlock().getType() == Material.DROPPER) {
                y = 0.9D;
            }

            Location finalLocation = this.chestLocation.clone().add(0.5D, y, 0.5D);
            Hologram hologram = new Hologram(Crates.getInstance(), loader, finalLocation, this);
            Bukkit.getOnlinePlayers().forEach(hologram::spawn);
            this.holograms.put(finalLocation, hologram);
        }
    }

    public boolean isHologramItemHumanSkull() {
        return this.hologramItem != null && this.hologramItem.getType() == CompatibleMaterial.HUMAN_SKULL.getMaterial();
    }

    public void createHologram(Location location) {
        double y = this.isHologramItemNull() ? 0.6 : (this.isHologramItemHumanSkull() ? 0.9 : 0.85);
        Location finalLocation = location.clone().add(0.5, y, 0.5);
        Hologram hologram = new Hologram(Crates.getInstance(), loader, finalLocation, this);
        Bukkit.getOnlinePlayers().forEach(hologram::spawn);
        this.holograms.put(finalLocation, hologram);
    }

    public static void load(CratesLoader loader) {
        if (loader.getPlugin().isServerLoaded()) {
            Crate.loader = loader;
            JsonArray array = configuration.load();
            if (array == null) {
                return;
            }
            array.forEach(element -> {
                Document document;
                try {
                    document = Document.parse(element.getAsJsonObject().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                Crate crate = document.containsKey("type") ? (document.getString("type").equalsIgnoreCase("supplydrop") ? new SupplyDrop() : (document.getString("type").equalsIgnoreCase("mysterybox") ? new MysteryBox(document.getString("name")) : (document.getString("type").equalsIgnoreCase("airdrop") ? new Airdrop() : new Crate(document.getString("name"))))) : new Crate(document.getString("name"));
                crate.setEnable(document.getBoolean("enable"));
                crate.setDisplayName(document.getString("displayName"));
                crate.setKey(Serializer.GSON.fromJson(document.getString("key"), ItemStack.class));
                crate.setVirtual(document.getBoolean("virtual", true));
                crate.setHologramLines(document.getList("hologramLines", String.class, loader.getDefaultHologramLines(crate)).stream().map(CC::translate).collect(Collectors.toList()));
                crate.setKeyLoreLines(document.getList("keyLoreLines", String.class, loader.getDefaultKeyLoreLines(crate)).stream().map(CC::translate).collect(Collectors.toList()));
                if (document.containsKey("hologramItem")) {
                    try {
                        crate.setHologramItem(Serializer.GSON.fromJson(document.getString("hologramItem"), ItemStack.class));
                    } catch (Exception e) {
                        System.out.println("Loading Hologram item:" + e.getLocalizedMessage());
                    }
                }
                if (document.containsKey("openSound")) {
                    crate.setOpenSound(Sound.valueOf(document.getString("openSound")));
                }
                if (document.containsKey("effect")) {
                    String[] dataSplit = document.getString("effect").split(";");
                    crate.setEffect(new ParticleEffect(ParticleType.of(dataSplit[0]), ParticleShape.valueOf(dataSplit[1])));
                }
                crate.setMinimumReward(document.getInteger("minimumReward"));
                crate.setMaximumReward(document.getInteger("maximumReward"));
                for (Document document2 : document.getList("rewards", Document.class)) {
                    crate.getRewards().add(new Reward(document2));
                }
                for (Document document3 : document.getList("placeholders", Document.class)) {
                    crate.getPlaceholders().add(new CratePlaceholder(document3));
                }
                if (crate instanceof MysteryBox) {
                    for (Document document4 : document.getList("obligatoryrewards", Document.class)) {
                        ((MysteryBox)crate).getObligatoryRewards().add(new Reward(document4));
                    }
                }
                if (document.containsKey("chestLocations")) {
                    for (String string : document.getList("chestLocations", String.class)) {
                        crate.setChestLocation(LocationUtil.convertLocation(string));
                    }
                }
                if (document.containsKey("chestLocation")) {
                    crate.setChestLocation(LocationUtil.convertLocation(document.getString("chestLocation")));
                }
                for (CratePlaceholder cratePlaceholder : crate.getPlaceholders()) {
                    crate.getInventory().setItem(cratePlaceholder.getSlot(), cratePlaceholder.getEditPreview());
                }
                for (Reward reward : crate.getRewards()) {
                    crate.getInventory().setItem(reward.getSlot(), reward.getItem());
                }
                if (crate instanceof MysteryBox) {
                    for (Reward reward : ((MysteryBox)crate).getObligatoryRewards()) {
                        crate.getInventory().setItem(reward.getSlot(), reward.getItem());
                    }
                }
                if (crate.getChestLocation() != null && crate.getEffect() != null) {
                    crate.getEffect().play(crate.getChestLocation());
                }
                crate.sendHolograms();
            });
        }
    }

    public void save() {
        configuration.save(crates.values());
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("name", this.name);
        document.append("enable", this.enable);
        document.append("displayName", this.displayName);
        document.append("key", Serializer.GSON.toJson(this.key));
        if (this.openSound != null) {
            document.append("openSound", this.openSound.name());
        }
        document.append("minimumReward", this.minimumReward);
        document.append("maximumReward", this.maximumReward);
        document.append("rewards", this.rewards.stream().map(Reward::serialize).collect(Collectors.toList()));
        document.append("placeholders", this.placeholders.stream().filter(placeholder -> placeholder.getItem() != null).map(CratePlaceholder::serialize).collect(Collectors.toList()));
        document.append("virtual", this.virtual);
        document.append("hologramLines", this.hologramLines.stream().map(line -> line.replace("§", "&")).collect(Collectors.toList()));
        document.append("keyLoreLines", this.keyLoreLines.stream().map(line -> line.replace("§", "&")).collect(Collectors.toList()));
        if (this.effect != null) {
            String data = this.effect.getParticle().getName() + ";" + this.effect.getShape().name();
            document.append("effect", data);
        }
        if (this.hologramItem != null) {
            document.append("hologramItem", Serializer.GSON.toJson(this.hologramItem));
        }
        if (this.chestLocation != null) {
            document.append("chestLocation", LocationUtil.parseLocation(this.chestLocation));
        }
        if (this instanceof MysteryBox) {
            MysteryBox mysteryBox = (MysteryBox)this;
            document.append("type", "mysteryBox");
            document.append("obligatoryrewards", mysteryBox.getObligatoryRewards().stream().map(Reward::serialize).collect(Collectors.toList()));
        } else if (this instanceof Airdrop) {
            document.append("type", "airdrop");
        } else if (this instanceof SupplyDrop) {
            document.append("type", "supplydrop");
        } else {
            document.append("type", "crate");
        }
        return document;
    }

    public static Crate getByLocation(Location location) {
        for (Crate crate : crates.values()) {
            if (crate.getChestLocation() == null || !crate.getChestLocation().equals(location)) continue;
            return crate;
        }
        return null;
    }

    public static Crate getByBlock(Block block) {
        if (block == null) {
            return null;
        }
        Location location = block.getLocation();
        for (Crate crate : crates.values()) {
            if (crate.getChestLocation() == null || !crate.getChestLocation().equals(location)) continue;
            return crate;
        }
        return null;
    }

    public static Crate getByName(String name) {
        return crates.get(name);
    }

    public static Crate getByNameIgnoreCase(String name) {
        for (Map.Entry<String, Crate> entry : crates.entrySet()) {
            String key = entry.getKey();
            if (!key.equalsIgnoreCase(name)) continue;
            return entry.getValue();
        }
        return null;
    }

    public ItemStack generateKey() {
        return new ItemBuilder(this.key.clone()).name(CC.translate(this.displayName + " Key")).lore(this.keyLoreLines.stream().map(line -> line.replace("{display}", this.displayName).replace("{name}", this.name)).collect(Collectors.toList())).build();
    }

    public static Crate getCrateByKey(ItemStack key) {
        String name;
        if (key == null) {
            return null;
        }
        if (key.getItemMeta() == null) {
            return null;
        }
        ItemMeta meta = key.getItemMeta();
        if (meta.hasDisplayName() && meta.hasLore() && (name = meta.getDisplayName()).contains("Key")) {
            String finalName = name.replace(" Key", "");
            return crates.values().stream().filter(crate -> crate.getDisplayName().equalsIgnoreCase(finalName)).findFirst().orElse(null);
        }
        return null;
    }

    public void openCrate(Player player) {
        if (!this.enable) {
            player.sendMessage(CC.translate("&cThis crate is currently disabled"));
            return;
        }
        if (this.getMaximumReward() == 0 || this.getRewards().isEmpty()) {
            player.sendMessage(CC.translate("&cCrate " + this.getName() + " is empty, please contact an admin."));
            return;
        }
        if (player.getInventory().firstEmpty() < 0) {
            player.sendMessage(CC.translate("&cInventory Full."));
            return;
        }
        int random = new Random().nextInt(this.getMaximumReward() - this.getMinimumReward() + 1) + this.getMinimumReward();
        ArrayList<Reward> randomRewards = new ArrayList<>();
        ArrayList<Reward> rewardList = new ArrayList<>(this.getRewards());
        Collections.shuffle(rewardList);
        for (int i = 0; i < random; ++i) {
            randomRewards.add(RandomUtils.getRandomReward(rewardList));
        }
        for (Reward reward : randomRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();
                for (String command : commands) {
                    if (command.contains("op")) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                }
            }
            if (reward.getBroadcast().isEmpty()) continue;
            reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName()).replace("{display}", this.getDisplayName()).replace("{reward}", reward.getItemName())));
        }
        if (randomRewards.isEmpty()) {
            Reward reward = this.getRewards().get(new Random().nextInt(this.getRewards().size()));
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
            if (!reward.getBroadcast().isEmpty()) {
                reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
        }
        if (this.openSound != null) {
            player.playSound(player.getLocation(), this.openSound, 1.0f, 1.0f);
        }
        player.sendMessage(CC.translate("&aYou have received " + randomRewards.size() + " reward(s)"));
        this.consumeKey(player);
        player.updateInventory();
    }

    public void openEditRewardsInventory(Player player) {
        player.openInventory(this.inventory);
    }

    public void consumeKey(Player player) {
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }

    public Reward getReward(int slot) {
        return this.rewards.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }

    public CratePlaceholder getPlaceholder(int slot) {
        return this.placeholders.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }

    public void delete() {
        if (this.effect != null) {
            this.effect.stop();
        }
        crates.remove(this.name);
        this.destroyHolograms();
        this.save();
    }

    public void removeHologram() {
        this.holograms.forEach((loc, hologram) -> Bukkit.getOnlinePlayers().forEach(player -> hologram.remove(player)));
    }

    public void respawnHolograms() {
        this.destroyHolograms();
        this.sendHolograms();
    }

    public boolean isHologramItemNull() {
        return this.hologramItem == null || this.hologramItem.getType() == Material.AIR;
    }

    public void handleSetChest(Player player, Location location) {
        Crate other = Crate.getByLocation(location);
        if (other != null) {
            player.sendMessage(CC.RED + "There is another crate.");
            return;
        }
        if (this.getChestLocation() != null && this.getChestLocation().equals(location)) {
            player.sendMessage(CC.translate("&cThere is another crate."));
            return;
        }
        if (this.effect != null) {
            this.effect.stop();
            this.effect.safePlay(location);
        }
        this.destroyHolograms();
        this.createHologram(ServerUtil.SERVER_VERSION_INT == 7 ? location.clone().add(0.0, 0.5, 0.0) : location);
        this.setChestLocation(location);
        player.sendMessage(CC.translate("&aSuccessfully set chest location."));
    }

}

