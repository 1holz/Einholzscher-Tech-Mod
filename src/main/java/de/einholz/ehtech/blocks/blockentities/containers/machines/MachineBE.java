package de.einholz.ehtech.blocks.blockentities.containers.machines;

import java.util.Optional;

import de.einholz.ehmooshroom.container.AdvancedContainerBE;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent.SideConfigType;
import de.einholz.ehmooshroom.container.component.energy.EnergyComponent;
import de.einholz.ehmooshroom.container.component.item.ItemComponent;
import de.einholz.ehmooshroom.recipes.AdvancedRecipe;
import de.einholz.ehmooshroom.recipes.Ingrediets.ItemIngredient;
import de.einholz.ehmooshroom.registry.RegistryEntry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.blocks.components.machine.MachineComponent;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public abstract class MachineBE<T extends MachineBE<T>> extends AdvancedContainerBE<T> implements Tickable {
    public final BlockApiCache<EnergyComponent, Direction> ENERGY_CACHE;
    public final BlockApiCache<ItemComponent, SideConfigType> ITEM_INTERNAL_CACHE;
    public final BlockApiCache<MachineComponent, Void> MACHINE_CACHE;

    public MachineBE(RegistryEntry registryEntry) {
        super(registryEntry);
        ENERGY_CACHE =  createCache(EnergyComponent.ENERGY_LOOKUP);
        ITEM_INTERNAL_CACHE =  createCache(ItemComponent.ITEM_INTERNAL_LOOKUP);
        MACHINE_CACHE =  createCache(MachineComponent.MACHINE_LOOKUP);
    }

    @Override
    public void tick() {
        boolean isRunning = MACHINE_CACHE.find(null).getCur() > MACHINE_CACHE.find(null).getMin() && isActivated();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (MACHINE_CACHE.find(null).getCur() == MACHINE_CACHE.find(null).getMin()) start();
            if (process()) task();
            if (MACHINE_CACHE.find(null).getCur() == MACHINE_CACHE.find(null).getMax()) complete();
        } else idle();
        correct();
        markDirty();
    }

    public void transfer() {
        //TODO: only for early development replace with proper creative battery
        if (getMachineInvComp().getStack(getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("power_input"))).getItem().equals(Items.BEDROCK) && getMachineCapacitorComp().getCurrentEnergy() < getMachineCapacitorComp().getMaxEnergy()) getMachineCapacitorComp().generateEnergy(world, pos, getMachineCapacitorComp().getPreferredType().getMaximumTransferSize());
    }

    @SuppressWarnings("unchecked")
    public boolean checkForRecipe() {
        Optional<AdvancedRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<AdvancedRecipe>) recipeType, new InventoryWrapperPos(pos), world);
        MACHINE_CACHE.find(null).setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        AdvancedRecipe recipe = (AdvancedRecipe) MACHINE_CACHE.find(null).getRecipe(world);
        boolean consumerRecipe = (recipe.consumes == Double.NaN ? 0.0 : recipe.consumes) > (recipe.generates == Double.NaN ? 0.0 : recipe.generates);
        int consum = (int) (MACHINE_CACHE.find(null).getEfficiency() * MACHINE_CACHE.find(null).getSpeed() * recipe.consumes);
        if ((consumerRecipe && getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.TEST) == consum) || !consumerRecipe) {
            for (ItemIngredient ingredient : recipe.input.items) {
                int consumingLeft = ingredient.amount;
                for (Slot slot : getSlots(Type.INPUT)) {
                    if (ingredient.ingredient.contains(slot.stack.getItem()) && NbtHelper.matches(ingredient.tag, slot.stack.getTag(), true)) {
                        if (slot.stack.getCount() >= consumingLeft) {
                            slot.stack.decrement(consumingLeft);
                            break;
                        } else {
                            consumingLeft -= slot.stack.getCount();
                            slot.stack.setCount(0);;
                        }
                    }
                }
            }
            //TODO: Fluids
        }
    }

    public boolean process() {
        AdvancedRecipe recipe = (AdvancedRecipe) MACHINE_CACHE.find(null).getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generate = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (MACHINE_CACHE.find(null).getEfficiency() * MACHINE_CACHE.find(null).getSpeed() * recipe.consumes);
            if (getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.TEST) < consum) canConsum = false;
        }
        if (doGenerate) {
            generate = (int) (MACHINE_CACHE.find(null).getEfficiency() * MACHINE_CACHE.find(null).getSpeed() * recipe.generates);
            if (getMachineCapacitorComp().getCurrentEnergy() + generate > getMachineCapacitorComp().getMaxEnergy()) canGenerate = false;
        }
        if (doConsum) {
            if (canConsum && canGenerate) getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.PERFORM);
            else canProcess = false;
        }
        if (doGenerate) {
            if (canConsum && canGenerate) getMachineCapacitorComp().generateEnergy(world, pos, generate);
            else canProcess = false;
        }
        if (canProcess) MACHINE_CACHE.find(null).addProgress(recipe.timeModifier * MACHINE_CACHE.find(null).getSpeed());
        return canProcess;
    }

    public void task() {}

    public void complete() {
        cancel();
    }

    public void cancel() {
        MACHINE_CACHE.find(null).resetProgress();
        MACHINE_CACHE.find(null).resetRecipe();
    }

    public void idle() {}

    public void correct() {}

    public boolean isActivated() {
        short activationState = MACHINE_CACHE.find(null).getActivationState();
        if (activationState == 0) return true;
        else if(activationState == 1) return world.isReceivingRedstonePower(pos);
        else if(activationState == 2) return !world.isReceivingRedstonePower(pos);
        else return false;
    }
}
