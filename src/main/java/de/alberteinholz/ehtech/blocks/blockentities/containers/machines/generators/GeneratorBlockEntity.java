package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.generators;

import de.alberteinholz.ehmooshroom.MooshroomLib;
import de.alberteinholz.ehmooshroom.container.blockentity.AdvancedContainer;
import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent;
import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigBehavior;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigType;

public abstract class GeneratorBlockEntity extends AdvancedContainer {
    public GeneratorBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        ((ConfigDataComponent) comps.get(MooshroomLib.HELPER.makeId("config"))).setConfigAvailability();
        ((MachineDataProviderComponent) data).setConfigAvailability(new ConfigType[]{ConfigType.POWER}, new ConfigBehavior[]{ConfigBehavior.SELF_OUTPUT, ConfigBehavior.FOREIGN_OUTPUT}, null, true);
    }
}