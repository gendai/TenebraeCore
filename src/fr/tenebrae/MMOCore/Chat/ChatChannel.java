package fr.tenebrae.MMOCore.Chat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ChatChannel {

    double range;
    String pattern;

    public ChatChannel(double range, String pattern) {
        this.range = range;
        this.pattern = pattern;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    public Set<Player> getRecipients(Location loc) {
        Set<Player> recipients = new HashSet<Player>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLoc = player.getLocation();
            if (Math.hypot(loc.getX() - playerLoc.getX(), loc.getZ() - playerLoc.getZ()) <= this.range)
                recipients.add(player);
        }
        return recipients;
    }
}
