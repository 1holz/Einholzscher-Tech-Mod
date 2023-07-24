package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.registry.BlockRegistry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.MachineBlock;
import de.einholz.ehtech.block.entity.MachineBE;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;

public class BlockReg extends BlockRegistry {
    public static final Block CHARCOAL_BLOCK = new BlockReg()
            .register("charcoal_block", Block::new,
                    FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(5.0F, 6.0F))
            .get();
    public static final Block COKE_COAL_BLOCK = new BlockReg()
            .register("coke_coal_block", Block::new,
                    FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(5.0F, 6.0F))
            .get();
    public static final Block MACHINE_FRAME_1 = new BlockReg()
            .register("machine_frame_1", Block::new, MachineBlock.getDefaultSettings())
            .get();
    public static final MachineBlock COAL_GENERATOR = (MachineBlock) new BlockReg()
            .register("coal_generator")
            .get();
    public static final MachineBlock ORE_GROWER = (MachineBlock) new BlockReg()
            .register("ore_grower")
            .get();

    public BlockReg register(String name) {
        // idFactory() has to be called manually since id is not initialized at this time
        return (BlockReg) register(name, new MachineBlock(idFactory().apply(name), MachineBE::tick));
    }

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
