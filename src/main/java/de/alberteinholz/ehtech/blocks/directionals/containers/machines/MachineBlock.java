package de.alberteinholz.ehtech.blocks.directionals.containers.machines;

import java.util.Set;

import de.alberteinholz.ehmooshroom.registry.BlockRegistryHelper;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.alberteinholz.ehtech.blocks.directionals.containers.ContainerBlock;
import io.github.cottonmc.component.UniversalComponents;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class MachineBlock extends ContainerBlock implements BlockEntityProvider {
    public MachineBlock(Identifier id) {
        this(getStandardFabricBlockSettings(), id);
    }
    
    public MachineBlock(FabricBlockSettings settings, Identifier id) {
        super(settings, id);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return BlockRegistryHelper.BLOCKS.get(id).blockEntityType.instantiate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T getComponent(BlockView blockView, BlockPos pos, ComponentType<T> type, Direction side) {
        if(type == UniversalComponents.CAPACITOR_COMPONENT) {
            return (T) ((MachineBlockEntity) blockView.getBlockEntity(pos)).capacitor;
        } else {
            return super.getComponent(blockView, pos, type, side);
        }
    }

    @Override
    public Set<ComponentType<?>> getComponentTypes(BlockView blockView, BlockPos pos, Direction side) {
        Set<ComponentType<?>> set = super.getComponentTypes(blockView, pos, side);
        set.add(UniversalComponents.CAPACITOR_COMPONENT);
        return set;
    }

    protected static FabricBlockSettings getStandardFabricBlockSettings() {
        return FabricBlockSettings.of(Material.METAL).strength(5, 10);
    }
}