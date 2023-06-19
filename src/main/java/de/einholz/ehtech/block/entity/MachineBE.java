package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.AdvItemStorage;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehmooshroom.storage.HeatStorage;
import de.einholz.ehtech.TechMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MachineBE extends ProcessingBE {
    public static final Identifier ELECTRIC_IN = TechMod.HELPER.makeId("electric_in");
    public static final Identifier ELECTRIC_OUT = TechMod.HELPER.makeId("electric_out");
    public static final Identifier UPGRADE = TechMod.HELPER.makeId("upgrade");
    public static final Identifier NETWORK = TechMod.HELPER.makeId("network");
    public static final Identifier MACHINE_ELECTRICITY = TechMod.HELPER.makeId("machine_electricity");
    public static final Identifier MACHINE_ITEMS = TechMod.HELPER.makeId("machine_items");

    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(MACHINE_ELECTRICITY, TransferablesReg.ELECTRICITY, new ElectricityStorage(this));
        getStorageMgr().withStorage(MACHINE_ITEMS, TransferablesReg.ITEMS, makeItemStorage());
        // TODO add cconfig availability
    }

    protected HeatStorage getMachineElectricity() {
        return (HeatStorage) getStorageMgr().getEntry(MACHINE_ELECTRICITY).storage;
    }

    protected Inventory getMachineInv() {
        return ((AdvItemStorage) getStorageMgr().getEntry(MACHINE_ITEMS).storage).getInv();
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, ELECTRIC_IN, ELECTRIC_OUT, UPGRADE, NETWORK);
        ((AdvInv) storage.getInv())
            .setAccepter((stack) -> Items.BEDROCK.equals(stack.getItem()), ELECTRIC_IN, ELECTRIC_OUT)
            .setAccepter((stack) -> false, UPGRADE, NETWORK);
        return storage;
    }
}
