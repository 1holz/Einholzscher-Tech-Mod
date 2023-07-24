package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.registry.TransferableRegistry;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.ElectricityStorage;
import de.einholz.ehtech.TechMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
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

    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state,
            ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(MACHINE_ELECTRICITY, TransferableRegistry.ELECTRICITY, new ElectricityStorage(this));
        getStorageMgr().withStorage(MACHINE_ITEMS, TransferableRegistry.ITEMS, makeItemStorage());
        // TODO add config availability
        putMaxTransfer(TransferableRegistry.ITEMS, 1);
        putMaxTransfer(TransferableRegistry.ELECTRICITY, 1);
    }

    @Override
    public void transfer() {
        super.transfer();
        // TODO only for early development replace with proper creative battery
        if (getMachineInv().getStack(ELECTRIC_IN).getItem().equals(Items.BEDROCK))
            getMachineElectricity().increase(getMaxTransfer(TransferableRegistry.ELECTRICITY));
        if (getMachineInv().getStack(ELECTRIC_OUT).getItem().equals(Items.BEDROCK))
            getMachineElectricity().decrease(getMaxTransfer(TransferableRegistry.ELECTRICITY));
    }

    public ElectricityStorage getMachineElectricity() {
        return (ElectricityStorage) getStorageMgr().getEntry(MACHINE_ELECTRICITY).storage;
    }

    public AdvInv getMachineInv() {
        return (AdvInv) ((AdvItemStorage) getStorageMgr().getEntry(MACHINE_ITEMS).storage).getInv();
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, ELECTRIC_IN, ELECTRIC_OUT, UPGRADE, NETWORK);
        ((AdvInv) storage.getInv())
                .setAccepter((stack) -> Items.BEDROCK.equals(stack.getItem()), ELECTRIC_IN, ELECTRIC_OUT)
                .setAccepter((stack) -> false, UPGRADE, NETWORK);
        return storage;
    }
}
