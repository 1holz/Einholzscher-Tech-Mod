package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.HeatStorage;
import de.einholz.ehtech.registry.Registry;
import de.einholz.ehtech.storage.MachineInv;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class CoalGeneratorBE extends MachineBE {
    public CoalGeneratorBE(BlockPos pos, BlockState state) {
        this(Registry.COAL_GENERATOR.BLOCK_ENTITY_TYPE, pos, state, null);
    }

    public CoalGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(TransferablesReg.ITEMS, CoalGeneratorInv.of());
        getStorageMgr().withStorage(TransferablesReg.HEAT, new HeatStorage());
    
        //getConfigComp().setConfigAvailability(new Identifier[] {getFirstInputInvComp().getId()}, new ConfigBehavior[] {ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    public HeatStorage getHeat() {
        return (HeatStorage) getStorageMgr().getStorageEntry(TransferablesReg.HEAT).storage;
    }

    /* TODO del
    public AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getImmutableComps().get(TechMod.HELPER.makeId("coal_generator_input_inv_1"));
    }

    public HeatDataComponent getFirstHeatComp() {
        return (HeatDataComponent) getImmutableComps().get(TechMod.HELPER.makeId("coal_generator_heat_1"));
    }
    */

    @Override
    public boolean process() {
        AdvRecipe recipe = getRecipe();
        /*
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            int generation = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * (getFirstHeatComp().heat.getBarCurrent() - getFirstHeatComp().heat.getBarMinimum()) / (getFirstHeatComp().heat.getBarMaximum() - getFirstHeatComp().heat.getBarMinimum()) * 3 + 1));
            if (getMachineCapacitorComp().getCurrentEnergy() + generation <= getMachineCapacitorComp().getMaxEnergy()) getMachineCapacitorComp().generateEnergy(world, pos, generation);
            else return false;
        }
        */
        setProgress(getRecipe().timeModifier * getSpeed());
        return true;
    }

    @Override
    public void task() {
        super.task();
        AdvRecipe recipe = getRecipe();
        /*
        if (recipe.generates != Double.NaN && recipe.generates > 0.0) {
            getFirstHeatComp().addHeat(recipe.generates * getMachineDataComp().getSpeed() * getMachineDataComp().getEfficiency());
        }
        */
    }

    @Override
    public void idle() {
        super.idle();
        if (!getHeat().isResourceBlank()) getHeat().decrease();
    }

    public static class CoalGeneratorInv extends MachineInv {
        public static final int SIZE = MachineInv.SIZE + 1;
        public static final int COAL_IN = MachineInv.SIZE + 0;

        public static InventoryStorage of() {
            return InventoryStorage.of(new CoalGeneratorInv(), null);
        }

        public CoalGeneratorInv() {
            this(0);
        }

        public CoalGeneratorInv(int add) {
            super(add + SIZE);
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            switch (slot) {
                case COAL_IN:
                    return true; // TODO check if part of recipe. probably requires data generators
                default:
                    return super.isValid(slot, stack);
            }
        }
    }
}
