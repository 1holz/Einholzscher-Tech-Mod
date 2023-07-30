package de.einholz.ehtech.gui.gui;

import java.util.function.Function;
import java.util.function.Supplier;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.gui.gui.ContainerGui;
import de.einholz.ehmooshroom.gui.gui.UnitFormatter;
import de.einholz.ehmooshroom.gui.widget.Bar;
import de.einholz.ehmooshroom.gui.widget.Button;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.storages.BarStorage;
import de.einholz.ehmooshroom.storage.storages.ElectricityStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.MachineBE;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class MachineGui extends ContainerGui {
    protected Identifier electricityBarBG = TechMod.HELPER
            .makeId("textures/gui/container/machine/elements/electricity_bar/background.png");
    protected Identifier electricityBarFG = TechMod.HELPER
            .makeId("textures/gui/container/machine/elements/electricity_bar/foreground.png");
    protected Identifier progressBarBG = TechMod.HELPER
            .makeId("textures/gui/container/machine/elements/progress_bar/background.png");
    protected Identifier progressBarFG = TechMod.HELPER
            .makeId("textures/gui/container/machine/elements/progress_bar/foreground.png");
    protected WItemSlot electricityInSlot;
    protected WItemSlot upgradeSlot;
    protected Bar electricityBar;
    protected Button activationButton;
    protected Bar progressBar;
    protected WItemSlot networkSlot;
    protected WItemSlot electricityOutSlot;
    protected Button configurationButton;

    protected MachineGui(ScreenHandlerType<? extends SyncedGuiDescription> type, int syncId, PlayerInventory playerInv,
            PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    public static MachineGui init(MachineGui gui) {
        gui.activationButton = gui.new ActivationButton(player -> {
            gui.getBE().nextActivationState();
            return true;
        });
        gui.configurationButton = (Button) new Button(player -> {
            if (!gui.world.isClient)
                player.openHandledScreen(gui.getBE().new SideConfigScreenHandlerFactory());
            return true;
        }).setLabel(new TranslatableText("block.ehtech.machine.configuration_button"));
        return (MachineGui) ContainerGui.init(gui);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initWidgets() {
        super.initWidgets();
        electricityInSlot = WItemSlot.of(getMachineInv(), ((AdvInv) getMachineInv()).getSlotIndex(MachineBE.ELECTRIC_IN));
        upgradeSlot = WItemSlot.of(getMachineInv(), ((AdvInv) getMachineInv()).getSlotIndex(MachineBE.UPGRADE));
        electricityBar = new Bar(electricityBarBG, electricityBarFG, BarStorage.MIN, () -> getMachineElectricity().getAmount(), getMachineElectricity().getMax(), Direction.UP, "Wh");
        electricityBar.addDefaultTooltip("tooltip.ehtech.machine.power_bar_amount");
        Supplier<Object>[] powerBarTrendSuppliers = new Supplier[] {
            () -> UnitFormatter.formatLong(getMachineElectricity().getBal(), "Wh/tick")
        };
        electricityBar.getDynTooltips().put("tooltip.ehtech.machine.power_bar_trend", powerBarTrendSuppliers);
        Supplier<Object>[] activationButtonSuppliers = new Supplier[] {
            getBE().getActivationState().toString()::toLowerCase
        };
        activationButton.advancedTooltips.put("tooltip.ehtech.activation_button", activationButtonSuppliers);
        addButton(activationButton);
        progressBar = new Bar(progressBarBG, progressBarFG, (long) ProcessingBE.PROGRESS_MIN, () -> (long) getBE().getProgress(), (long) ProcessingBE.PROGRESS_MAX, Direction.RIGHT, "\u2030");
        progressBar.addDefaultTooltip("tooltip.ehtech.machine.progress_bar");
        networkSlot = WItemSlot.of(getMachineInv(), ((AdvInv) getMachineInv()).getSlotIndex(MachineBE.NETWORK));
        electricityOutSlot = WItemSlot.of(getMachineInv(), ((AdvInv) getMachineInv()).getSlotIndex(MachineBE.ELECTRIC_OUT));
        configurationButton.tooltips.add("tooltip.ehtech.machine.configuration_button");
        addButton(configurationButton);
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
    protected MachineBE getBE() {
        return (MachineBE) super.getBE();
    }

    public Inventory getMachineInv() {
        return ((MachineBE) getBE()).getMachineInv();
    }

    public ElectricityStorage getMachineElectricity() {
        return ((MachineBE) getBE()).getMachineElectricity();
    }

    protected class ActivationButton extends Button {
        public ActivationButton(Function<PlayerEntity, Boolean> exe) {
            super(exe);
        }

        @Override
        public Identifier setTexture(int mouseX, int mouseY) {
            withTexture(TechMod.HELPER.makeId("textures/gui/container/machine/elements/activation_button/"
                    + getBE().getActivationState().toString().toLowerCase() + ".png"));
            return super.setTexture(mouseX, mouseY);
        }
    }
}
