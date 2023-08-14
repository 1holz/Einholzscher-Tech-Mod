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

package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.registry.BlockEntityTypeRegistry;
import de.einholz.ehmooshroom.storage.BlockApiLookups;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.CoalGeneratorBE;
import de.einholz.ehtech.block.entity.OreGrowerBE;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class BlockEntityTypeReg<T extends BlockEntity> extends BlockEntityTypeRegistry<T> {
    public static final BlockEntityType<?> COAL_GENERATOR = ((BlockEntityTypeReg<BlockEntity>) new BlockEntityTypeReg<>()
            .register("coal_generator", CoalGeneratorBE::new))
            .withMachine()
            .get();
    public static final BlockEntityType<?> ORE_GROWER = ((BlockEntityTypeReg<BlockEntity>) new BlockEntityTypeReg<>()
            .register("ore_grower", OreGrowerBE::new))
            .withMachine()
            .get();

    public BlockEntityTypeReg<T> withMachine() {
        return (BlockEntityTypeReg<T>) withBlockApiLookup(BlockApiLookups.ITEM_ID, BlockApiLookups.ELECTRICITY_ID);
    }

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
