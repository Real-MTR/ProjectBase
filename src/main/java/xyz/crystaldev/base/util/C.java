package xyz.crystaldev.base.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@UtilityClass
public class C {

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
}
