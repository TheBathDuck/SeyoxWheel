package nl.daanplugge.seyoxwheel.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class ChatUtils {

    public Component parse(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

}
