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

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.BlockApiLookups;
import de.einholz.ehmooshroom.storage.SideConfigType;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.HeatStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.CoalGeneratorGui;
import de.einholz.ehtech.registry.BlockEntityTypeReg;
import de.einholz.ehtech.registry.RecipeTypeReg;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CoalGeneratorBE extends MachineBE {
    public static final Identifier COAL_IN = TechMod.HELPER.makeId("coal_in");
    public static final Identifier COAL_GENERATOR_ITEMS = TechMod.HELPER.makeId("coal_generator_items");
    public static final Identifier COAL_GENERATOR_HEAT = TechMod.HELPER.makeId("coal_generator_heat");

    public CoalGeneratorBE(BlockPos pos, BlockState state) {
        this(BlockEntityTypeReg.COAL_GENERATOR, pos, state, CoalGeneratorGui::init);
    }

    public CoalGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState state,
            ExtendedFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(COAL_GENERATOR_ITEMS, BlockApiLookups.ITEM_ID, makeItemStorage());
        getStorageMgr().withStorage(COAL_GENERATOR_HEAT, BlockApiLookups.HEAT_ID, new HeatStorage(this));
        getStorageMgr().getEntry(COAL_GENERATOR_ITEMS).change(SideConfigType.OUT_PROC);
        getStorageMgr().getEntry(COAL_GENERATOR_ITEMS).setAvailability(false, new SideConfigType[] {
                SideConfigType.SELF_OUT_D, SideConfigType.SELF_OUT_U, SideConfigType.SELF_OUT_N,
                SideConfigType.SELF_OUT_S, SideConfigType.SELF_OUT_W, SideConfigType.SELF_OUT_E,
                SideConfigType.FOREIGN_OUT_D, SideConfigType.FOREIGN_OUT_U, SideConfigType.FOREIGN_OUT_N,
                SideConfigType.FOREIGN_OUT_S, SideConfigType.FOREIGN_OUT_W, SideConfigType.FOREIGN_OUT_E
        });
        getStorageMgr().getEntry(COAL_GENERATOR_HEAT).setAvailability(false, (SideConfigType[]) null);
    }

    public Inventory getCoalGeneratorInv() {
        return ((AdvItemStorage) getStorageMgr().getEntry(COAL_GENERATOR_ITEMS).getStorage()).getInv();
    }

    public HeatStorage getCoalGeneratorHeat() {
        return (HeatStorage) getStorageMgr().getEntry(COAL_GENERATOR_HEAT).getStorage();
    }

    @Override
    public void operate() {
        super.operate();
        // originally 0.1 instead of 1
        if (!getCoalGeneratorHeat().isResourceBlank() && !getMachineElectricity().isFull())
            getMachineElectricity().increase((long) ((getCoalGeneratorHeat().getAmount() * 3 + 1) * getEfficiency()));
    }

    @Override
    public void idle() {
        super.idle();
        // originally 0.1 instead of 1
        if (!getCoalGeneratorHeat().isResourceBlank())
            getCoalGeneratorHeat().decrease(1);
    }

    @Override
    public RecipeType<AdvRecipe> getRecipeType() {
        return RecipeTypeReg.COAL_GENERATOR;
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, COAL_IN);
        ((AdvInv) storage.getInv()).setAccepter((stack) -> true, COAL_IN);
        return storage;
    }
}
