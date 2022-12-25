/*
package de.einholz.ehtech.blocks.blockentities.containers.machines;

import java.util.Optional;

import de.einholz.ehmooshroom.container.AdvancedContainerBE;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent.SideConfigBehavior;
import de.einholz.ehmooshroom.container.component.config.SideConfigComponent.SideConfigType;
import de.einholz.ehmooshroom.container.component.energy.EnergyComponent;
import de.einholz.ehmooshroom.container.component.energy.SimpleEnergyComponent;
import de.einholz.ehmooshroom.container.component.item.ItemComponent;
import de.einholz.ehmooshroom.container.component.item.SimpleItemComponent;
import de.einholz.ehmooshroom.container.component.util.TransportingComponent;
import de.einholz.ehmooshroom.recipes.AdvancedRecipe;
import de.einholz.ehmooshroom.recipes.Ingrediets.ItemIngredient;
import de.einholz.ehmooshroom.registry.RegistryEntry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.blocks.components.machine.MachineComponent;
import de.einholz.ehtech.blocks.components.machine.SimpleMachineComponent;
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
    public int powerBalance = 0;
    public int lastPower = 0;
    public RecipeType<? extends Recipe<?>> recipeType;

    public MachineBE(RegistryEntryBuilder registryEntry) {
        super(registryEntry);
        recipeType = registryEntry.recipeType;
        addComponent(this, MachineComponent.MACHINE, SimpleMachineComponent::new, null);
        addComponent(this, EnergyComponent.ENERGY, SimpleEnergyComponent::new, new Integer[] {160000, 4});
        addComponent(this, ItemComponent.ITEM, SimpleItemComponent::new, new Integer[] {4, new SlotFactory()}); //, new AdvancedInventoryComponent(new Type[] {Type.OTHER, Type.OTHER, Type.OTHER, Type.OTHER}, TechMod.HELPER.MOD_ID, new String[] {"power_input", "power_output", "upgrade", "network"}));
        createCache(MachineComponent.MACHINE, MachineComponent.MACHINE_LOOKUP);
        createCache(EnergyComponent.ENERGY, EnergyComponent.ENERGY_LOOKUP);
        createCache(ItemComponent.ITEM, ItemComponent.ITEM_LOOKUP);
    }

    //conveniant access to some comps
    public MachineComponent getMachineComp() {
        return getCache(MachineComponent.MACHINE).find(null);
    }

    @SuppressWarnings("unchecked")
    public EnergyComponent getEnergyComp(Direction dir) {
        return ((BlockApiCache<EnergyComponent, Direction>) getCache(EnergyComponent.ENERGY)).find(dir);
    }

    @SuppressWarnings("unchecked")
    public ItemComponent getItemInternalComp(SideConfigType type) {
        return ((BlockApiCache<ItemComponent, SideConfigType>) getCache(ItemComponent.ITEM)).find(type);
    }

    @Override
    public void tick() {
        boolean isRunning = getMachineComp().getCur() > getMachineComp().getMin() && isActivated();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (getMachineComp().getCur() == getMachineComp().getMin()) start();
            if (process()) task();
            if (getMachineComp().getCur() == getMachineComp().getMax()) complete();
        } else idle();
        correct();
        markDirty();
    }

    public void transfer() {
        //TODO: only for early development replace with proper creative battery
        if (getCache(ItemComponent.ITEM).find(null).getStack(0).getItem().equals(Items.BEDROCK) && ENERGY_CACHE.find(null).getCur() < ENERGY_CACHE.find(null).getMax()) ENERGY_CACHE.find(null).change(ENERGY_CACHE.find(null).getMaxTransfer(), Action.PERFORM, null);
        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.offset(dir);
            Direction targetDir = dir.getOpposite();
            AdvancedContainerBE<?> targetBE = (AdvancedContainerBE<?>) getWorld().getBlockEntity(targetPos);
            for (ComponentKey<?> key : getComponentContainer().keys()) {
                Component comp = (Component) key.get(this);
                Component targetComp = (Component) key.get(targetBE);
                if (!(comp instanceof TransportingComponent && targetComp instanceof TransportingComponent)) continue;
                Identifier id = key.getId();
                //@SuppressWarnings("unchecked")
                //TransportingComponent<Component> comp = (TransportingComponent<Component>) entry.getValue();
                //BlockComponentHook hook = BlockComponentHook.INSTANCE;
                ((TransportingComponent<?>) comp).pull((TransportingComponent<?>) targetComp, dir);
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

    @SuppressWarnings("unchecked")
    public boolean checkForRecipe() {
        Optional<AdvancedRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<AdvancedRecipe>) recipeType, new InventoryWrapperPos(pos), world);
        getMachineComp().setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineComp().getRecipe(world);
        boolean consumingRecipe = (recipe.consumes == Double.NaN ? 0.0 : recipe.consumes) > (recipe.generates == Double.NaN ? 0.0 : recipe.generates);
        int consum = (int) (getMachineComp().getEfficiency() * getMachineComp().getSpeed() * recipe.consumes);
        if ((consumingRecipe && getEnergyComp(null).getSpace() == consum) || !consumingRecipe) {
            for (ItemIngredient ingredient : recipe.input.items) {
                int consumingLeft = ingredient.amount;
                for (Slot slot : getSlots(1)) {
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
            //TODO: Fluids etc.
        }
    }

    public boolean process() {
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineComp().getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generate = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (getMachineComp().getEfficiency() * getMachineComp().getSpeed() * recipe.consumes);
            if (getEnergyComp(null).extractEnergy(getEnergyComp(null).getPreferredType(), consum, ActionType.TEST) < consum) canConsum = false;
        }
        if (doGenerate) {
            generate = (int) (getMachineComp().getEfficiency() * getMachineComp().getSpeed() * recipe.generates);
            if (getEnergyComp(null).getCurrentEnergy() + generate > getEnergyComp(null).getMaxEnergy()) canGenerate = false;
        }
        if (doConsum) {
            if (canConsum && canGenerate) getEnergyComp(null).extractEnergy(getEnergyComp(null).getPreferredType(), consum, ActionType.PERFORM);
            else canProcess = false;
        }
        if (doGenerate) {
            if (canConsum && canGenerate) getEnergyComp(null).generateEnergy(world, pos, generate);
            else canProcess = false;
        }
        if (canProcess) getMachineComp().addProgress(recipe.timeModifier * getMachineComp().getSpeed());
        return canProcess;
    }

    public void task() {}

    public void complete() {
        cancel();
    }

    public void cancel() {
        getMachineComp().resetProgress();
        getMachineComp().resetRecipe();
    }

    public void idle() {}

    public void correct() {}

    public boolean isActivated() {
        short activationState = getMachineComp().getActivationState();
        if (activationState == 0) return true;
        else if(activationState == 1) return world.isReceivingRedstonePower(pos);
        else if(activationState == 2) return !world.isReceivingRedstonePower(pos);
        else return false;
    }
}
*/
