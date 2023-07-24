package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.RecipeTypeRegistry;
import de.einholz.ehtech.TechMod;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class RecipeTypeReg<T extends Recipe<?>> extends RecipeTypeRegistry<T> {
    @SuppressWarnings("unchecked")
    public static final RecipeType<AdvRecipe> COAL_GENERATOR = (RecipeType<AdvRecipe>) new RecipeTypeReg<>()
            .register("coal_generator")
            .get();
    @SuppressWarnings("unchecked")
    public static final RecipeType<AdvRecipe> ORE_GROWER = (RecipeType<AdvRecipe>) new RecipeTypeReg<>()
            .register("ore_grower")
            .get();

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
