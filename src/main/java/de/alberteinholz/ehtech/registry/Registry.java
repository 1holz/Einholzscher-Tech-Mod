package de.alberteinholz.ehtech.registry;

import java.util.Optional;
import java.util.function.Function;

import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
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
import de.alberteinholz.ehtech.items.Wrench;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
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

    //TODO: optimize for newer Mooshroom versions
    public static void register() {
        //item groups
        RegistryHelper.create(TechMod.HELPER.makeId("wrench")).withItemGroupBuild().withItemBuildAutoItemGroup(Wrench::new, new Item.Settings());
        //items
        Function<RegistryEntry, RegistryEntry> simpleItemTemplate = entry -> {
            return entry.withItemBuild(Item::new, new Item.Settings().group(RegistryHelper.getEntry(TechMod.HELPER.makeId("wrench")).itemGroup));
        };
        RegistryHelper.create(TechMod.HELPER.makeId("hard_coal_tiny")).applyTemplate(simpleItemTemplate).makeItemFurnaceFuel(200);
        RegistryHelper.create(TechMod.HELPER.makeId("charcoal_tiny")).applyTemplate(simpleItemTemplate).makeItemFurnaceFuel(200);
        RegistryHelper.create(TechMod.HELPER.makeId("coke_coal_tiny")).applyTemplate(simpleItemTemplate).makeItemFurnaceFuel(400);
        RegistryHelper.create(TechMod.HELPER.makeId("coke_coal_chunk")).applyTemplate(simpleItemTemplate).makeItemFurnaceFuel(3200);
        //blocks
        Function<RegistryEntry, RegistryEntry> simpleBlockTemplate = entry -> {
            return entry.withBlockItemBuild(new Item.Settings().group(RegistryHelper.getEntry(TechMod.HELPER.makeId("wrench")).itemGroup));
        };
        FuelRegistry.INSTANCE.add(RegistryHelper.create(TechMod.HELPER.makeId("charcoal_block")).withBlock(new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BLACK).requiresTool().strength(5.0F, 6.0F))).applyTemplate(simpleBlockTemplate).item, 16000);
        FuelRegistry.INSTANCE.add(RegistryHelper.create(TechMod.HELPER.makeId("coke_coal_block")).withBlock(new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.GRAY).requiresTool().strength(5.0F, 6.0F))).applyTemplate(simpleBlockTemplate).item, 32000);
        RegistryHelper.create(TechMod.HELPER.makeId("machine_frame_1")).withBlock(new Block(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(5, 10))).applyTemplate(simpleBlockTemplate);
        //machines:
        Function<RegistryEntry, RegistryEntry> machineTemplate = entry -> {
            return entry.withBlock(new MachineBlock(entry.id)).withScreenHacky(ContainerScreen::new).withRecipe(getDefaultRecipeType(entry.id)).withRecipeSerializer(new Serializer()).applyTemplate(simpleBlockTemplate);
        };
        RegistryHelper.create(TechMod.HELPER.makeId("coal_generator")).withGui(CoalGeneratorGui::new).applyTemplate(machineTemplate).withBlockEntityBuild(CoalGeneratorBlockEntity::new);
        RegistryHelper.create(TechMod.HELPER.makeId("ore_grower")).withGui(OreGrowerGui::new).applyTemplate(machineTemplate).withBlockEntityBuild(OreGrowerBlockEntity::new);
        //additional guis
        RegistryHelper.create(TechMod.HELPER.makeId("machine_config")).withGui(MachineConfigGui::new).withScreenHacky(ContainerScreen::new);
    }
}