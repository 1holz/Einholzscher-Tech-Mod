package de.alberteinholz.ehtech.registry;

import java.util.Optional;

import de.alberteinholz.ehmooshroom.registry.BlockRegistryHelper;
import de.alberteinholz.ehmooshroom.registry.ItemRegistryHelper;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.consumers.OreGrowerBlockEntity;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.generators.CoalGeneratorBlockEntity;
import de.alberteinholz.ehtech.blocks.components.container.InventoryWrapper;
import de.alberteinholz.ehtech.blocks.directionals.containers.machines.MachineBlock;
import de.alberteinholz.ehtech.blocks.guis.guis.machines.CoalGeneratorGui;
import de.alberteinholz.ehtech.blocks.guis.guis.machines.MachineConfigGui;
import de.alberteinholz.ehtech.blocks.guis.guis.machines.OreGrowerGui;
import de.alberteinholz.ehtech.blocks.guis.screens.ContainerScreen;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe.Serializer;
import de.alberteinholz.ehtech.itemgroups.ItemGroups;
import de.alberteinholz.ehtech.items.Wrench;
import de.alberteinholz.ehtech.util.Helper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item.Settings;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Registry {
    //temp:
    private static <T extends Recipe<?>> RecipeType<T> getDefaultRecipeType(Identifier id) {
        return new RecipeType<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public <C extends Inventory> Optional<T> get(Recipe<C> recipe, World world, C inventory) {
                return ((MachineRecipe) recipe).matches(((InventoryWrapper) inventory).pos, world) ? (Optional<T>) Optional.of(recipe) : Optional.empty();
            }

            @Override
            public String toString() {
                return id.getPath();
            }
        };
    }

    public static void registrerItems() {
        ItemRegistryHelper.create(Helper.makeId("wrench")).withItemBuild(Wrench::new, new Settings().group(ItemGroups.EH_TECH)).register();
    }

    public static void registerBlocks() {
        BlockRegistryHelper.create(Helper.makeId("machine_config")).withGui(MachineConfigGui::new).withScreen(ContainerScreen::new);
        //machines:
        BlockRegistryHelper.create(Helper.makeId("coal_generator")).withBlock(new MachineBlock(Helper.makeId("coal_generator"))).withBlockItemBuild(new Settings().group(ItemGroups.EH_TECH)).withBlockEntity(CoalGeneratorBlockEntity::new).withGui(CoalGeneratorGui::new).withScreen(ContainerScreen::new).withRecipe(getDefaultRecipeType(Helper.makeId("coal_generator"))).withRecipeSerializer(new Serializer());
        BlockRegistryHelper.create(Helper.makeId("ore_grower")).withBlock(new MachineBlock(Helper.makeId("ore_grower"))).withBlockItemBuild(new Settings().group(ItemGroups.EH_TECH)).withBlockEntity(OreGrowerBlockEntity::new).withGui(OreGrowerGui::new).withScreen(ContainerScreen::new).withRecipe(getDefaultRecipeType(Helper.makeId("ore_grower"))).withRecipeSerializer(new Serializer());
    }
}