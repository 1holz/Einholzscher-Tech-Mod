package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.generators;

import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.blocks.components.container.ContainerInventoryComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.CoalGeneratorDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigBehavior;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigType;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;
import de.alberteinholz.ehtech.util.Helper;

public class CoalGeneratorBlockEntity extends GeneratorBlockEntity {
    public CoalGeneratorBlockEntity() {
        this(RegistryHelper.getEntry(Helper.makeId("coal_generator")));
    }

    public CoalGeneratorBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        inventory.stacks.put("coal_input", new ContainerInventoryComponent.Slot(ContainerInventoryComponent.Slot.Type.INPUT));
        ((MachineDataProviderComponent) data).setConfigAvailability(new ConfigType[]{ConfigType.ITEM}, new ConfigBehavior[]{ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    @Override
    public boolean process() {
        CoalGeneratorDataProviderComponent data = (CoalGeneratorDataProviderComponent) this.data;
        MachineRecipe recipe = (MachineRecipe) data.getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            int generation = (int) (data.getEfficiency() * data.getSpeed() * (data.getEfficiency() * data.getSpeed() * (data.heat.getBarCurrent() - data.heat.getBarMinimum()) / (data.heat.getBarMaximum() - data.heat.getBarMinimum()) * 3 + 1));
            if (capacitor.getCurrentEnergy() + generation <= capacitor.getMaxEnergy()) {
                capacitor.generateEnergy(world, pos, generation);
            } else {
                return false;
            }
        }
        data.addProgress(recipe.timeModifier * data.getSpeed());
        return true;
    }

    @Override
    public void task() {
        super.task();
        CoalGeneratorDataProviderComponent data = (CoalGeneratorDataProviderComponent) this.data;
        MachineRecipe recipe = (MachineRecipe) data.getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            data.addHeat(recipe.generates * data.getSpeed() * data.getEfficiency());
        }
    }

    @Override
    public void idle() {
        super.idle();
        CoalGeneratorDataProviderComponent data = (CoalGeneratorDataProviderComponent) this.data;
        if (data.heat.getBarCurrent() > data.heat.getBarMinimum()) {
            data.decreaseHeat();
        }
    }

    @Override
    protected CoalGeneratorDataProviderComponent initializeDataProviderComponent() {
        return new CoalGeneratorDataProviderComponent();
    }
}