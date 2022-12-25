package de.einholz.ehtech.block;

import de.einholz.ehmooshroom.block.ContainerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.Identifier;

public class MachineBlock extends ContainerBlock {
    public MachineBlock(Identifier id, BlockEntityTicker<? extends BlockEntity> ticker) {
        this(getDefaultSettings(), id, ticker);
    }
    
    public MachineBlock(FabricBlockSettings settings, Identifier id, BlockEntityTicker<? extends BlockEntity> ticker) {
        super(settings, id, ticker);
    }

    public static FabricBlockSettings getDefaultSettings() {
        return FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5, 10);
    }
}
