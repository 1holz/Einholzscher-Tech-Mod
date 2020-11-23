package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.generators;

import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent.ConfigBehavior;
import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;

public class CoalGeneratorBlockEntity extends GeneratorBlockEntity {
    public CoalGeneratorBlockEntity() {
        this(RegistryHelper.getEntry(Helper.makeId("coal_generator")));
    }

    public CoalGeneratorBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        inventory.addSlots(Type.INPUT);
        getConfigComp().setConfigAvailability(new ConfigType[]{ConfigType.ITEM}, new ConfigBehavior[]{ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    @Override
    public boolean process() {
        MachineRecipe recipe = (MachineRecipe) getMachineDataComp().getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            int generation = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (data.heat.getBarCurrent() - data.heat.getBarMinimum()) / (data.heat.getBarMaximum() - data.heat.getBarMinimum()) * 3 + 1));
            if (getMachineCapacitorComp().getCurrentEnergy() + generation <= getMachineCapacitorComp().getMaxEnergy()) getMachineCapacitorComp().generateEnergy(world, pos, generation);
            else return false;
        }
        getMachineDataComp().addProgress(recipe.timeModifier * getMachineDataComp().getSpeed());
        return true;
    }

    @Override
    public void task() {
        super.task();
        MachineRecipe recipe = (MachineRecipe) getMachineDataComp().getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            data.addHeat(recipe.generates * getMachineDataComp().getSpeed() * getMachineDataComp().getEfficiency());
        }
    }

    @Override
    public void idle() {
        super.idle();
        if (data.heat.getBarCurrent() > data.heat.getBarMinimum()) {
            data.decreaseHeat();
        }
    }
}