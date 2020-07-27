package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.generators;

import de.alberteinholz.ehmooshroom.registry.BlockRegistryEntry;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigBehavior;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigType;

public abstract class GeneratorBlockEntity extends MachineBlockEntity {
    public GeneratorBlockEntity(BlockRegistryEntry registryEntry) {
        super(registryEntry);
        ((MachineDataProviderComponent) data).setConfigAvailability(new ConfigType[]{ConfigType.POWER}, new ConfigBehavior[]{ConfigBehavior.SELF_OUTPUT, ConfigBehavior.FOREIGN_OUTPUT}, null, true);
    }
}