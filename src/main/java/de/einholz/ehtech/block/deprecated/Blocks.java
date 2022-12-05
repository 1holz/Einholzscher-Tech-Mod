package de.einholz.ehtech.block.deprecated;

import de.einholz.ehtech.block.MachineBlock;
/*
import de.einholz.ehmooshroom.registry.BlockReg;
import de.einholz.ehtech.TechMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
*/
import net.minecraft.util.Identifier;

@Deprecated
public class Blocks {
    /*
    // Normal blocks
    public static final Block CHARCOAL_BLOCK = registerBlock(TechMod.HELPER.makeId("charcoal_block"), FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(5.0F, 6.0F));
    public static final Block COKE_COAL_BLOCK = registerBlock(TechMod.HELPER.makeId("coke_coal_block"), FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(5.0F, 6.0F));
    public static final Block MACHINE_FRAME_1 = registerBlock(TechMod.HELPER.makeId("machine_frame_1"), MachineBlock.getDefaultSettings());
    // Machines
    public static final MachineBlock COAL_GENERATOR = registerMachine(TechMod.HELPER.makeId("coal_generator"));
    public static final MachineBlock ORE_GROWER = registerMachine(TechMod.HELPER.makeId("ore_grower"));
    */

    protected static MachineBlock registerMachine(Identifier id) {
        return null; //return (MachineBlock) registerRaw(id, new MachineBlock(id));
    }
}
