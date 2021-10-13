package de.einholz.ehtech.blocks;

import de.einholz.ehmooshroom.block.ContainerBlock;
import de.einholz.ehmooshroom.registry.RegistryHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class MachineBlock extends ContainerBlock implements BlockEntityProvider {
    public MachineBlock(Identifier id) {
        this(getStandardFabricBlockSettings(), id);
    }
    
    public MachineBlock(FabricBlockSettings settings, Identifier id) {
        super(settings, id);
    }

    protected static FabricBlockSettings getStandardFabricBlockSettings() {
        return FabricBlockSettings.of(Material.METAL).requiresTool().strength(5, 10);
    }
}