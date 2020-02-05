package net.snofox.bungeecord.shriekingshutdown;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Josh on 2020-02-02
 */
public class ShutdownCommand extends Command {
    public ShutdownCommand(final String name, final String permission, final String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if(args.length != 1) {
            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Syntax error: /shutdown <seconds>"));
        }
        final int seconds = Integer.parseInt(args[0]);
        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Proxy shutting down in " + seconds + " seconds"));
        ShriekingShutdown.getInstance().shutdown(seconds);
    }
}
