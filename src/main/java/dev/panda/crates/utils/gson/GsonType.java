/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Note
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.util.BlockVector
 *  org.bukkit.util.Vector
 */
package dev.panda.crates.utils.gson;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;
import java.util.List;

public class GsonType {
    public static final Type BLOCKVECTOR = new TypeToken<List<BlockVector>>(){}.getType();
    public static final Type VECTOR = new TypeToken<List<Vector>>(){}.getType();
    public static final Type LOCATION = new TypeToken<List<Location>>(){}.getType();
    public static final Type ITEMSTACK = new TypeToken<List<ItemStack>>(){}.getType();
    public static final Type POTIONEFFECT = new TypeToken<List<PotionEffect>>(){}.getType();
    public static final Type STRING_LIST = new TypeToken<List<String>>(){}.getType();
    public static final Type NOTE = new TypeToken<List<Note>>(){}.getType();
}

