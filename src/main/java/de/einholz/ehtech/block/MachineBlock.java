package de.einholz.ehtech.block;

import de.einholz.ehmooshroom.block.ContainerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;

public class MachineBlock extends ContainerBlock {
    public MachineBlock(Identifier id) {
        this(getDefaultSettings(), id);
    }
    
    public MachineBlock(FabricBlockSettings settings, Identifier id) {
        super(settings, id);
    }

    public static FabricBlockSettings getDefaultSettings() {
        return FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5, 10);
    }
}
