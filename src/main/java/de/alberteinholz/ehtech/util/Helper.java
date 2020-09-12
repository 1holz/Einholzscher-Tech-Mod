package de.alberteinholz.ehtech.util;

import java.util.stream.IntStream;

import de.alberteinholz.ehtech.TechMod;
import net.minecraft.util.Identifier;

public class Helper {
    public static int[] countingArray(int size) {
        return IntStream.range(0, size).toArray();
    }

    //TODO: expand this
    public static Identifier makeId(String name) {
        return new Identifier(TechMod.MOD_ID, name);
    }
}