package de.einholz.ehtech.blocks.guis.guis.machines;

import java.util.function.Supplier;

import de.alberteinholz.ehmooshroom.container.component.energy.AdvancedCapacitorComponent;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.einholz.ehtech.blocks.components.machine.MachineDataComponent;
import de.einholz.ehtech.blocks.guis.guis.ContainerGui;
import de.einholz.ehtech.blocks.guis.widgets.Bar;
import de.einholz.ehtech.blocks.guis.widgets.Button;
import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.component.data.api.UnitManager;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

    protected MachineGui(ScreenHandlerType<SyncedGuiDescription> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    public static MachineGui init(MachineGui gui) {
        gui.powerBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/background.png");
        gui.powerBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/power_bar/foreground.png");
        gui.progressBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/progress_bar/background.png");
        gui.progressBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/elements/progress_bar/foreground.png");
        gui.activationButton = gui.new ActivationButton();
        gui.configurationButton = (Button) new Button().setLabel(new LiteralText("CON"));
        return (MachineGui) ContainerGui.init(gui);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initWidgets() {
        super.initWidgets();
        powerInputSlot = WItemSlot.of(blockInventory, getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("power_input")));
        upgradeSlot = WItemSlot.of(blockInventory, getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("upgrade")));
        powerBar = new Bar(powerBarBG, powerBarFG, getCapacitorComp(), Direction.UP);
        powerBar.addDefaultTooltip("tooltip.ehtech.maschine.power_bar_amount");
        Supplier<?>[] powerBarTrendSuppliers = {
            () -> {
                return UnitManager.WU_PER_TICK.format(((MachineBlockEntity) world.getBlockEntity(pos)).powerBalance);
            }
        };
        powerBar.advancedTooltips.put("tooltip.ehtech.machine.power_bar_trend", (Supplier<Object>[]) powerBarTrendSuppliers);
        Supplier<?>[] activationButtonSuppliers = {
            () -> {
                return getMachineDataComp().getActivationState().name().toLowerCase();
            }
        };
        activationButton.advancedTooltips.put("tooltip.ehtech.activation_button", (Supplier<Object>[]) activationButtonSuppliers);
        activationButton.setOnClick(getDefaultOnButtonClick(activationButton));
        buttonIds.add(activationButton);
        progressBar = new Bar(progressBarBG, progressBarFG, getMachineDataComp().progress, Direction.RIGHT);
        progressBar.addDefaultTooltip("tooltip.ehtech.maschine.progress_bar");
        networkSlot = WItemSlot.of(blockInventory, getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("network")));
        powerOutputSlot = WItemSlot.of(blockInventory, getMachineInvComp().getIntFromId(TechMod.HELPER.makeId("power_output")));
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
            getMachineDataComp().nextActivationState();
            world.getBlockEntity(pos).markDirty();
            return true;
        } else if (id == buttonIds.indexOf(configurationButton)) {
            if (!world.isClient) player.openHandledScreen(((MachineBlockEntity) world.getBlockEntity(pos)).getSideConfigScreenHandlerFactory());
            return true;
        }
        return false;
    }

    protected MachineDataComponent getMachineDataComp() {
        return (MachineDataComponent) getDataComp().getComp(TechMod.HELPER.makeId("data_machine"));
    }

    protected AdvancedCapacitorComponent getCapacitorComp() {
        return (AdvancedCapacitorComponent) BlockComponentProvider.get(world.getBlockState(pos)).getComponent(world, pos, UniversalComponents.CAPACITOR_COMPONENT, null);
    }

    protected AdvancedInventoryComponent getMachineInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("inventory_machine"));
    }

    protected class ActivationButton extends Button {
        @Override
        public Identifier setTexture(int mouseX, int mouseY) {
            withTexture(TechMod.HELPER.makeId("textures/gui/container/machine/elements/activation_button/" + getMachineDataComp().getActivationState().toString().toLowerCase() + ".png"));
            return super.setTexture(mouseX, mouseY);
        }
    }
}