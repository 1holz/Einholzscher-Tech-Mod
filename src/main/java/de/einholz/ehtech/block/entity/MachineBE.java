/*
 * Copyright 2023 Einholz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.block.entity.ProcessingBE;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.BlockApiLookups;
import de.einholz.ehmooshroom.storage.SideConfigType;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.ElectricityStorage;
import de.einholz.ehtech.TechMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class MachineBE extends ProcessingBE {
    public static final Identifier ELECTRIC_IN = TechMod.HELPER.makeId("electric_in");
    public static final Identifier ELECTRIC_OUT = TechMod.HELPER.makeId("electric_out");
    public static final Identifier UPGRADE = TechMod.HELPER.makeId("upgrade");
    public static final Identifier NETWORK = TechMod.HELPER.makeId("network");
    public static final Identifier MACHINE_ELECTRICITY = TechMod.HELPER.makeId("machine_electricity");
    public static final Identifier MACHINE_ITEMS = TechMod.HELPER.makeId("machine_items");

    public MachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state,
            ExtendedFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(MACHINE_ELECTRICITY, new ElectricityStorage(this));
        getStorageMgr().withStorage(MACHINE_ITEMS, makeItemStorage());
        getStorageMgr().getEntry(MACHINE_ITEMS).setAvailability(false, (SideConfigType[]) null);
        putMaxTransfer(Registry.ITEM_KEY.getValue(), 1);
        putMaxTransfer(BlockApiLookups.ELECTRICITY_ID, 1);
    }

    @Override
    public void transfer() {
        super.transfer();
        // TODO only for early development replace with proper creative battery
        if (getMachineInv().getStack(ELECTRIC_IN).getItem().equals(Items.BEDROCK))
            getMachineElectricity().increase(getMaxTransfer(BlockApiLookups.ELECTRICITY_ID));
        if (getMachineInv().getStack(ELECTRIC_OUT).getItem().equals(Items.BEDROCK))
            getMachineElectricity().decrease(getMaxTransfer(BlockApiLookups.ELECTRICITY_ID));
    }

    public ElectricityStorage getMachineElectricity() {
        return (ElectricityStorage) getStorageMgr().getEntry(MACHINE_ELECTRICITY).getStorage();
    }

    public AdvInv getMachineInv() {
        return (AdvInv) ((AdvItemStorage) getStorageMgr().getEntry(MACHINE_ITEMS).getStorage()).getInv();
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, ELECTRIC_IN, ELECTRIC_OUT, UPGRADE, NETWORK);
        ((AdvInv) storage.getInv())
                .setAccepter((stack) -> Items.BEDROCK.equals(stack.getItem()), ELECTRIC_IN, ELECTRIC_OUT)
                .setAccepter((stack) -> false, UPGRADE, NETWORK);
        return storage;
    }
}
