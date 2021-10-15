package de.einholz.ehtech.blocks.blockentities.containers.machines;

import de.einholz.ehmooshroom.container.AdvancedContainerBE;
import de.einholz.ehmooshroom.registry.RegistryEntry;
import net.minecraft.util.Tickable;

public abstract class MachineBE<T> extends AdvancedContainerBE implements Tickable {

    public MachineBE(RegistryEntry registryEntry) {
        super(registryEntry);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void tick() {
        boolean isRunning = getMachineDataComp().progress.getBarCurrent() > getMachineDataComp().progress.getBarMinimum() && isActivated();
        powerBalance = getMachineCapacitorComp().getCurrentEnergy() - lastPower;
        lastPower = getMachineCapacitorComp().getCurrentEnergy();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (getMachineDataComp().progress.getBarCurrent() == getMachineDataComp().progress.getBarMinimum()) start();
            if (process()) task();
            if (getMachineDataComp().progress.getBarCurrent() == getMachineDataComp().progress.getBarMaximum()) complete();
        } else idle();
        correct();
        markDirty();
    }

    @Override
    public void transfer() {
        super.transfer();
        //TODO: only for early development replace with proper creative battery
        if (getMachineInvComp().getStack(getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("power_input"))).getItem().equals(Items.BEDROCK) && getMachineCapacitorComp().getCurrentEnergy() < getMachineCapacitorComp().getMaxEnergy()) getMachineCapacitorComp().generateEnergy(world, pos, getMachineCapacitorComp().getPreferredType().getMaximumTransferSize());
    }

    @SuppressWarnings("unchecked")
    public boolean checkForRecipe() {
        Optional<AdvancedRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<AdvancedRecipe>) recipeType, new InventoryWrapperPos(pos), world);
        getMachineDataComp().setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineDataComp().getRecipe(world);
        boolean consumerRecipe = (recipe.consumes == Double.NaN ? 0.0 : recipe.consumes) > (recipe.generates == Double.NaN ? 0.0 : recipe.generates);
        int consum = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * recipe.consumes);
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
        AdvancedRecipe recipe = (AdvancedRecipe) getMachineDataComp().getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generate = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * recipe.consumes);
            if (getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.TEST) < consum) canConsum = false;
        }
        if (doGenerate) {
            generate = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * recipe.generates);
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
        if (canProcess) getMachineDataComp().addProgress(recipe.timeModifier * getMachineDataComp().getSpeed());
        return canProcess;
    }

    public void task() {}

    public void complete() {
        cancel();
    }

    public void cancel() {
        getMachineDataComp().resetProgress();
        getMachineDataComp().resetRecipe();
    }

    public void idle() {}

    public void correct() {}
}
