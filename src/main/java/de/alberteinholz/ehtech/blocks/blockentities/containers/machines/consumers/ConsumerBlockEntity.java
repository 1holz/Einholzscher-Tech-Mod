package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.consumers;

import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigBehavior;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigType;

public abstract class ConsumerBlockEntity extends MachineBlockEntity {
	public ConsumerBlockEntity(RegistryEntry registryEntry) {
		super(registryEntry);
		((MachineDataProviderComponent) data).setConfigAvailability(new ConfigType[]{ConfigType.POWER}, new ConfigBehavior[]{ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
	}
}