package de.alberteinholz.ehtech.blocks.guis.guis.machines;

import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;

public class OreGrowerGui extends MachineGui {
    protected WItemSlot oreInputSlot;

    protected OreGrowerGui(ScreenHandlerType<SyncedGuiDescription> type, int syncId, PlayerInventory playerInv) {
        super(type, syncId, playerInv);
    }

    @SuppressWarnings("unchecked")
    public static OreGrowerGui init(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        return init(new OreGrowerGui((ScreenHandlerType<SyncedGuiDescription>) RegistryHelper.getEntry(TechMod.HELPER.makeId("ore_grower")).screenHandlerType, syncId, playerInv), buf);
    }

    public static OreGrowerGui init(OreGrowerGui gui, PacketByteBuf buf) {
        gui.progressBarBG = TechMod.HELPER.makeId("textures/gui/container/machine/oregrower/elements/progress_bar_bg.png");
        gui.progressBarFG = TechMod.HELPER.makeId("textures/gui/container/machine/oregrower/elements/progress_bar_fg.png");
        gui.oreInputSlot = WItemSlot.of(gui.blockInventory, 4);
        return (OreGrowerGui) MachineGui.init(gui, buf);
    }

    @Override
    public void drawDefault() {
        super.drawDefault();
        ((WGridPanel) root).add(oreInputSlot, 2, 3);
        ((WGridPanel) root).add(progressBar, 3, 3, 2, 1);
    }

    protected AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("ore_grower_input_inv_1"));
    }
}