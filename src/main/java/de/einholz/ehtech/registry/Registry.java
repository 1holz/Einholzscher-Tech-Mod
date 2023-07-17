package de.einholz.ehtech.registry;

import de.einholz.ehmooshroom.gui.screen.ContainerScreen;
import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.RegEntry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.MachineBlock;
import de.einholz.ehtech.block.entity.CoalGeneratorBE;
import de.einholz.ehtech.block.entity.OreGrowerBE;
import de.einholz.ehtech.gui.gui.CoalGeneratorGui;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import de.einholz.ehtech.item.Wrench;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class Registry {
    public static final Identifier wrenchId = TechMod.HELPER.makeId("wrench");

    // BLOCKS
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> CHARCOAL_BLOCK = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withBlockBuild(Block::new,
                    FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(5.0F, 6.0F))
            .withBlockItemBuild(new Item.Settings())
            .withFuelBuild(16000)
            .withItemGroupAddBuild(wrenchId)
            .build("charcoal_block");
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> COKE_COAL_BLOCK = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withBlockBuild(Block::new,
                    FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(5.0F, 6.0F))
            .withBlockItemBuild(new Item.Settings())
            .withFuelBuild(32000)
            .withItemGroupAddBuild(wrenchId)
            .build("coke_coal_block");
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> MACHINE_FRAME_1 = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withBlockBuild(Block::new, MachineBlock.getDefaultSettings())
            .withBlockItemBuild(new Item.Settings())
            .withItemGroupAddBuild(wrenchId)
            .build("machine_frame_1");

    // MACHINES
    public static final RegEntry<CoalGeneratorBE, CoalGeneratorGui, ContainerScreen<CoalGeneratorGui>, AdvRecipe> COAL_GENERATOR = RegistryTemplates
            .machine(
                    new RegistryEntryBuilder<CoalGeneratorBE, CoalGeneratorGui, ContainerScreen<CoalGeneratorGui>, AdvRecipe>())
            .withBlockEntityBuild(CoalGeneratorBE::new)
            .withGuiBuild(ExtendedScreenHandlerType<CoalGeneratorGui>::new, CoalGeneratorGui::init)
            .withScreenBuild(ContainerScreen::new)
            .withAdvRecipeBuild()
            .build("coal_generator");
    public static final RegEntry<OreGrowerBE, OreGrowerGui, ContainerScreen<OreGrowerGui>, AdvRecipe> ORE_GROWER = RegistryTemplates
            .machine(new RegistryEntryBuilder<OreGrowerBE, OreGrowerGui, ContainerScreen<OreGrowerGui>, AdvRecipe>())
            .withBlockEntityBuild(OreGrowerBE::new)
            .withGuiBuild(ExtendedScreenHandlerType<OreGrowerGui>::new, OreGrowerGui::init)
            .withScreenBuild(ContainerScreen::new)
            .withAdvRecipeBuild()
            .build("ore_grower");

    // ITEMS
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> HARD_COAL_TINY = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withItemBuild(Item::new, new Item.Settings())
            .withFuelBuild(200)
            .withItemGroupAddBuild(wrenchId)
            .build("hard_coal_tiny");
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> CHARCOAL_TINY = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withItemBuild(Item::new, new Item.Settings())
            .withFuelBuild(200)
            .withItemGroupAddBuild(wrenchId)
            .build("charcoal_tiny");
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> COKE_COAL_CHUNK = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withItemBuild(Item::new, new Item.Settings())
            .withFuelBuild(400)
            .withItemGroupAddBuild(wrenchId)
            .build("coke_coal_tiny");
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> COKE_COAL_TINY = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withItemBuild(Item::new, new Item.Settings())
            .withFuelBuild(3200)
            .withItemGroupAddBuild(wrenchId)
            .build("coke_coal_chunk");

    // ITEM GROUPS
    public static final RegEntry<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>> WRENCH = new RegistryEntryBuilder<BlockEntity, ScreenHandler, HandledScreen<ScreenHandler>, Recipe<?>>()
            .withSelfItemGroupBuild(Wrench::new, new Item.Settings())
            .build("wrench");

    public static void register() {
    }
}
