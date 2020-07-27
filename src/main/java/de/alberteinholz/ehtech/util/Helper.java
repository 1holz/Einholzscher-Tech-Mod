package de.alberteinholz.ehtech.util;

import java.util.Arrays;

import de.alberteinholz.ehtech.TechMod;
import net.minecraft.util.Identifier;

public class Helper {
    public static Integer[] countingArray(int size) {
        Integer[] array = new Integer[size];
        Arrays.setAll(array, i -> i);
        return array;
    }

    //TODO: expand this
    public static Identifier makeIdentifier(String name) {
        return new Identifier(TechMod.MOD_ID, name);
    }
}