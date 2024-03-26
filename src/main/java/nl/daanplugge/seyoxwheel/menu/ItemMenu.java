package nl.daanplugge.seyoxwheel.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.util.ItemNbt;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.daanplugge.seyoxwheel.WheelPlugin;
import nl.daanplugge.seyoxwheel.database.Database;
import nl.daanplugge.seyoxwheel.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMenu {

    private final Database database = WheelPlugin.getInstance().getDatabase();
    private final PaginatedGui gui;

    public ItemMenu(Player player) {
        gui = Gui.paginated()
                .title(MiniMessage.miniMessage().deserialize("<gold>Bewerk de wheelcoin items."))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        WheelPlugin.getInstance().getWheelcoinItems().forEach((id, item) -> {
            GuiItem guiItem = ItemBuilder.from(item).setNbt("seyoxwheel_database_id", String.valueOf(id)).asGuiItem();
            guiItem.setAction(this::handleRemove);
            gui.addItem(guiItem);
        });

        gui.setItem(6, 3, ItemBuilder.from(Material.ARROW).name(ChatUtils.parse("<yellow>Vorige Pagina")).asGuiItem(e -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.ARROW).name(ChatUtils.parse("<yellow>Volgende Pagina")).asGuiItem(e -> gui.next()));

        gui.setDefaultClickAction((event) -> {
            event.setCancelled(true);
            if (event.getClickedInventory() == gui.getInventory()) return;
            ItemStack item = event.getCurrentItem();

            if (item == null || item.getType().equals(Material.AIR)) {
                player.sendMessage(ChatUtils.parse("<red>Selecteer een geldig item in je inventory."));
                return;
            }

            registerItem(item);
        });


        gui.open(player);
    }

    /**
     * Removes triumphgui button identifier.
     **/
    public ItemStack stripTriumphInfo(ItemStack item) {
        ItemStack clone = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(new NamespacedKey("seyoxwheel", "mf-gui"));
        clone.setItemMeta(meta);
        return clone;
    }

    /**
     * Handles removing an item in the gui
     **/
    private void handleRemove(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        int tableId = Integer.parseInt(ItemNbt.getString(item, "seyoxwheel_database_id"));
        gui.removePageItem(item);
        WheelPlugin.getInstance().getWheelcoinItems().remove(tableId);
        database.removeItem(tableId);
    }

    /**
     * Handles adding an item in the gui
     */
    private void registerItem(ItemStack item) {
        database.insertItem(stripTriumphInfo(item)).thenAccept((id) -> {
            WheelPlugin.getInstance().getWheelcoinItems().put(id, stripTriumphInfo(item));
            GuiItem guiItem = ItemBuilder.from(stripTriumphInfo(item)).setNbt("seyoxwheel_database_id", String.valueOf(id)).asGuiItem();
            guiItem.setAction(this::handleRemove);
            gui.addItem(guiItem);
            gui.update();
        });
    }

}
