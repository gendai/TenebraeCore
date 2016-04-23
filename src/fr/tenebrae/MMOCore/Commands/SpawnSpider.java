package fr.tenebrae.MMOCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Entities.L01MineSpider;

public class SpawnSpider implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player) {
            Player player = (Player) arg0;
            L01MineSpider spider = new L01MineSpider(player.getLocation());
            spider.spawn();
		}
		return true;
	}


}
