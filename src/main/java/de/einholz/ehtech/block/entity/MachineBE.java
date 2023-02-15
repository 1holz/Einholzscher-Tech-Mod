package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehtech.storage.MachineItemStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class MachineBE extends ProcessingBE {
    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        getStorageMgr().withStorage(TransferablesReg.ELECTRICITY, new ElectricityStorage());
        getStorageMgr().withStorage(TransferablesReg.ITEMS, new MachineItemStorage());
    }
}
