package fr.tenebrae.MMOCore.Chat;

import com.google.common.base.Joiner;
import fr.tenebrae.MMOCore.Utils.StringParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatHandler implements Listener,CommandExecutor {

    private ChatManager manager;

    public ChatHandler(ChatManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        ChatChannel channel = manager.getChannel("say");
        String message = event.getMessage();

        sendMessage(channel,message,player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to perform this command!");
            return false;
        }
        Player player = (Player) sender;
        String message = Joiner.on("").join(args);
        ChatChannel channel;
        if (cmd.getName().equalsIgnoreCase("yell")) {
            channel = manager.getChannel("yell");
            sendMessage(channel,message,player);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("me")) {
            channel = manager.getChannel("emote");
            sendMessage(channel,message,player);
            return true;
        }
        return false;
    }

    public void sendMessage(ChatChannel channel, String message, Player sender) {
        Set<Player> recipients = channel.getRecipients(sender.getLocation());
        message = StringParser.parse(sender, channel.getPattern()).replaceAll("\\{MSG\\}", message);

        for (Player player: recipients) {
            player.sendMessage(message);
        }
    }

}
