package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehtech.storage.MachineItemStorage;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class MachineBE extends ProcessingBE {
    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(TransferablesReg.ELECTRICITY, new ElectricityStorage());
        getStorageMgr().withStorage(TransferablesReg.ITEMS, InventoryStorage.of(new MachineItemStorage()));
    }
}
