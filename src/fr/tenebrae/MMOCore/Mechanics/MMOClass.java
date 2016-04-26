package fr.tenebrae.MMOCore.Mechanics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.Archer.L01RecruitTunic;
import fr.tenebrae.MMOCore.Items.Archer.L01TrainingBow;
import fr.tenebrae.MMOCore.Items.Assassin.L01TrainingDagger;
import fr.tenebrae.MMOCore.Items.Mage.L01ApprenticeRobe;
import fr.tenebrae.MMOCore.Items.Mage.L01TrainingStaff;
import fr.tenebrae.MMOCore.Items.Warrior.L01RecruitShirt;
import fr.tenebrae.MMOCore.Items.Warrior.L01TrainingSword;
import fr.tenebrae.MMOCore.Utils.SQLHelper;

public enum MMOClass {

	WARRIOR(60000, L01TrainingSword.class, L01RecruitShirt.class),
	ARCHER(60001, L01TrainingBow.class, L01RecruitTunic.class),
	ASSASSIN(60002, L01TrainingDagger.class, L01RecruitTunic.class),
	MAGE(60003, L01TrainingStaff.class, L01ApprenticeRobe.class);
	
	private int id = 0;
	private Class<? extends Item> startWeapon;
	private Class<? extends Item> startArmor;
	
	private MMOClass(int id, Class<? extends Item> startWeapon, Class<? extends Item> startArmor) {
		this.id = id;
		this.startWeapon = startWeapon;
		this.startArmor = startArmor;
	}
	
	public String getString(String language) {
		String returned = "null";
		try {
			SQLResultSet sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", id);
			ResultSet nameRow = sqlRS.getResultSet();
			if (!nameRow.next()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			if (nameRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			returned = nameRow.getString(language);
			sqlRS.close();
		} catch (Exception e) { e.printStackTrace(); }
		if (returned == null) returned = "null";
		return returned;
	}
	
	public Item getStartWeapon() {
		try {
			return this.startWeapon.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Item getStartArmor() {
		try {
			return this.startArmor.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Stats,Double> getBaseStats() {
		Map<Stats,Double> returned = new HashMap<Stats,Double>();
		for (Stats stat : Stats.values()) {
			returned.put(stat, 0.0D);
		}
		switch(this) {
		case WARRIOR:
			returned.put(Stats.HEALTH, 38.0D);
			returned.put(Stats.STRENGTH, 2.0D);
			returned.put(Stats.STAMINA, 3.0D);
			returned.put(Stats.CRITICAL_CHANCE, 4.0D);
			returned.put(Stats.DODGE_CHANCE, 4.0D);
			returned.put(Stats.HIT_CHANCE, 4.0D);
			break;
		case ARCHER:
			returned.put(Stats.HEALTH, 30.0D);
			returned.put(Stats.AGILITY, 3.0D);
			returned.put(Stats.STAMINA, 2.0D);
			returned.put(Stats.CRITICAL_CHANCE, 5.25D);
			returned.put(Stats.DODGE_CHANCE, 5.0D);
			returned.put(Stats.HIT_CHANCE, 5.0D);
			returned.put(Stats.MANA, 10.0D);
			break;
		case ASSASSIN:
			returned.put(Stats.HEALTH, 26.0D);
			returned.put(Stats.AGILITY, 2.0D);
			returned.put(Stats.STRENGTH, 2.0D);
			returned.put(Stats.STAMINA, 1.0D);
			returned.put(Stats.CRITICAL_CHANCE, 9.75D);
			returned.put(Stats.DODGE_CHANCE, 7.5D);
			returned.put(Stats.HIT_CHANCE, 3.0D);
			break;
		case MAGE:
			returned.put(Stats.HEALTH, 20.0D);
			returned.put(Stats.INTELLIGENCE, 3.0D);
			returned.put(Stats.SPIRIT, 2.0D);
			returned.put(Stats.CRITICAL_CHANCE, 3.5D);
			returned.put(Stats.DODGE_CHANCE, 4.0D);
			returned.put(Stats.HIT_CHANCE, 5.0D);
			returned.put(Stats.MANA, 30.0D);
			break;
		default:
			returned.put(Stats.HEALTH, 23.0D);
			returned.put(Stats.INTELLIGENCE, 1.0D);
			returned.put(Stats.SPIRIT, 1.0D);
			returned.put(Stats.STAMINA, 1.0D);
			returned.put(Stats.STRENGTH, 1.0D);
			returned.put(Stats.AGILITY, 1.0D);
			returned.put(Stats.CRITICAL_CHANCE, 5.0D);
			returned.put(Stats.DODGE_CHANCE, 5.0D);
			returned.put(Stats.HIT_CHANCE, 5.0D);
			break;
		}
		return returned;
	}
}
