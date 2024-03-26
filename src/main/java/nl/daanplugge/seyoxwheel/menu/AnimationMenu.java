package nl.daanplugge.seyoxwheel.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import nl.daanplugge.seyoxwheel.WheelPlugin;
import nl.daanplugge.seyoxwheel.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnimationMenu extends BukkitRunnable {

    private final Player player;
    private final Gui gui;
    private final List<ItemStack> selectedItems;
    int timer = 0;

    float pitch = 1f;

    public AnimationMenu(Player player) {
        this.player = player;
        gui = Gui.gui()
                .title(ChatUtils.parse("<gold>Aan het spinnen!"))
                .rows(5)
                .disableAllInteractions()
                .create();

        selectedItems = new ArrayList<>();
        List<ItemStack> stacks = new ArrayList<>(WheelPlugin.getInstance().getWheelcoinItems()
                .values()
                .stream()
                .toList());

        for (int i = 0; i <= 7; i++) {
            Collections.shuffle(stacks);
            ItemStack item = stacks.get(0);
            selectedItems.add(item);
            stacks.remove(item);
        }

        populateMenu();
        gui.open(player);
    }

    @Override
    public void run() {
        timer++;
        Collections.rotate(selectedItems, 1);

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, pitch);

        pitch += 0.1f;
        if (pitch >= 2f) pitch = 2f;

        gui.setItem(3, 5, ItemBuilder.from(Material.NETHER_STAR).name(ChatUtils.parse("<gradient:gold:yellow>/\\ Winnend item /\\")).asGuiItem());

        gui.setItem(2, 4, new GuiItem(selectedItems.get(0)));
        gui.setItem(2, 5, new GuiItem(selectedItems.get(1)));
        gui.setItem(2, 6, new GuiItem(selectedItems.get(2)));

        gui.setItem(3, 4, new GuiItem(selectedItems.get(7)));
        gui.setItem(3, 6, new GuiItem(selectedItems.get(3)));

        gui.setItem(4, 4, new GuiItem(selectedItems.get(6)));
        gui.setItem(4, 5, new GuiItem(selectedItems.get(5)));
        gui.setItem(4, 6, new GuiItem(selectedItems.get(4)));

        gui.update();
        gui.open(player);

        if (timer >= 35) {
            this.cancel();
            populateMenu();

            ItemStack wonItem = selectedItems.get(1);
            player.getInventory().addItem(wonItem);
            player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);

            gui.setItem(3, 5, new GuiItem(wonItem));
            gui.update();
        }
    }

    private void populateMenu() {
        GuiItem item = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem();
        for (int i = 0; i < (gui.getRows() * 9); i++) {
            this.gui.setItem(i, item);
        }
        gui.update();
    }

}
