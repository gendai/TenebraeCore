package fr.tenebrae.MMOCore.LoginScreen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Mechanics.MMOClass;
import fr.tenebrae.MMOCore.Utils.NicknameAPI;
import fr.tenebrae.MMOCore.Utils.SQLHelper;
import fr.tenebrae.PlayerLanguage.LanguageAPI;

public class LoginScreen {

	
	private Player p;
	public Hologram playButton;
	public List<Hologram> chars = new ArrayList<Hologram>();
	public List<Hologram> misc = new ArrayList<Hologram>();
	private String selectedChar = "";
	
	private static Location loc = new Location(Bukkit.getWorld("MMOCharCreation"), 212.5, 70.0, 256.5);
	private static List<Location> slots = new ArrayList<Location>(Arrays.asList(
			new Location(Bukkit.getWorld("MMOCharCreation"), 208.5, 71.2, 257.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 208.5, 71.2, 255.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 210.5, 71.2, 253.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 212.5, 71.2, 253.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 214.5, 71.2, 253.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 216.5, 71.2, 255.5),
			new Location(Bukkit.getWorld("MMOCharCreation"), 216.5, 71.2, 257.5)));
	private static Location playButtonLoc = new Location(Bukkit.getWorld("MMOCharCreation"), 212.5, 71.32, 259.75);
	public static Map<Player,LoginScreen> loggingPlayers = new HashMap<Player,LoginScreen>();
	
	public LoginScreen(final Player p) {
		this.p = p;
		
		SQLResultSet sqlRS = null;
		ResultSet chars = null;
		try {
			sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_PLAYERS_TABLE, "owner_uuid", p.getUniqueId().toString());
			chars = sqlRS.getResultSet();
		} catch (ClassNotFoundException | SQLException e) {
			p.kickPlayer("§cAn error occured while loading your characters data.");
			e.printStackTrace();
			return;
		}
		
		loggingPlayers.put(p,this);
		p.teleport(loc);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.hidePlayer(p);
			p.hidePlayer(pl);
		}
		
		/*new BukkitRunnable() {
			@Override
			public void run() {
				NickNamerAPI.getNickManager().setNick(p.getUniqueId(), p.getName());
			}
		}.runTaskLaterAsynchronously(Main.plugin, 20L);*/
		
		this.playButton = HologramsAPI.createHologram(Main.plugin, playButtonLoc);
		VisibilityManager vmp = this.playButton.getVisibilityManager();
		vmp.setVisibleByDefault(false);
		vmp.showTo(p);
		TextLine l = this.playButton.appendTextLine("§7Play");
		l.setTouchHandler(new TouchHandler() {
					@Override
					public void onTouch(Player pt) {
						if (!pt.getName().equals(p.getName())) return;
						play();
					}
				});
		
		int i = 0;
		try {
			while(!chars.isAfterLast()) {
				if (chars.isAfterLast()) break;
				if (!chars.next()) break;
				Hologram hol = HologramsAPI.createHologram(Main.plugin, slots.get(i));
				VisibilityManager vm = hol.getVisibilityManager();
				vm.setVisibleByDefault(false);
				vm.showTo(p);
				final TextLine l1 = hol.appendTextLine(chars.getString("name"));
				l1.setTouchHandler(new TouchHandler() {
					@Override
					public void onTouch(Player pt) {
						if (!pt.getName().equals(p.getName())) return;
						selectCharacter(l1.getText());
					}
				});
				final TextLine l2 = hol.appendTextLine(MMOClass.valueOf(chars.getString("class")).getString(LanguageAPI.getStringLanguage(p))+"  LvL "+chars.getInt("level"));
				l2.setTouchHandler(new TouchHandler() {
					@Override
					public void onTouch(Player pt) {
						if (!pt.getName().equals(p.getName())) return;
						selectCharacter(l1.getText());
					}
				});
				this.chars.add(hol);
				i++;
				if (i > 6) break;
			}
			if (i < 6) {
				while(i < 6) {
					Hologram hol = HologramsAPI.createHologram(Main.plugin, slots.get(i));
					VisibilityManager vm = hol.getVisibilityManager();
					vm.setVisibleByDefault(false);
					vm.showTo(p);
					final TextLine l1 = hol.appendTextLine("§6New Character");
					l1.setTouchHandler(new TouchHandler() {
						@Override
						public void onTouch(Player pt) {
							if (!pt.getName().equals(p.getName())) return;
							if (CharacterCreator.creatingPlayers.containsKey(pt.getName())) return;
							createCharacter();
						}
					});
					this.misc.add(hol);
					i++;
				}
			}
			
			if (!this.chars.isEmpty()) {
				this.selectCharacter(((TextLine)this.chars.get(0).getLine(0)).getText());
			}
			sqlRS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createCharacter() {
		for (Hologram hol : chars) hol.delete();
		for (Hologram hol : misc) hol.delete();
		playButton.delete();
		loggingPlayers.remove(p);
		
		new CharacterCreator(p);
	}
	
	public void selectCharacter(String name) {
		p.sendMessage("§6You have selected: §f"+name);
		this.selectedChar = name;
		((TextLine)playButton.getLine(0)).setText("§6Play");
	}
	
	public void play() {
		for (Hologram hol : chars) hol.delete();
		for (Hologram hol : misc) hol.delete();
		playButton.delete();
		loggingPlayers.remove(p);
		
		Main.connectedCharacters.put(p, new fr.tenebrae.MMOCore.Characters.Character(this.selectedChar));
		
		for (Player pm : Bukkit.getOnlinePlayers()) {
			p.showPlayer(pm);
			pm.showPlayer(p);
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					NicknameAPI.applyNick(p, selectedChar);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 20L);
	}
}

