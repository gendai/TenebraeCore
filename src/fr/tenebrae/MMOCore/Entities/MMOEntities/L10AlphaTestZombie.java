package fr.tenebrae.MMOCore.Entities.MMOEntities;

import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.MMOZombie;
import fr.tenebrae.MMOCore.Entities.Events.MMODeathEvent;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Items.Coins.CopperCoin;
import fr.tenebrae.MMOCore.Items.Coins.GoldCoin;
import fr.tenebrae.MMOCore.Items.Coins.SilverCoin;
import fr.tenebrae.MMOCore.Mechanics.Sound;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.SoundCategory;

public class L10AlphaTestZombie extends MMOZombie {

	public L10AlphaTestZombie(Location loc) {
		super(((CraftWorld)loc.getWorld()).getHandle());
		this.spawn = loc;

		this.drops.put(ItemRegistry.getItem(8), 1.5);
		this.drops.put(ItemRegistry.getItem(9), 1.5);
		this.drops.put(ItemRegistry.getItem(10), 1.5);
		this.drops.put(ItemRegistry.getItem(11), 1.5);
		this.drops.put(ItemRegistry.getItem(12), 1.5);
		this.drops.put(ItemRegistry.getItem(13), 1.5);
		this.drops.put(ItemRegistry.getItem(14), 1.5);
		this.drops.put(ItemRegistry.getItem(15), 1.5);
		this.drops.put(ItemRegistry.getItem(16), 0.5);
		this.drops.put(ItemRegistry.getItem(17), 0.5);
		this.drops.put(ItemRegistry.getItem(19), 1.5);
		this.drops.put(ItemRegistry.getItem(20), 1.5);
		this.drops.put(ItemRegistry.getItem(21), 1.5);
		this.drops.put(new CopperCoin(new Random().nextInt(50)+12), 100.0);
		this.drops.put(new SilverCoin(new Random().nextInt(5)+1), 50.0);
		this.drops.put(new GoldCoin(new Random().nextInt(1)+1), 10.0);
		
		this.maxHealth = 41;
		this.attackSpeed = 1250;
		this.atkMin = 6;
		this.atkMin = 14;
		this.level = 10;
		this.givenXp = 175;
		this.attackRange = 1;
		this.nameId = 300003;
		this.walkSpeed = 0.24786667640209197000000000000001D;
		this.sprintSpeed = 0.37786667640209197000000000000001D;
		this.resetSpeed = 0.47786667640209197000000000000001D;

		this.hurtSound = "mob.littleSpider.hurt";
		this.aggroSound = "mob.littleSpider.aggro";
		this.attackSound = "mob.littleSpider.attack";
		this.idleSound = "mob.littleSpider.idle";
		this.stepSound = "mob.littleSpider.step";
		this.deathSound = "mob.littleSpider.death";
		
		this.clearGoals();
		this.setupGoals();
	}
	
	@Override
	public void onDeath(){
		if (this.isDead) return;
		if (this.target instanceof EntityPlayer) {
			if (!((Player)(((EntityPlayer)target).getBukkitEntity())).isOnline()) {
				this.target = null;
			}else{
				Bukkit.getPluginManager().callEvent(new MMODeathEvent(L10AlphaTestZombie.class, (Player)(((EntityPlayer)target).getBukkitEntity())));
			}
		}
		this.getBEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10, true, false));
		this.getBEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, -10, true, false));
		new Sound(this.deathSound, SoundCategory.HOSTILE).setLoc(this.getLocation()).setPitch(1.26F).play();
		this.world.broadcastEntityEffect(this, (byte) 3);
		if (this.lastDamager instanceof EntityPlayer) {
			fr.tenebrae.MMOCore.Characters.Character c = Main.connectedCharacters.get(((Player)CraftPlayer.getEntity(this.world.getServer(), this.lastDamager)));
			if (c.getLevel()-this.level < 6) c.addXp(this.givenXp);
		}
		this.isDead = true;
		new BukkitRunnable() {
			@Override
			public void run() {
				final Location loc = getLocation();
				dead = true;
				bossBar.setVisible(false);
				if (!drops.isEmpty()) {
					for (Entry<Item,Double> entry : drops.entrySet()) {
						if ((double)(new Random().nextInt(10000))/100 <= entry.getValue()) {
							final ItemStack is = entry.getKey().getItemStack();
							new BukkitRunnable() {
								@Override
								public void run() {
									loc.getWorld().dropItemNaturally(loc, is);
								}
							}.runTask(Main.plugin);
						}
					}
				}
				for (int i = 0; i < 20; i++) {
					double d0 = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;

					world.addParticle(EnumParticle.EXPLOSION_NORMAL, locX + random.nextFloat() * width * 2.0F - width, locY + random.nextFloat() * length, locZ + random.nextFloat() * width * 2.0F - width, d0, d1, d2, new int[0]);
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 1L);
	}
}