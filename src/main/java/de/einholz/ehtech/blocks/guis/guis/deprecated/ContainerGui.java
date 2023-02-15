package de.einholz.ehtech.blocks.guis.guis.deprecated;

import java.util.ArrayList;
import java.util.List;

import de.einholz.ehmooshroom.gui.screens.ContainerScreen;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

// fixme: clean up guis in general
@Deprecated
public abstract class ContainerGui extends SyncedGuiDescription {
    public BlockPos pos;
    public List<WButton> buttonIds;
    public ContainerScreen screen;
    
    protected ContainerGui(ScreenHandlerType<SyncedGuiDescription> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv);
        pos = buf.readBlockPos();
        // blockInventory = getInvComp().asInventory();
    }

    public static ContainerGui init(ContainerGui gui) {
        gui.buttonIds = new ArrayList<WButton>();
        gui.initWidgets();
        gui.drawDefault();
        gui.finish();
        return gui;
    }

    protected void initWidgets() {}

    protected void drawDefault() {
        ((WGridPanel) rootPanel).add(createPlayerInventoryPanel(), 0, 7);
    }

    public void finish() {
        rootPanel.validate(this);
    }

    protected Runnable getDefaultOnButtonClick(WButton button) {
        return () -> {
            MinecraftClient minecraft = screen.getMinecraftClient();
            minecraft.interactionManager.clickButton(syncId, buttonIds.indexOf(button));
            onButtonClick(playerInventory.player, buttonIds.indexOf(button));
        };
    }

    /*
    protected CombinedInventoryComponent getInvComp() {
        return (CombinedInventoryComponent) BlockComponentProvider.get(world.getBlockState(pos)).getComponent(world, pos, UniversalComponents.INVENTORY_COMPONENT, null);
    }

    protected CombinedDataComponent getDataComp() {
        return (CombinedDataComponent) BlockComponentProvider.get(world.getBlockState(pos)).getComponent(world, pos, UniversalComponents.DATA_PROVIDER_COMPONENT, null);
    }

    protected ConfigDataComponent getConfigComp() {
        return (ConfigDataComponent) getDataComp().getComp(MooshroomLib.HELPER.makeId("config"));
    }

    protected NameDataComponent getNameComp() {
        return (NameDataComponent) getDataComp().getComp(MooshroomLib.HELPER.makeId("name"));
    }
    */
}
