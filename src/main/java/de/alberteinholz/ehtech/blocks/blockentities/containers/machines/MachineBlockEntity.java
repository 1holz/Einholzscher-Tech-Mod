package de.alberteinholz.ehtech.blocks.blockentities.containers.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.alberteinholz.ehmooshroom.MooshroomLib;
import de.alberteinholz.ehmooshroom.container.AdvancedContainerBlockEntity;
import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent;
import de.alberteinholz.ehmooshroom.container.component.energy.AdvancedCapacitorComponent;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent.Slot;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent.Slot.Type;
import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
import de.alberteinholz.ehtech.blocks.components.machine.MachineDataComponent;
import de.alberteinholz.ehtech.blocks.components.machine.MachineDataComponent.ActivationState;
import de.alberteinholz.ehtech.blocks.directionals.containers.machines.MachineBlock;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;
import de.alberteinholz.ehtech.blocks.recipes.Input.BlockIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.DataIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.EntityIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.FluidIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.ItemIngredient;
import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.energy.type.EnergyType;
import io.github.cottonmc.component.energy.type.EnergyTypes;
import io.github.cottonmc.component.fluid.TankComponent;
import io.github.cottonmc.component.item.InventoryComponent;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class MachineBlockEntity extends AdvancedContainerBlockEntity implements Tickable {
    public int powerBilanz = 0;
    public int lastPower = 0;
    public RecipeType<? extends Recipe<?>> recipeType;

    public MachineBlockEntity(RegistryEntry registryEntry) {
        this(registryEntry, EnergyTypes.ULTRA_LOW_VOLTAGE);
    }

    public MachineBlockEntity(RegistryEntry registryEntry, EnergyType energyType) {
        super(registryEntry);
        this.recipeType = registryEntry.recipeType;
        addComponent(TechMod.HELPER.makeId("data_machine"), new MachineDataComponent());
        addComponent(TechMod.HELPER.makeId("capacitor_machine"), new AdvancedCapacitorComponent(energyType));
        addComponent(TechMod.HELPER.makeId("inventory_machine"), new AdvancedInventoryComponent(new Type[] {Type.OTHER, Type.OTHER, Type.OTHER, Type.OTHER}, TechMod.HELPER.MOD_ID, new String[] {"power_input", "power_output", "upgrade", "network"}));
    }

    //convenience access to some comps
    public AdvancedCapacitorComponent getMachineCapacitorComp() {
        return (AdvancedCapacitorComponent) getImmutableComps().get(MooshroomLib.HELPER.makeId("capacitor_machine"));
    }
    
    public AdvancedInventoryComponent getMachineInvComp() {
        return (AdvancedInventoryComponent) getImmutableComps().get(MooshroomLib.HELPER.makeId("inventory_machine"));
    }
    
    public MachineDataComponent getMachineDataComp() {
        return (MachineDataComponent) getImmutableComps().get(MooshroomLib.HELPER.makeId("data_machine"));
    }

    //type can be null to don't filter
    public List<Slot> getSlots(Type type) {
        List<Slot> list = new ArrayList<>();
        for (Component comp : comps.values()) {
            if (!(comp instanceof AdvancedInventoryComponent)) continue;
            list.addAll(((AdvancedInventoryComponent) comp).getSlots(type));
        }
        return list;
    }

    @Override
    public void tick() {
        boolean isRunning = getMachineDataComp().progress.getBarCurrent() > getMachineDataComp().progress.getBarMinimum() && isActivated();
        powerBilanz = getMachineCapacitorComp().getCurrentEnergy() - lastPower;
        lastPower = getMachineCapacitorComp().getCurrentEnergy();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (getMachineDataComp().progress.getBarCurrent() == getMachineDataComp().progress.getBarMinimum()) start();
            if (process()) task();
            if (getMachineDataComp().progress.getBarCurrent() == getMachineDataComp().progress.getBarMaximum()) finish();
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
        Optional<MachineRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<MachineRecipe>) recipeType, new InventoryWrapper(pos), world);
        getMachineDataComp().setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        MachineRecipe recipe = (MachineRecipe) getMachineDataComp().getRecipe(world);
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
        MachineRecipe recipe = (MachineRecipe) getMachineDataComp().getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generation = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * recipe.consumes);
            if (!(getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.TEST) == consum)) canConsum = false;
        }
        if (doGenerate) {
            generation = (int) (getMachineDataComp().getEfficiency() * getMachineDataComp().getSpeed() * recipe.generates);
            if (!(getMachineCapacitorComp().getCurrentEnergy() + generation <= getMachineCapacitorComp().getMaxEnergy())) canGenerate = false;
        }
        if (doConsum) {
            if (canConsum && canGenerate) getMachineCapacitorComp().extractEnergy(getMachineCapacitorComp().getPreferredType(), consum, ActionType.PERFORM);
            else canProcess = false;
        }
        if (doGenerate) {
            if (canConsum && canGenerate) getMachineCapacitorComp().generateEnergy(world, pos, generation);
            else canProcess = false;
        }
        if (canProcess) getMachineDataComp().addProgress(recipe.timeModifier * getMachineDataComp().getSpeed());
        return canProcess;
    }

    public void task() {}

    public void finish() {
        cancle();
    }

    public void cancle() {
        getMachineDataComp().resetProgress();
        getMachineDataComp().resetRecipe();
    }

    public void idle() {}

    public void correct() {}

    public boolean containsItemIngredients(ItemIngredient... ingredients) {
        boolean bl = true;
        //XXX:currently not available will change back later
        /*
        for (ItemIngredient ingredient : ingredients) {
            if (!getCombinedInvComp().containsInput(ingredient)) {
                bl = false;
                break;
            } 
        }
        */
        return bl;
    }

    public boolean containsFluidIngredients(FluidIngredient... ingredients) {
        boolean bl = true;
        for (FluidIngredient ingredient : ingredients) {
            TechMod.LOGGER.wip("Containment Check for " + ingredient);
            //TODO:fluid
        }
        return bl;
    }

    //only by overriding
    public boolean containsBlockIngredients(BlockIngredient... ingredients) {
        return true;
    }

    //only by overriding
    public boolean containsEntityIngredients(EntityIngredient... ingredients) {
        return true;
    }

    //only by overriding
    public boolean containsDataIngredients(DataIngredient... ingredients) {
        return true;
    }

    public boolean isActivated() {
        ActivationState activationState = getMachineDataComp().getActivationState();
        if (activationState == ActivationState.ALWAYS_ON) return true;
        else if(activationState == ActivationState.REDSTONE_ON) return world.isReceivingRedstonePower(pos);
        else if(activationState == ActivationState.REDSTONE_OFF) return !world.isReceivingRedstonePower(pos);
        else return false;
    }

    @Override
    public void fromTag(BlockState state,CompoundTag tag) {
        super.fromTag(state, tag);
        if (world != null && tag.contains("Capacitor", NbtType.COMPOUND)) getMachineCapacitorComp().fromTag(tag.getCompound("Capacitor"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (world != null) {
            CompoundTag capacitorTag = new CompoundTag();
            getMachineCapacitorComp().toTag(capacitorTag);
            if (!capacitorTag.isEmpty()) tag.put("Capacitor", capacitorTag);
        }
        return tag;
    }

    public SideConfigScreenHandlerFactory getSideConfigScreenHandlerFactory() {
        return new SideConfigScreenHandlerFactory();
    }

    public class SideConfigScreenHandlerFactory implements ExtendedScreenHandlerFactory {
		@Override
		public Text getDisplayName() {
			return MachineBlockEntity.this.getDisplayName();
		}

		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            writeScreenOpeningData((ServerPlayerEntity) player, buf);
            return RegistryHelper.getEntry(TechMod.HELPER.makeId("machine_config")).clientHandlerFactory.create(syncId, inv, buf);
		}

		@Override
		public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
			buf.writeBlockPos(pos);
		}
    }
}