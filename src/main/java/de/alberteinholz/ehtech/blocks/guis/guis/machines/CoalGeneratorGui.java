package de.alberteinholz.ehtech.blocks.guis.guis.machines;

import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
import de.alberteinholz.ehtech.blocks.components.machine.HeatDataComponent;
import de.alberteinholz.ehtech.blocks.guis.widgets.Bar;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class CoalGeneratorGui extends MachineGui {
    protected Identifier heatBarBG;
    protected Identifier heatBarFG;
    protected Bar heatBar;
    protected WItemSlot coalInputSlot;

    protected CoalGeneratorGui(ScreenHandlerType<SyncedGuiDescription> type, int syncId, PlayerInventory playerInv) {
        super(type, syncId, playerInv);
    }

    @SuppressWarnings("unchecked")
    public static CoalGeneratorGui init(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        return init(new CoalGeneratorGui((ScreenHandlerType<SyncedGuiDescription>) RegistryHelper.getEntry(TechMod.HELPER.makeId("coal_generator")).screenHandlerType, syncId, playerInv), buf);
    }

    public static CoalGeneratorGui init(CoalGeneratorGui gui, PacketByteBuf buf) {
        gui.heatBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/background.png");
        gui.heatBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/foreground.png");
        gui.heatBar = new Bar(gui.heatBarBG, gui.heatBarFG, gui.getFirstHeatComp().heat, Direction.UP);
        gui.coalInputSlot = WItemSlot.of(gui.blockInventory, 4);
        return (CoalGeneratorGui) MachineGui.init(gui, buf);
    }

    @Override
    public void drawDefault() {
        super.drawDefault();
        heatBar.addDefaultTooltip("tooltip.ehtech.coal_generator.heat_bar");
        ((WGridPanel) root).add(heatBar, 5, 2, 3, 3);
        ((WGridPanel) root).add(coalInputSlot, 2, 3);
        ((WGridPanel) root).add(progressBar, 3, 3, 2, 1);
    }

    protected AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("coal_generator_input_inv_1"));
    }

    protected HeatDataComponent getFirstHeatComp() {
        return (HeatDataComponent) getDataComp().getComp(TechMod.HELPER.makeId("coal_generator_heat_1"));
    }
}