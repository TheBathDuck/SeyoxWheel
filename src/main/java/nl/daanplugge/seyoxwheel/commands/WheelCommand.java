package nl.daanplugge.seyoxwheel.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import nl.daanplugge.seyoxwheel.WheelPlugin;
import nl.daanplugge.seyoxwheel.menu.AnimationMenu;
import nl.daanplugge.seyoxwheel.menu.ItemMenu;
import nl.daanplugge.seyoxwheel.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@CommandAlias("wheel")
@CommandPermission("seyoxwheel.manage")
public class WheelCommand extends BaseCommand implements Listener {


    @Subcommand("items")
    @Description("Bewerk items in een menu")
    public void editItems(Player player) {
        new ItemMenu(player);
    }

    @Subcommand("get")
    public void giveWheelcoin(Player player) {
        ItemStack item = ItemBuilder.from(Material.IRON_INGOT)
                .name(ChatUtils.parse("<gradient:gold:yellow>Wheel Coin"))
                .model(10)
                .setNbt("seyoxwheel_item_wheelcoin", "yes")
                .build();

        player.getInventory().addItem(item);
    }

    @Default("open")
    public void rotate(Player player) {
        if(WheelPlugin.getInstance().getWheelcoinItems().values().size() < 8) {
            player.sendMessage(ChatUtils.parse("<red>Er zijn momenteel niet genoeg items beschikbaar."));
            return;
        }

        player.sendMessage(ChatUtils.parse("<green>Je opent nu een <gradient:gold:yellow>Wheelcoin<green>!"));
        new AnimationMenu(player).runTaskTimer(WheelPlugin.getInstance(), 0L, 2L);
    }


}
