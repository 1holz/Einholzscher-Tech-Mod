package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.RecipeSerializerRegistry;
import de.einholz.ehtech.TechMod;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class RecipeSerializerReg<T extends Recipe<?>> extends RecipeSerializerRegistry<T> {
    @SuppressWarnings("unchecked")
    public static final RecipeSerializer<AdvRecipe> COAL_GENERATOR = (RecipeSerializer<AdvRecipe>) new RecipeSerializerReg<>()
            .register("coal_generator")
            .get();
    @SuppressWarnings("unchecked")
    public static final RecipeSerializer<AdvRecipe> ORE_GROWER = (RecipeSerializer<AdvRecipe>) new RecipeSerializerReg<>()
            .register("ore_grower")
            .get();

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
