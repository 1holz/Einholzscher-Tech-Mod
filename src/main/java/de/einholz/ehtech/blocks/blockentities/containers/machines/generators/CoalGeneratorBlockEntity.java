package de.einholz.ehtech.blocks.blockentities.containers.machines.generators;

import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent.ConfigBehavior;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent.Slot.Type;
import de.alberteinholz.ehmooshroom.recipes.AdvancedRecipe;
import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.blocks.components.machine.HeatDataComponent;
import net.minecraft.util.Identifier;

public class CoalGeneratorBlockEntity extends GeneratorBlockEntity {
    public CoalGeneratorBlockEntity() {
        this(RegistryHelper.getEntry(TechMod.HELPER.makeId("coal_generator")));
    }

    public CoalGeneratorBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        addComponent(TechMod.HELPER.makeId("coal_generator_input_inv_1"), new AdvancedInventoryComponent(new Type[] {Type.INPUT}, TechMod.HELPER.MOD_ID, new String[] {"input_coal"}));
        getConfigComp().setConfigAvailability(new Identifier[] {getFirstInputInvComp().getId()}, new ConfigBehavior[] {ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
        addComponent(TechMod.HELPER.makeId("coal_generator_heat_1"), new HeatDataComponent(1500.0));
    }

    public AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getImmutableComps().get(TechMod.HELPER.makeId("coal_generator_input_inv_1"));
    }

    public HeatDataComponent getFirstHeatComp() {
        return (HeatDataComponent) getImmutableComps().get(TechMod.HELPER.makeId("coal_generator_heat_1"));
    }

    @Override
    public boolean process() {
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineDataComp().getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            int generation = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (getFirstHeatComp().heat.getBarCurrent() - getFirstHeatComp().heat.getBarMinimum()) / (getFirstHeatComp().heat.getBarMaximum() - getFirstHeatComp().heat.getBarMinimum()) * 3 + 1));
            if (getMachineCapacitorComp().getCurrentEnergy() + generation <= getMachineCapacitorComp().getMaxEnergy()) getMachineCapacitorComp().generateEnergy(world, pos, generation);
            else return false;
        }
        getMachineDataComp().addProgress(recipe.timeModifier * getMachineDataComp().getSpeed());
        return true;
    }

    @Override
    public void task() {
        super.task();
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineDataComp().getRecipe(world);
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            getFirstHeatComp().addHeat(recipe.generates * getMachineDataComp().getSpeed() * getMachineDataComp().getEfficiency());
        }
    }

    @Override
    public void idle() {
        super.idle();
        if (getFirstHeatComp().heat.getBarCurrent() > getFirstHeatComp().heat.getBarMinimum()) {
            getFirstHeatComp().decreaseHeat();
        }
    }
}