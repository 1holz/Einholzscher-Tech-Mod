package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.ElectricityStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.storage.MachineInv;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class MachineBE extends ProcessingBE {
    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(TechMod.HELPER.makeId("machine_electricity"), TransferablesReg.ELECTRICITY, new ElectricityStorage());
        getStorageMgr().withStorage(TechMod.HELPER.makeId("machine_items") , TransferablesReg.ITEMS, MachineInv.of());
    }
}
