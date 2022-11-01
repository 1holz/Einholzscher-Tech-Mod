package de.einholz.ehtech.blocks;

import de.einholz.ehmooshroom.block.ContainerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class MachineBlock extends ContainerBlock {
    public MachineBlock(Identifier id) {
        this(getStandardFabricBlockSettings(), id);
    }
    
    public MachineBlock(FabricBlockSettings settings, Identifier id) {
        super(settings, id);
    }

    protected static FabricBlockSettings getStandardFabricBlockSettings() {
        return FabricBlockSettings.of(Material.METAL).requiresTool().strength(5, 10);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return Registry.BLOCK_ENTITY_TYPE.get(id).instantiate(pos, state);
    }
}
