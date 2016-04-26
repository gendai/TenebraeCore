package fr.tenebrae.MMOCore.Chat;

import fr.tenebrae.MMOCore.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class ChatManager {

    private HashMap<String, ChatChannel> channels = new HashMap<String, ChatChannel>();

    public void init(ConfigurationSection config)  {
        for (String key :config.getKeys(false)) {
            addChannel(key, new ChatChannel(config.getDouble(key+".range"), config.getString(key+".pattern")));
        }
        ChatHandler handler = new ChatHandler(this);
        Bukkit.getPluginManager().registerEvents(handler,Main.plugin);
        Main.plugin.getCommand("yell").setExecutor(handler);
        Main.plugin.getCommand("me").setExecutor(handler);

    }

    public void addChannel(String name, ChatChannel channel) {
        this.channels.put(name, channel);
    }

    public void editChannel(String name, ChatChannel channel) {
        this.channels.remove(name);
        this.channels.put(name, channel);
    }

    public void removeChannel(String name) {
        this.channels.remove(name);
    }

    public ChatChannel getChannel(String name) {
        return this.channels.get(name);
    }
}
