package de.einholz.ehtech.registry;

import de.einholz.ehmooshroom.registry.RegEntry;
import de.einholz.ehtech.block.MachineBlock;
import de.einholz.ehtech.gui.gui.CoalGeneratorGui;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class Registry {
    // BLOCKS
    public static final RegEntry CHARCOAL_BLOCK = new RegistryEntryBuilder().withBlockBuild(Block::new, FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(5.0F, 6.0F)).build("charcoal_block");
    public static final RegEntry COKE_COAL_BLOCK = new RegistryEntryBuilder().withBlockBuild(Block::new, FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(5.0F, 6.0F)).build("coke_coal_block");
    public static final RegEntry MACHINE_FRAME_1 = new RegistryEntryBuilder().withBlockBuild(Block::new, MachineBlock.getDefaultSettings()).build("machine_frame_1");
    // MACHINES
    public static final RegEntry COAL_GENERATOR = new RegistryEntryBuilder().applyTemplate(RegistryTemplates.MACHINE).withBlockEntityBuild(null).withGuiBuild(ExtendedScreenHandlerType<CoalGeneratorGui>::new, CoalGeneratorGui::init).build("coal_generator");
    public static final RegEntry ORE_GROWER = new RegistryEntryBuilder().applyTemplate(RegistryTemplates.MACHINE).withBlockEntityBuild(null).withGuiBuild(ExtendedScreenHandlerType<OreGrowerGui>::new, OreGrowerGui::init).build("ore_grower");
    
    public static void register() {}
}
