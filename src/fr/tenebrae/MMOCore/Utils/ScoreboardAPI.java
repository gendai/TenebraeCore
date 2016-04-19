package fr.tenebrae.MMOCore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardAPI {
	
	public static Scoreboard createScoreboard() {
		return Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public static Scoreboard getVanillaScoreboard() {
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}
	
	public static Objective createObjective(Scoreboard scoreboard, String objectiveName, String objectiveType) {
		return scoreboard.registerNewObjective(objectiveName, objectiveType);
	}
	
	public static void setObjectiveDisplay(Objective objective, DisplaySlot slot) {
		objective.setDisplaySlot(slot);
	}
	
	public static Score getPlayer(Objective objective, String name) {
		return objective.getScore(name);
	}
	
	public static void setScore(Score player, int score) {
		player.setScore(score);
	}
	
	public static void setScore(Objective objective, String player, int score) {
		if (player.length() <= 16) {
			objective.getScore(player).setScore(score);
		} else {
			Team playerTeam = objective.getScoreboard().registerNewTeam(player.substring(0, 8).replace("§", ""));
			if (player.length() > 16 && player.length() <= 32) {
				playerTeam.setPrefix(player.substring(0, 15));
				playerTeam.addEntry(player.substring(16, player.length()-1));
				objective.getScore(playerTeam.getEntries().iterator().next()).setScore(score);
			} else if (player.length() > 32 && player.length() <= 48) {
				playerTeam.setPrefix(player.substring(0, 15));
				playerTeam.addEntry(player.substring(16, 31));
				playerTeam.setSuffix(player.substring(32, player.length()-1));
				objective.getScore(playerTeam.getEntries().iterator().next()).setScore(score);
			} else if (player.length() > 48) return;
		}
	}
	
	public static void removePlayer(Objective objective, Score player) {
		objective.getScoreboard().resetScores(player.getEntry());
	}
	
	public static void removeObjective(Objective objective) {
		objective.unregister();
	}
}
