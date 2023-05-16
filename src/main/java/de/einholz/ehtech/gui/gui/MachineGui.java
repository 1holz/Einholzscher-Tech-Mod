package de.einholz.ehtech.gui.gui;

import java.util.function.Supplier;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.gui.gui.ContainerGui;
import de.einholz.ehmooshroom.gui.gui.Unit;
import de.einholz.ehmooshroom.gui.widget.Bar;
import de.einholz.ehmooshroom.gui.widget.Button;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.BarStorage;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehmooshroom.storage.StorageEntry;
import de.einholz.ehmooshroom.storage.transferable.ElectricityVariant;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.MachineBE;
import de.einholz.ehtech.storage.MachineInv;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public abstract class MachineGui extends ContainerGui {
    protected Identifier electricityBarBG;
    protected Identifier electricityBarFG;
    protected Identifier progressBarBG;
    protected Identifier progressBarFG;
    protected WItemSlot electricityInSlot;
    protected WItemSlot upgradeSlot;
    protected Bar electricityBar;
    protected Button activationButton ;
    protected Bar progressBar;
    protected WItemSlot networkSlot;
    protected WItemSlot electricityOutSlot;
    protected Button configurationButton;

    protected MachineGui(ScreenHandlerType<? extends SyncedGuiDescription> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    public static MachineGui init(MachineGui gui) {
        gui.electricityBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/background.png");
        gui.electricityBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/foreground.png");
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
        electricityInSlot = WItemSlot.of(getMachineInv(), MachineInv.ELECTRIC_IN);
        upgradeSlot = WItemSlot.of(getMachineInv(), MachineInv.UPGRADE);
        ElectricityStorage electricityStorage = ((ElectricityStorage) getMachineElectricity().storage);
        electricityBar = new Bar(electricityBarBG, electricityBarFG, Unit.ELECTRICITY.getColor(), BarStorage.MIN, () -> electricityStorage.getAmount(), electricityStorage.getMax(), Direction.UP);
        electricityBar.addDefaultTooltip("tooltip.ehtech.maschine.power_bar_amount");
        Supplier<?>[] powerBarTrendSuppliers = {
            () -> {
                return Unit.ELECTRICITY_PER_TICK.format(((ElectricityStorage) getMachineElectricity().storage).getBal());
            }
        };
        electricityBar.getAdvancedTooltips().put("tooltip.ehtech.machine.power_bar_trend", (Supplier<Object>[]) powerBarTrendSuppliers);
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
        networkSlot = WItemSlot.of(getMachineInv(), MachineInv.NETWORK);
        electricityOutSlot = WItemSlot.of(getMachineInv(), MachineInv.ELECTRIC_OUT);
        configurationButton.tooltips.add("tooltip.ehtech.configuration_button");
        configurationButton.setOnClick(getDefaultOnButtonClick(configurationButton));
        buttonIds.add(configurationButton);
    }

    @Override
    public void drawDefault() {
        super.drawDefault();
        ((WGridPanel) rootPanel).add(electricityInSlot, 8, 1);
        ((WGridPanel) rootPanel).add(upgradeSlot, 9, 1);
        ((WGridPanel) rootPanel).add(electricityBar, 8, 2, 1, 3);
        ((WGridPanel) rootPanel).add(activationButton, 9, 2);
        ((WGridPanel) rootPanel).add(networkSlot, 9, 3);
        ((WGridPanel) rootPanel).add(electricityOutSlot, 8, 5);
        ((WGridPanel) rootPanel).add(configurationButton, 9, 5);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == buttonIds.indexOf(activationButton)) {
            getBE().nextActivationState();
            return true;
        }
        if (id == buttonIds.indexOf(configurationButton)) {
            if (!world.isClient) player.openHandledScreen(getBE().new SideConfigScreenHandlerFactory());
            return true;
        }
        return super.onButtonClick(player, id);
    }

    @Override
    protected MachineBE getBE() {
        return (MachineBE) super.getBE();
    }

    // XXX protected?
    public Inventory getMachineInv() {
        return AdvInv.itemStorageToInv(getStorageMgr().getEntry(TechMod.HELPER.makeId("machine_items")));
    }

    // XXX protected?
    @SuppressWarnings("unchecked")
    public StorageEntry<Void, ElectricityVariant> getMachineElectricity() {
        return (StorageEntry<Void, ElectricityVariant>) getStorageMgr().getEntry(TechMod.HELPER.makeId("machine_electricity"));
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
