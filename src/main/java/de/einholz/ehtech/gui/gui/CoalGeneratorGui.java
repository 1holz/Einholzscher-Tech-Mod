package de.einholz.ehtech.gui.gui;

import de.einholz.ehmooshroom.gui.gui.Unit;
import de.einholz.ehmooshroom.gui.widget.Bar;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.BarStorage;
import de.einholz.ehmooshroom.storage.HeatStorage;
import de.einholz.ehmooshroom.storage.StorageEntry;
import de.einholz.ehmooshroom.storage.transferable.HeatVariant;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.CoalGeneratorBE.CoalGeneratorInv;
import de.einholz.ehtech.registry.Registry;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class CoalGeneratorGui extends MachineGui {
    protected Identifier heatBarBG;
    protected Identifier heatBarFG;
    protected Bar heatBar;
    protected WItemSlot coalInSlot;

    protected CoalGeneratorGui(ScreenHandlerType<? extends CoalGeneratorGui> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }
    
    public static CoalGeneratorGui init(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        return init(new CoalGeneratorGui(Registry.COAL_GENERATOR.GUI, syncId, playerInv, buf));
    }

    public static CoalGeneratorGui init(CoalGeneratorGui gui) {
        gui.heatBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/background.png");
        gui.heatBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/coalgenerator/elements/heat_bar/foreground.png");
        gui.heatBar = new Bar(gui.heatBarBG, gui.heatBarFG, Unit.KELVIN.getColor(), BarStorage.MIN, () -> ((HeatStorage) gui.getCoalGeneratorHeat().storage).getAmount(), ((HeatStorage) gui.getCoalGeneratorHeat().storage).getMax(), Direction.UP);
        gui.coalInSlot = WItemSlot.of(gui.getCoalGeneratorInv(), CoalGeneratorInv.COAL_IN);
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

    // XXX protected?
    public Inventory getCoalGeneratorInv() {
        return AdvInv.itemStorageToInv(getStorageMgr().getEntry(TechMod.HELPER.makeId("coal_generator_items")));
    }

    // XXX protected?
    @SuppressWarnings("unchecked")
    public StorageEntry<Void, HeatVariant> getCoalGeneratorHeat() {
        return (StorageEntry<Void, HeatVariant>) getStorageMgr().getEntry(TechMod.HELPER.makeId("coal_generator_heat"));
    }

    /* TODO del
    protected AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("coal_generator_input_inv_1"));
    }

    protected HeatDataComponent getFirstHeatComp() {
        return (HeatDataComponent) getDataComp().getComp(TechMod.HELPER.makeId("coal_generator_heat_1"));
    }
    */

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (player.world.isClient) System.out.println("CoalGeneratorC");
        else System.out.println("CoalGeneratorS");
        return super.onButtonClick(player, id);
    }
}
