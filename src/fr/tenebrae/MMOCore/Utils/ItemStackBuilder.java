package fr.tenebrae.MMOCore.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class ItemStackBuilder {

    private Material material;
    private int amount = 1;
    private short durability;
    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantmentIntegerMap;
    private String owner;
    private String[] pages;
    private String author;
    private FireworkEffect fireworkEffect;
    private Color color;
    private boolean scaling;
    private Set<PotionEffect> potionEffects;
    private boolean glowing = false;
    private PotionType potionType = PotionType.AWKWARD;

    public ItemStackBuilder() {
        lore = new ArrayList<>();
        enchantmentIntegerMap = new HashMap<Enchantment, Integer>();
        potionEffects = new HashSet<PotionEffect>();
    }

    public ItemStackBuilder withMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemStackBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder withDurability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder withDurability(int durability) {
        this.durability = (short) durability;
        return this;
    }
    
    public ItemStackBuilder withPotionType(PotionType type) {
    	this.potionType = type;
    	return this;
    }

    public ItemStackBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    
    public ItemStackBuilder setGlowing(boolean glow) {
    	this.glowing = glow;
    	return this;
    }

    public ItemStackBuilder withLore(List<String> lore) {
        if (lore == null) {
            this.lore = new ArrayList<String>();
            return this;
        }
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder addLore(String string) {
        lore.add(string);
        return this;
    }

    public ItemStackBuilder withEnchantmentMap(Map<Enchantment, Integer> map) {
        if (map == null) {
            this.enchantmentIntegerMap = new HashMap<Enchantment, Integer>();
            return this;
        }
        this.enchantmentIntegerMap = map;
        return this;
    }
    

    public ItemStackBuilder addEnchantment(Enchantment enchantment, Integer integer) {
    	if (this.enchantmentIntegerMap == null) this.enchantmentIntegerMap = new HashMap<Enchantment, Integer>();
        this.enchantmentIntegerMap.put(enchantment, integer);
        return this;
    }

    public ItemStackBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public ItemStackBuilder withPages(String[] pages) {
        this.pages = pages;
        return this;
    }

    public ItemStackBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public ItemStackBuilder withFireworkEffect(FireworkEffect effect) {
        this.fireworkEffect = effect;
        return this;
    }

    public ItemStackBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public ItemStackBuilder withScaling(boolean scaling) {
        this.scaling = scaling;
        return this;
    }

    public ItemStackBuilder withPotionEffects(Set<PotionEffect> effects) {
        if (effects == null) {
            this.potionEffects = new HashSet<>();
            return this;
        }
        this.potionEffects = effects;
        return this;
    }

    public ItemStackBuilder withPotionEffect(PotionEffect effect) {
        this.potionEffects.add(effect);
        return this;
    }

    public ItemStack build() {
        if (material == null) return null;
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);
        if (itemMeta instanceof BookMeta) {
            if (author != null) ((BookMeta) itemMeta).setAuthor(author);
            if (pages != null) ((BookMeta) itemMeta).setPages(pages);
            if (displayName != null) ((BookMeta) itemMeta).setTitle(displayName);
        } else if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(owner);
        } else if (itemMeta instanceof PotionMeta) {
            for (PotionEffect potionEffect : potionEffects) {
                ((PotionMeta) itemMeta).addCustomEffect(potionEffect, true);
            }
            ((PotionMeta) itemMeta).setBasePotionData(new PotionData(potionType));
        } else if (itemMeta instanceof EnchantmentStorageMeta) {
            for (Map.Entry<Enchantment, Integer> entry : enchantmentIntegerMap.entrySet()) {
                ((EnchantmentStorageMeta) itemMeta)
                        .addStoredEnchant(entry.getKey(), entry.getValue(), true);
            }
        } else if (itemMeta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) itemMeta).setEffect(fireworkEffect);
        } else if (itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        } else if (itemMeta instanceof MapMeta) {
            ((MapMeta) itemMeta).setScaling(scaling);
        }

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (this.glowing) {
        	itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        	itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
	        for (Map.Entry<Enchantment, Integer> entry : enchantmentIntegerMap.entrySet()) {
	            itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
	        }
        }
        
        ItemStack itemStack = new ItemStack(material, amount, durability);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}