package de.einholz.ehtech.blocks.blockentities.containers.machines;

import java.util.Optional;

import de.einholz.ehmooshroom.container.AdvancedContainerBE;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent.SideConfigBehavior;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent.SideConfigType;
import de.einholz.ehmooshroom.container.component.energy.EnergyComponent;
import de.einholz.ehmooshroom.container.component.item.ItemComponent;
import de.einholz.ehmooshroom.container.component.util.TransportingComponent;
import de.einholz.ehmooshroom.container.component.util.TransportingComponent.Action;
import de.einholz.ehmooshroom.recipes.AdvancedRecipe;
import de.einholz.ehmooshroom.recipes.Ingrediets.ItemIngredient;
import de.einholz.ehmooshroom.registry.RegistryEntry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.blocks.components.machine.MachineComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class MachineBE<T extends MachineBE<T>> extends AdvancedContainerBE<T> implements Tickable {
    public RecipeType<? extends Recipe<?>> recipeType;

    public MachineBE(RegistryEntry registryEntry) {
        super(registryEntry);
        recipeType = registryEntry.recipeType;
        createCache(EnergyComponent.ENERGY, EnergyComponent.ENERGY_LOOKUP);
        createCache(ItemComponent.ITEM_INTERNAL, ItemComponent.ITEM_INTERNAL_LOOKUP);
        createCache(MachineComponent.MACHINE, MachineComponent.MACHINE_LOOKUP);
    }

    @Override
    public void tick() {
        boolean isRunning = getCache(MachineComponent.MACHINE).find(null).getCur() > getCache(MachineComponent.MACHINE).find(null).getMin() && isActivated();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (getCache(MachineComponent.MACHINE).find(null).getCur() == getCache(MachineComponent.MACHINE).find(null).getMin()) start();
            if (process()) task();
            if (getCache(MachineComponent.MACHINE).find(null).getCur() == getCache(MachineComponent.MACHINE).find(null).getMax()) complete();
        } else idle();
        correct();
        markDirty();
    }

    public void transfer() {
        //TODO: only for early development replace with proper creative battery
        if (getCache(ItemComponent.ITEM_INTERNAL).find(null).getStack(0).getItem().equals(Items.BEDROCK) && ENERGY_CACHE.find(null).getCur() < ENERGY_CACHE.find(null).getMax()) ENERGY_CACHE.find(null).change(ENERGY_CACHE.find(null).getMaxTransfer(), Action.PERFORM, null);
        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.offset(dir);
            Direction targetDir = dir.getOpposite();
            AdvancedContainerBE<?> targetBE = (AdvancedContainerBE<?>) getWorld().getBlockEntity(targetPos);
            for (ComponentKey<?> key : getComponentContainer().keys()) {
                Component comp = (Component) key.get(this);
                Component targetComp = (Component) key.get(targetBE);
                if (!(comp instanceof TransportingComponent && comp instanceof TransportingComponent)) continue;
                Identifier id = key.getId();
                //@SuppressWarnings("unchecked")
                //TransportingComponent<Component> comp = (TransportingComponent<Component>) entry.getValue();
                //BlockComponentHook hook = BlockComponentHook.INSTANCE;
                ((TransportingComponent) comp).pull(targetComp, dir, Action.PERFORM, type)
                //if (comp instanceof InventoryComponent && hook.hasInvComponent(world, targetPos, targetDir)) comp.pull(hook.getInvComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                //if (comp instanceof TankComponent && hook.hasTankComponent(world, targetPos, targetDir)) comp.pull(hook.getTankComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                //if (comp instanceof CapacitorComponent && hook.hasCapComponent(world, targetPos, targetDir)) comp.pull(hook.getCapComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                //TODO push
                //if (comp instanceof InventoryComponent && hook.hasInvComponent(world, targetPos, targetDir)) comp.push(hook.getInvComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                //if (comp instanceof TankComponent && hook.hasTankComponent(world, targetPos, targetDir)) comp.push(hook.getTankComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                //if (comp instanceof CapacitorComponent && hook.hasCapComponent(world, targetPos, targetDir)) comp.push(hook.getCapComponent(world, targetPos, targetDir), dir, Action.PERFORM);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean checkForRecipe() {
        Optional<AdvancedRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<AdvancedRecipe>) recipeType, new InventoryWrapperPos(pos), world);
        getCache(MachineComponent.MACHINE).find(null).setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        AdvancedRecipe recipe = (AdvancedRecipe) getCache(MachineComponent.MACHINE).find(null).getRecipe(world);
        boolean consumerRecipe = (recipe.consumes == Double.NaN ? 0.0 : recipe.consumes) > (recipe.generates == Double.NaN ? 0.0 : recipe.generates);
        int consum = (int) (getCache(MachineComponent.MACHINE).find(null).getEfficiency() * getCache(MachineComponent.MACHINE).find(null).getSpeed() * recipe.consumes);
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
        AdvancedRecipe recipe = (AdvancedRecipe) getCache(MachineComponent.MACHINE).find(null).getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generate = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (getCache(MachineComponent.MACHINE).find(null).getEfficiency() * getCache(MachineComponent.MACHINE).find(null).getSpeed() * recipe.consumes);
            if (getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.TEST) < consum) canConsum = false;
        }
        if (doGenerate) {
            generate = (int) (getCache(MachineComponent.MACHINE).find(null).getEfficiency() * getCache(MachineComponent.MACHINE).find(null).getSpeed() * recipe.generates);
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
        if (canProcess) getCache(MachineComponent.MACHINE).find(null).addProgress(recipe.timeModifier * getCache(MachineComponent.MACHINE).find(null).getSpeed());
        return canProcess;
    }

    public void task() {}

    public void complete() {
        cancel();
    }

    public void cancel() {
        getCache(MachineComponent.MACHINE).find(null).resetProgress();
        getCache(MachineComponent.MACHINE).find(null).resetRecipe();
    }

    public void idle() {}

    public void correct() {}

    public boolean isActivated() {
        short activationState = getCache(MachineComponent.MACHINE).find(null).getActivationState();
        if (activationState == 0) return true;
        else if(activationState == 1) return world.isReceivingRedstonePower(pos);
        else if(activationState == 2) return !world.isReceivingRedstonePower(pos);
        else return false;
    }
}
