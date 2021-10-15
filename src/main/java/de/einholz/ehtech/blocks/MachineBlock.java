package de.einholz.ehtech.blocks;

import de.einholz.ehmooshroom.block.ContainerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;

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
}