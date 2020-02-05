package net.snofox.bungeecord.shriekingshutdown;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Josh on 2020-02-02
 */
class ShutdownTask implements Runnable {
    private final ShriekingShutdown module;
    final Date shutdownDate;

    public ShutdownTask(final ShriekingShutdown shriekingShutdown, final int seconds) {
        this.module = shriekingShutdown;
        shutdownDate = new Date(new Date().getTime() + seconds*1000);
    }

    @Override
    public void run() {
        final Collection<ProxiedPlayer> players = module.getProxy().getPlayers();
        String message = "Proxy shutting down " + estimateDuration(shutdownDate) + "; please reconnect";
        if(players.size() <= 0 || shutdownDate.before(new Date())) {
            message = "Proxy shutting down now. You will be disconnected.";
            module.getProxy().stop("Proxy shutdown. Please reconnect");
        }
        for(ProxiedPlayer proxiedPlayer : module.getProxy().getPlayers()) {
            proxiedPlayer.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + message));
        }
    }

    /**
     * Takes in a date and the highest time unit; ex "in 1 hour" or "in 3 minutes"
     * Or a few special lines for weird results
     * @param date
     * @return
     */
    String estimateDuration(Date date) {
        if(date == null) return "I guess? This is bug";
        long timeLeft = (date.getTime() - new Date().getTime())/1000;
        if(timeLeft <= 0) return "soon";
        StringBuilder sb = new StringBuilder();
        sb.append("in ");

        LinkedHashMap<String, Long> units = new LinkedHashMap<String, Long>(4);
        units.put("day", (long)24*3600);
        units.put("hour", (long)3600);
        units.put("minute", (long)60);
        units.put("second", (long)1);

        boolean comma = false;

        for(Map.Entry<String, Long> thisUnit: units.entrySet()) {
            long unitTime = thisUnit.getValue();
            long these = timeLeft / unitTime;
            if(comma && thisUnit.getValue() == 1) break; // skip seconds if we have minutes
            if(these > 0) {
                if(comma) sb.append(", ");
                if(unitTime == 60) ++these; // add an extra minute, since we're cutting off seconds
                sb.append(these).append(" ").append(thisUnit.getKey());
                sb.append((these == 1 ? "" : "s"));
                comma = true;
            }
            timeLeft -= these * unitTime;
        }
        return sb.toString();
    }
}
