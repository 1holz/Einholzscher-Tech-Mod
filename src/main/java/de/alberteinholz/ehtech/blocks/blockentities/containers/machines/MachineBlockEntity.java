package de.alberteinholz.ehtech.blocks.blockentities.containers.machines;

import java.util.Optional;

import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
import de.alberteinholz.ehtech.blocks.blockentities.containers.ContainerBlockEntity;
import de.alberteinholz.ehtech.blocks.components.container.ContainerInventoryComponent;
import de.alberteinholz.ehtech.blocks.components.container.InventoryWrapper;
import de.alberteinholz.ehtech.blocks.components.container.ContainerInventoryComponent.Slot;
import de.alberteinholz.ehtech.blocks.components.container.ContainerInventoryComponent.Slot.Type;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineCapacitorComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ActivationState;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigBehavior;
import de.alberteinholz.ehtech.blocks.components.container.machine.MachineDataProviderComponent.ConfigType;
import de.alberteinholz.ehtech.blocks.directionals.containers.machines.MachineBlock;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;
import de.alberteinholz.ehtech.blocks.recipes.Input.BlockIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.DataIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.EntityIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.FluidIngredient;
import de.alberteinholz.ehtech.blocks.recipes.Input.ItemIngredient;
import de.alberteinholz.ehtech.util.Helper;
import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.energy.type.EnergyTypes;
import io.github.cottonmc.component.fluid.TankComponent;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
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
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class MachineBlockEntity extends ContainerBlockEntity implements Tickable {
    public MachineCapacitorComponent capacitor = initializeCapacitorComponent();
    public int powerBilanz = 0;
    public int lastPower = capacitor.getCurrentEnergy();

    public MachineBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        capacitor.setDataProvider((MachineDataProviderComponent) data);
        inventory.stacks.put("power_input", new ContainerInventoryComponent.Slot(ContainerInventoryComponent.Slot.Type.OTHER));
        inventory.stacks.put("power_output", new ContainerInventoryComponent.Slot(ContainerInventoryComponent.Slot.Type.OTHER));
        inventory.stacks.put("upgrade", new ContainerInventoryComponent.Slot(ContainerInventoryComponent.Slot.Type.OTHER));
        inventory.stacks.put("network", new ContainerInventoryComponent.Slot(ContainerInventoryComponent.Slot.Type.OTHER));
    }

    @Override
    public void tick() {
        MachineDataProviderComponent data = (MachineDataProviderComponent) this.data;
        boolean isRunning = data.progress.getBarCurrent() > data.progress.getBarMinimum() && isActivated();
        powerBilanz = capacitor.getCurrentEnergy() - lastPower;
        lastPower = capacitor.getCurrentEnergy();
        transfer();
        if (!isRunning && isActivated()) isRunning = checkForRecipe();
        if (isRunning) {
            if (data.progress.getBarCurrent() == data.progress.getBarMinimum()) start();
            if (process()) task();
            if (data.progress.getBarCurrent() == data.progress.getBarMaximum()) finish();
        } else idle();
        correct();
        markDirty();
    }

    //TODO: make shorter with interface transferable
    public void transfer() {
        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.offset(dir);
            Block targetBlock = world.getBlockState(targetPos).getBlock();
            BlockEntity targetBlockEntity = world.getBlockEntity(targetPos);
            if (targetBlock instanceof BlockComponentProvider && ((BlockComponentProvider) targetBlock).hasComponent(world, targetPos, UniversalComponents.INVENTORY_COMPONENT, dir.getOpposite()) || targetBlockEntity instanceof Inventory) {
                Inventory foreignInv = ((BlockComponentProvider) targetBlock).hasComponent(world, targetPos, UniversalComponents.INVENTORY_COMPONENT, dir.getOpposite()) ? new InventoryWrapper(((BlockComponentProvider) targetBlock).getComponent(world, targetPos, UniversalComponents.INVENTORY_COMPONENT, dir.getOpposite())) : (Inventory) targetBlockEntity;
                InventoryWrapper ownInv = new InventoryWrapper(inventory);
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.ITEM, ConfigBehavior.SELF_INPUT, dir)) ContainerInventoryComponent.move(foreignInv, ownInv, 1, dir, ActionType.PERFORM);
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.ITEM, ConfigBehavior.SELF_OUTPUT, dir)) ContainerInventoryComponent.move(ownInv, foreignInv, 1, dir, ActionType.PERFORM);
            }
            //TODO: Fluid
            if (targetBlock instanceof BlockComponentProvider && ((BlockComponentProvider) targetBlock).hasComponent(world, targetPos, UniversalComponents.TANK_COMPONENT, dir.getOpposite())) {
                @SuppressWarnings("unused")
                TankComponent tank = ((BlockComponentProvider) targetBlock).getComponent(world, targetPos, UniversalComponents.TANK_COMPONENT, dir.getOpposite());
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.FLUID, ConfigBehavior.SELF_INPUT, dir)); //TODO:fluid
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.FLUID, ConfigBehavior.SELF_OUTPUT, dir)); //TODO:fluid
            }
            if (targetBlock instanceof BlockComponentProvider && ((BlockComponentProvider) targetBlock).hasComponent(world, targetPos, UniversalComponents.CAPACITOR_COMPONENT, dir.getOpposite())) {
                MachineCapacitorComponent cap = (MachineCapacitorComponent) ((MachineBlock) targetBlock).getComponent(world, targetPos, UniversalComponents.CAPACITOR_COMPONENT, null);
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.POWER, ConfigBehavior.SELF_INPUT, dir)) MachineCapacitorComponent.move(cap, capacitor, capacitor.getPreferredType(), dir, ActionType.PERFORM); //capacitor.pull(cap, ActionType.PERFORM, dir);
                if (((MachineDataProviderComponent) data).allowsConfig(ConfigType.POWER, ConfigBehavior.SELF_OUTPUT, dir)) MachineCapacitorComponent.move(capacitor, cap, capacitor.getPreferredType(), dir, ActionType.PERFORM); //capacitor.push(cap, ActionType.PERFORM, dir);
            }
            //TODO: only for early development replace with proper creative battery
            if (inventory.getStack("power_input").getItem() == Items.BEDROCK && capacitor.getCurrentEnergy() < capacitor.getMaxEnergy()) capacitor.generateEnergy(world, pos, 4);
        }
    }

    public boolean checkForRecipe() {
        Optional<MachineRecipe> optional = world.getRecipeManager().getFirstMatch((RecipeType<MachineRecipe>) registryEntry.recipeType, new InventoryWrapper(pos), world);
        ((MachineDataProviderComponent) this.data).setRecipe(optional.orElse(null));
        return optional.isPresent();
    }

    public void start() {
        MachineDataProviderComponent data = (MachineDataProviderComponent) this.data;
        MachineRecipe recipe = (MachineRecipe) data.getRecipe(world);
        boolean consumerRecipe = (recipe.consumes == Double.NaN ? 0.0 : recipe.consumes) > (recipe.generates == Double.NaN ? 0.0 : recipe.generates);
        int consum = (int) (data.getEfficiency() * data.getSpeed() * recipe.consumes);
        if ((consumerRecipe && capacitor.extractEnergy(capacitor.getPreferredType(), consum, ActionType.TEST) == consum) || !consumerRecipe) {
            for (ItemIngredient ingredient : recipe.input.items) {
                int consumingLeft = ingredient.amount;
                for (Slot slot : inventory.stacks.values()) {
                    if (slot.type == Type.INPUT && ingredient.ingredient.contains(slot.stack.getItem()) && NbtHelper.matches(ingredient.tag, slot.stack.getTag(), true)) {
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
        MachineDataProviderComponent data = (MachineDataProviderComponent) this.data;
        MachineRecipe recipe = (MachineRecipe) data.getRecipe(world);
        boolean doConsum = recipe.consumes != Double.NaN && recipe.consumes > 0.0;
        boolean canConsum = true;
        int consum = 0;
        boolean doGenerate = recipe.generates != Double.NaN && recipe.generates > 0.0;
        boolean canGenerate = true;
        int generation = 0;
        boolean canProcess = true;
        if (doConsum) {
            consum = (int) (data.getEfficiency() * data.getSpeed() * recipe.consumes);
            if (!(capacitor.extractEnergy(capacitor.getPreferredType(), consum, ActionType.TEST) == consum)) canConsum = false;
        }
        if (doGenerate) {
            generation = (int) (data.getEfficiency() * data.getSpeed() * recipe.generates);
            if (!(capacitor.getCurrentEnergy() + generation <= capacitor.getMaxEnergy())) canGenerate = false;
        }
        if (doConsum) {
            if (canConsum && canGenerate) capacitor.extractEnergy(capacitor.getPreferredType(), consum, ActionType.PERFORM);
            else canProcess = false;
        }
        if (doGenerate) {
            if (canConsum && canGenerate) capacitor.generateEnergy(world, pos, generation);
            else canProcess = false;
        }
        if (canProcess) data.addProgress(recipe.timeModifier * data.getSpeed());
        return canProcess;
    }

    public void task() {}

    public void finish() {
        cancle();
    }

    public void cancle() {
        MachineDataProviderComponent data = (MachineDataProviderComponent) this.data;
        data.resetProgress();
        data.resetRecipe();
    }

    public void idle() {}

    public void correct() {}

    public boolean containsItemIngredients(ItemIngredient... ingredients) {
        boolean bl = true;
        for (ItemIngredient ingredient : ingredients) {
            if (!inventory.containsInput(ingredient)) {
                bl = false;
                break;
            } 
        }
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
        ActivationState activationState = ((MachineDataProviderComponent) data).getActivationState();
        if (activationState == ActivationState.ALWAYS_ON) return true;
        else if(activationState == ActivationState.REDSTONE_ON) return world.isReceivingRedstonePower(pos);
        else if(activationState == ActivationState.REDSTONE_OFF) return !world.isReceivingRedstonePower(pos);
        else return false;
    }

    @Override
    public void fromTag(BlockState state,CompoundTag tag) {
        super.fromTag(state, tag);
        if (world != null && tag.contains("Capacitor", NbtType.COMPOUND)) capacitor.fromTag(tag.getCompound("Capacitor"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (world != null) {
            CompoundTag capacitorTag = new CompoundTag();
            capacitor.toTag(capacitorTag);
            if (!capacitorTag.isEmpty()) tag.put("Capacitor", capacitorTag);
        }
        return tag;
    }
    
    protected MachineCapacitorComponent initializeCapacitorComponent() {
        return new MachineCapacitorComponent(EnergyTypes.ULTRA_LOW_VOLTAGE);
    }

    @Override
    protected MachineDataProviderComponent initializeDataProviderComponent() {
        return (MachineDataProviderComponent) super.initializeDataProviderComponent();
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
            return RegistryHelper.getEntry(Helper.makeId("machine_config")).clientHandlerFactory.create(syncId, inv, buf);
		}

		@Override
		public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
			buf.writeBlockPos(pos);
		}
    }
}