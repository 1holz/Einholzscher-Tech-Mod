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

package de.einholz.ehtech.generators.loottables;

import de.einholz.ehtech.registry.BlockReg;
import de.einholz.ehtech.registry.ItemReg;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateBlockLootTables() {
        addDrop(BlockReg.CHARCOAL_BLOCK, drops(ItemReg.CHARCOAL_BLOCK));
        addDrop(BlockReg.COAL_GENERATOR, drops(ItemReg.COAL_GENERATOR));
        addDrop(BlockReg.COKE_COAL_BLOCK, drops(ItemReg.COKE_COAL_BLOCK));
        addDrop(BlockReg.MACHINE_FRAME_1, drops(ItemReg.MACHINE_FRAME_1));
        addDrop(BlockReg.ORE_GROWER, drops(ItemReg.ORE_GROWER));
    }
}
