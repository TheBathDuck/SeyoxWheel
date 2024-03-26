package nl.daanplugge.seyoxwheel.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class GsonStack {

    public String itemStackToString(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsStack = org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack.asNMSCopy(itemStack);
        CompoundTag compound = new CompoundTag();
        nmsStack.save(compound);
        return compound.toString();
    }

    public ItemStack stringToItemStack(String dataString) throws CommandSyntaxException {
        CompoundTag comp = TagParser.parseTag(dataString);
        net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.of(comp);
        return CraftItemStack.asBukkitCopy(item);
    }

}