package de.einholz.ehtech.gui.gui;

import java.util.function.Supplier;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.gui.gui.ContainerGui;
import de.einholz.ehmooshroom.gui.gui.Unit;
import de.einholz.ehmooshroom.gui.widget.Bar;
import de.einholz.ehmooshroom.gui.widget.Button;
import de.einholz.ehmooshroom.mixin.InventoryStorageImplA;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.BarStorage;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehmooshroom.storage.SidedStorageMgr.StorageEntry;
import de.einholz.ehmooshroom.storage.transferable.ElectricityVariant;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.MachineBE;
import de.einholz.ehtech.storage.MachineInv;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public abstract class MachineGui extends ContainerGui {
    protected Identifier powerBarBG;
    protected Identifier powerBarFG;
    protected Identifier progressBarBG;
    protected Identifier progressBarFG;
    protected WItemSlot powerInputSlot;
    protected WItemSlot upgradeSlot;
    protected Bar powerBar;
    protected Button activationButton ;
    protected Bar progressBar;
    protected WItemSlot networkSlot;
    protected WItemSlot powerOutputSlot;
    protected Button configurationButton;

    protected MachineGui(ScreenHandlerType<? extends SyncedGuiDescription> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    public static MachineGui init(MachineGui gui) {
        gui.powerBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/background.png");
        gui.powerBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/foreground.png");
        gui.progressBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/progress_bar/background.png");
        gui.progressBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/progress_bar/foreground.png");
        gui.activationButton = gui.new ActivationButton();
        // TODO use translateable text
        gui.configurationButton = (Button) new Button().setLabel(new LiteralText("CON"));
        return (MachineGui) ContainerGui.init(gui);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initWidgets() {
        super.initWidgets();
        powerInputSlot = WItemSlot.of(getInv(), MachineInv.ELECTRIC_IN);
        upgradeSlot = WItemSlot.of(getInv(), MachineInv.UPGRADE);
        ElectricityStorage electricityStorage = ((ElectricityStorage) getElectricity().storage);
        powerBar = new Bar(powerBarBG, powerBarFG, Unit.ELECTRICITY.getColor(), BarStorage.MIN, () -> electricityStorage.getAmount(), electricityStorage.getMax(), Direction.UP);
        powerBar.addDefaultTooltip("tooltip.ehtech.maschine.power_bar_amount");
        Supplier<?>[] powerBarTrendSuppliers = {
            () -> {
                return Unit.ELECTRICITY_PER_TICK.format(((ElectricityStorage) getBE().getStorageMgr().getStorageEntry(TransferablesReg.ELECTRICITY).storage).getBal());
            }
        };
        powerBar.getAdvancedTooltips().put("tooltip.ehtech.machine.power_bar_trend", (Supplier<Object>[]) powerBarTrendSuppliers);
        Supplier<?>[] activationButtonSuppliers = {
            () -> {
                return getBE().getActivationState().toString().toLowerCase();
            }
        };
        activationButton.advancedTooltips.put("tooltip.ehtech.activation_button", (Supplier<Object>[]) activationButtonSuppliers);
        activationButton.setOnClick(getDefaultOnButtonClick(activationButton));
        buttonIds.add(activationButton);
        progressBar = new Bar(progressBarBG, progressBarFG, Unit.PERCENT.getColor(), (long) ProcessingBE.PROGRESS_MIN, () -> (long) getBE().getProgress(), (long) ProcessingBE.PROGRESS_MAX, Direction.RIGHT);
        progressBar.addDefaultTooltip("tooltip.ehtech.maschine.progress_bar");
        networkSlot = WItemSlot.of(getInv(), MachineInv.NETWORK);
        powerOutputSlot = WItemSlot.of(getInv(), MachineInv.ELECTRIC_OUT);
        configurationButton.tooltips.add("tooltip.ehtech.configuration_button");
        configurationButton.setOnClick(getDefaultOnButtonClick(configurationButton));
        buttonIds.add(configurationButton);
    }

    @Override
    public void drawDefault() {
        super.drawDefault();
        ((WGridPanel) rootPanel).add(powerInputSlot, 8, 1);
        ((WGridPanel) rootPanel).add(upgradeSlot, 9, 1);
        ((WGridPanel) rootPanel).add(powerBar, 8, 2, 1, 3);
        ((WGridPanel) rootPanel).add(activationButton, 9, 2);
        ((WGridPanel) rootPanel).add(networkSlot, 9, 3);
        ((WGridPanel) rootPanel).add(powerOutputSlot, 8, 5);
        ((WGridPanel) rootPanel).add(configurationButton, 9, 5);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == buttonIds.indexOf(activationButton)) {
            getBE().nextActivationState();
            getBE().markDirty();
            return true;
        }
        if (id == buttonIds.indexOf(configurationButton)) {
            if (!world.isClient) player.openHandledScreen(getBE().new SideConfigScreenHandlerFactory());
            return true;
        }
        return super.onButtonClick(player, id);
    }

    /* TODO del
    @Deprecated // todo replace with MooshroomLib methode
    protected SidedStorageMgr getStorageMgr() {
        SidedStorageMgr mgr = getBE().getStorageMgr();
        if (mgr != null) return mgr;
        TechMod.LOGGER.smallBug(new IllegalStateException("Can only retrieve StorageMgr from ContainerBE"));
        return null;
    }
    */

    @Override
    protected MachineBE getBE() {
        return (MachineBE) super.getBE();
    }

    // XXX protected?
    public Inventory getInv() {
        if (getStorageMgr().getStorageEntry(TransferablesReg.ITEMS).storage instanceof InventoryStorageImpl impl)
            return ((InventoryStorageImplA) impl).getInventory();
        TechMod.LOGGER.bigBug(new IllegalStateException("Item Storage must be of the type InventoryStorageImpl"));
        return new SimpleInventory(0);
    }

    // XXX protected?
    public StorageEntry<ElectricityVariant> getElectricity() {
        return getStorageMgr().getStorageEntry(TransferablesReg.ELECTRICITY);
    }

    /* TODO del
    @Deprecated
    protected MachineDataComponent getMachineDataComp() {
        return (MachineDataComponent) getDataComp().getComp(TechMod.HELPER.makeId("data_machine"));
    }

    @Deprecated
    protected AdvancedCapacitorComponent getCapacitorComp() {
        return (AdvancedCapacitorComponent) BlockComponentProvider.get(world.getBlockState(pos)).getComponent(world, pos, UniversalComponents.CAPACITOR_COMPONENT, null);
    }

    @Deprecated
    protected AdvancedInventoryComponent getMachineInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("inventory_machine"));
    }
    */

    protected class ActivationButton extends Button {
        @Override
        public Identifier setTexture(int mouseX, int mouseY) {
            withTexture(TechMod.HELPER.makeId("textures/gui/container/machine/elements/activation_button/" + getBE().getActivationState().toString().toLowerCase() + ".png"));
            return super.setTexture(mouseX, mouseY);
        }
    }
}
