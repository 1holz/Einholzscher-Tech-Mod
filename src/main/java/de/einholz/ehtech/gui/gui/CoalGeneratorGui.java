package de.einholz.ehtech.gui.gui;

import de.einholz.ehmooshroom.gui.gui.Unit;
import de.einholz.ehmooshroom.gui.widget.Bar;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.storages.BarStorage;
import de.einholz.ehmooshroom.storage.storages.HeatStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.CoalGeneratorBE;
import de.einholz.ehtech.registry.Registry;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class CoalGeneratorGui extends MachineGui {
    protected Identifier heatBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/background.png");
    protected Identifier heatBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/foreground.png");
    protected Bar heatBar;
    protected WItemSlot coalInSlot;

    protected CoalGeneratorGui(ScreenHandlerType<? extends CoalGeneratorGui> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    public static CoalGeneratorGui init(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        return init(new CoalGeneratorGui(Registry.COAL_GENERATOR.GUI, syncId, playerInv, buf));
    }

    public static CoalGeneratorGui init(CoalGeneratorGui gui) {
        gui.heatBar = new Bar(gui.heatBarBG, gui.heatBarFG, Unit.KELVIN.getColor(), BarStorage.MIN, () -> gui.getCoalGeneratorHeat().getAmount(), gui.getCoalGeneratorHeat().getMax(), Direction.UP);
        gui.coalInSlot = WItemSlot.of(gui.getCoalGeneratorInv(), ((AdvInv) gui.getCoalGeneratorInv()).getSlotIndex(CoalGeneratorBE.COAL_IN));
        return (CoalGeneratorGui) MachineGui.init(gui);
    }

    @Override
    public void drawDefault() {
        super.drawDefault();
        heatBar.addDefaultTooltip("tooltip.ehtech.coal_generator.heat_bar");
        ((WGridPanel) rootPanel).add(heatBar, 5, 2, 3, 3);
        ((WGridPanel) rootPanel).add(coalInSlot, 2, 3);
        ((WGridPanel) rootPanel).add(progressBar, 3, 3, 2, 1);
    }

    protected Inventory getCoalGeneratorInv() {
        return ((CoalGeneratorBE) getBE()).getCoalGeneratorInv();
    }

    protected HeatStorage getCoalGeneratorHeat() {
        return ((CoalGeneratorBE) getBE()).getCoalGeneratorHeat();
    }
}
