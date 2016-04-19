package fr.tenebrae.MMOCore.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarScheduler extends BukkitRunnable {
	
	private String msg;
	private List<Player> players = new ArrayList<Player>();
	private Player player;
	private int ticks = 0;

	public ActionBarScheduler(Player player, String msg, int ticks) {
		if (ticks <= 40) {
			this.ticks = 1;
		} else {
			this.ticks = ticks-40;
		}
		this.msg = msg;
		this.player = player;
		this.ticks = ticks;
	}
	
	public ActionBarScheduler(List<Player> players, String msg, int ticks) {
		if (ticks <= 40) {
			this.ticks = 1;
		} else {
			this.ticks = ticks-40;
		}
		this.msg = msg;
		this.players = players;
		this.ticks = ticks;
	}
	
	public ActionBarScheduler(ArrayList<fr.tenebrae.MMOCore.Characters.Character> characters, String msg, int ticks) {
		List<Player> players = new ArrayList<Player>();
		for (fr.tenebrae.MMOCore.Characters.Character c : characters) {
			players.add(c.getAccount());
		}
		if (ticks <= 40) {
			this.ticks = 1;
		} else {
			this.ticks = ticks-40;
		}
		this.msg = msg;
		this.players = players;
		this.ticks = ticks;
	}
	
	@Override
	public void run() {
		if (ticks <= 0) {
			this.cancel();
		} else {
			ticks--;
			if (players.isEmpty()) {
				ActionBarAPI.sendActionBar(player, msg);
			} else {
				for (Player p : players) {
					ActionBarAPI.sendActionBar(p, msg);
				}
			}
		}
	}
	
}