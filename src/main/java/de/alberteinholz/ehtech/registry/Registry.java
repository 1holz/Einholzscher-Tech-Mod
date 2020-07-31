package de.alberteinholz.ehtech.registry;

import java.util.Optional;

import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
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
import de.alberteinholz.ehtech.util.Helper;
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

    public static void register() {
        //item groups
        RegistryHelper.create(Helper.makeId("wrench")).withItemGroupBuild().withItemBuildAutoItemGroup(Wrench::new, new Item.Settings());
        //items
        RegistryEntry simpleItemTemplate = RegistryHelper.create(null).withItemBuild(Item::new, new Item.Settings().group(RegistryHelper.getEntry(Helper.makeId("wrench")).itemGroup));
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("tiny_hard_coal")).applyTemplate(simpleItemTemplate).item, 200);
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("tiny_charcoal")).applyTemplate(simpleItemTemplate).item, 200);
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("tiny_coke_coal")).applyTemplate(simpleItemTemplate).item, 400);
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("coke_coal_chunk")).applyTemplate(simpleItemTemplate).item, 3200);
        //blocks
        RegistryEntry simpleBlockTemplate = RegistryHelper.create(null).withBlockItemBuild(new Item.Settings().group(RegistryHelper.getEntry(Helper.makeId("wrench")).itemGroup));
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("charcoal_block")).withBlock(new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BLACK).requiresTool().strength(5.0F, 6.0F))).applyTemplate(simpleBlockTemplate).item, 16000);
        FuelRegistry.INSTANCE.add(RegistryHelper.create(Helper.makeId("coke_coal_block")).withBlock(new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.GRAY).requiresTool().strength(5.0F, 6.0F))).applyTemplate(simpleBlockTemplate).item, 32000);
        RegistryHelper.create(Helper.makeId("machine_frame_tier_1")).withBlock(new Block(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(5, 10))).applyTemplate(simpleBlockTemplate);
        //machines:
        RegistryEntry machineTemplate = RegistryHelper.create(null).withScreen(ContainerScreen::new).withRecipeSerializer(new Serializer()).applyTemplate(simpleBlockTemplate);
        RegistryHelper.create(Helper.makeId("coal_generator")).withBlock(new MachineBlock(Helper.makeId("coal_generator"))).withBlockEntityBuild(CoalGeneratorBlockEntity::new).withGui(CoalGeneratorGui::new).withRecipe(getDefaultRecipeType(Helper.makeId("coal_generator"))).applyTemplate(machineTemplate);
        RegistryHelper.create(Helper.makeId("ore_grower")).withBlock(new MachineBlock(Helper.makeId("ore_grower"))).withBlockEntityBuild(OreGrowerBlockEntity::new).withGui(OreGrowerGui::new).withRecipe(getDefaultRecipeType(Helper.makeId("ore_grower"))).applyTemplate(machineTemplate);
        //additional guis
        RegistryHelper.create(Helper.makeId("machine_config")).withGui(MachineConfigGui::new).withScreen(ContainerScreen::new);
    }
}