// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.reward;

import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.CratePlaceholder;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.reward.menus.RewardEditMenu;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Iterator;
import java.util.Map;

public class RewardsListeners implements Listener {
    public RewardsListeners(Crates plugin, boolean isValid) {
        if (isValid) {
            if (plugin.isServerLoaded()) {
                Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            }

        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().contains(CC.translate("&eEdit Rewards of "))) {
            String name = event.getView().getTitle().replace(CC.translate("&eEdit Rewards of "), "");
            Crate crate = Crate.getByName(name);
            if (crate == null) {
                return;
            }

            Iterator<CratePlaceholder> iterator = crate.getPlaceholders().iterator();

            while(iterator.hasNext()) {
                CratePlaceholder placeholder = iterator.next();
                if (placeholder.getItem() == null) {
                    iterator.remove();
                } else {
                    inventory.setItem(placeholder.getSlot(), placeholder.getEditPreview());
                }
            }

            crate.getRewards().forEach((reward) -> inventory.setItem(reward.getSlot(), reward.getPreviewItem()));
            if (crate instanceof MysteryBox) {
                MysteryBox box = (MysteryBox)crate;
                box.getObligatoryRewards().forEach((reward) -> inventory.setItem(reward.getSlot(), reward.getPreviewObligatoryItem()));
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (event.getClickedInventory() != null) {
            if (event.getView().getTitle().contains(CC.translate("&eEdit Rewards of "))) {
                String name = event.getView().getTitle().replace(CC.translate("&eEdit Rewards of "), "");
                Crate crate = Crate.getByName(name);
                if (crate == null) {
                    return;
                }

                switch(event.getClick()) {
                    case RIGHT:
                        this.handleRightClick(inventory, event, player, crate);
                        break;
                    case DROP:
                    case CONTROL_DROP:
                        this.handleDropClick(inventory, event, player, crate);
                        break;
                    case SHIFT_LEFT:
                        this.handleShiftLeftClick(inventory, event, player, crate);
                        break;
                    case LEFT:
                        this.handleLeftClick(inventory, event, player, crate);
                }
            }

        }
    }

    private void handleRightClick(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        if (event.getClickedInventory() != player.getInventory()) {
            if (crate instanceof MysteryBox) {
                event.setCancelled(true);
                MysteryBox box = (MysteryBox)crate;
                if (box.getObligatoryReward(event.getSlot()) != null) {
                    player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));
                    (new RewardEditMenu(box.getObligatoryReward(event.getSlot()), crate)).openMenu(player);
                    return;
                }
            }

            if (crate.getReward(event.getSlot()) != null) {
                event.setCancelled(true);
                player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));
                (new RewardEditMenu(crate.getReward(event.getSlot()), crate)).openMenu(player);
            } else if (crate.getPlaceholder(event.getSlot()) != null) {
                event.setCancelled(true);
                CratePlaceholder placeholder = crate.getPlaceholder(event.getSlot());
                Reward newReward = new Reward(placeholder.getItem().clone(), 100.0D, placeholder.getSlot(), RewardType.ITEMS, null, false);
                crate.getPlaceholders().remove(placeholder);
                crate.getRewards().add(newReward);
                player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));
                (new RewardEditMenu(newReward, crate)).openMenu(player);
            } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                this.createPlaceholder(player, event, inventory, crate, event.getCursor().clone(), event.getSlot());
            }
        }
    }

    private void handleDropClick(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        this.handleCheckRemove(event, crate);
    }

    private void handleLeftClick(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        if (event.getClickedInventory() != player.getInventory()) {
            if (event.getInventory().getItem(event.getSlot()) == null) {
                this.handleLeftClickNewPlaceholder(inventory, event, player, crate);
            } else {
                this.handleLeftClickRemove(inventory, event, player, crate);
            }

        }
    }

    private void handleLeftClickRemove(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        this.handleCheckRemove(event, crate);
    }

    private void handleLeftClickNewPlaceholder(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
            if (ItemUtils.isReward(event.getCursor())) {
                Reward reward = ItemUtils.getRewardByItem(event.getCursor().clone(), event.getSlot());
                if (reward != null) {
                    if (reward.isObligatory() && crate instanceof MysteryBox) {
                        MysteryBox box = (MysteryBox)crate;
                        box.getObligatoryRewards().add(reward);
                        return;
                    }

                    crate.getRewards().add(reward);
                    return;
                }
            }

            this.createPlaceholder(player, event, inventory, crate, event.getCursor().clone(), event.getSlot());
        }
    }

    private void handleShiftLeftClick(Inventory inventory, InventoryClickEvent event, Player player, Crate crate) {
        if (event.getClickedInventory() == player.getInventory()) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                if (ItemUtils.isReward(event.getCursor())) {
                    Reward reward = ItemUtils.getRewardByItem(event.getCurrentItem().clone(), inventory.firstEmpty());
                    if (reward != null) {
                        if (reward.isObligatory() && crate instanceof MysteryBox) {
                            MysteryBox box = (MysteryBox)crate;
                            box.getObligatoryRewards().add(reward);
                            return;
                        }

                        crate.getRewards().add(reward);
                        return;
                    }
                }

                this.createPlaceholder(player, event, inventory, crate, event.getCurrentItem().clone(), inventory.firstEmpty());
            }
        } else {
            this.handleCheckRemove(event, crate);
        }
    }

    private void handleCheckRemove(InventoryClickEvent event, Crate crate) {
        if (crate.getPlaceholder(event.getSlot()) != null) {
            crate.getPlaceholders().remove(crate.getPlaceholder(event.getSlot()));
        }

        if (crate.getReward(event.getSlot()) != null) {
            crate.getRewards().remove(crate.getReward(event.getSlot()));
        }

        if (crate instanceof MysteryBox) {
            MysteryBox box = (MysteryBox)crate;
            if (box.getObligatoryReward(event.getSlot()) != null) {
                box.getObligatoryRewards().remove(box.getObligatoryReward(event.getSlot()));
            }
        }

    }

    @EventHandler
    public void onDragItems(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().contains(CC.translate("&eEdit Rewards of "))) {
            if (inventory == player.getInventory()) {
                return;
            }

            String name = event.getView().getTitle().replace(CC.translate("&eEdit Rewards of "), "");
            Crate crate = Crate.getByName(name);
            if (crate == null) {
                return;
            }

            Map<Integer, ItemStack> items = event.getNewItems();
            items.forEach((slot, stack) -> {
                if (stack != null && stack.getType() != Material.AIR && slot <= inventory.getSize()) {
                    this.createPlaceholderDrag(player, event, inventory, crate, stack, slot);
                }
            });
        }

    }

    private void createPlaceholderDrag(Player player, InventoryDragEvent event, Inventory inventory, Crate crate, ItemStack stack, int slot) {
        ItemStack finalItem = stack.clone();
        if (event.getType() == DragType.SINGLE) {
            finalItem.setAmount(1);
        }

        this.createPlaceholder(player, null, inventory, crate, stack, slot);
    }

    private void createPlaceholder(Player player, InventoryClickEvent event, Inventory inventory, Crate crate, ItemStack stack, int slot) {
        ItemStack finalItem = stack.clone();
        if (event != null && event.getClick().isRightClick()) {
            finalItem.setAmount(1);
        }

        CratePlaceholder placeholder = new CratePlaceholder(finalItem, slot);
        crate.getPlaceholders().add(placeholder);
    }
}