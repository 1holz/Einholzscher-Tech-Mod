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

import de.einholz.ehmooshroom.registry.ItemRegistry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.item.Wrench;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemReg extends ItemRegistry {
    public static final Identifier WRENCH_GROUP_ID = TechMod.HELPER.makeId("wrench");

    public static final BlockItem CHARCOAL_BLOCK = (BlockItem) new ItemReg()
            .register("charcoal_block", BlockReg.CHARCOAL_BLOCK)
            .withFuel(16000)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem COKE_COAL_BLOCK = (BlockItem) new ItemReg()
            .register("coke_coal_block", BlockReg.COKE_COAL_BLOCK)
            .withFuel(32000)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem MACHINE_FRAME_1 = (BlockItem) new ItemReg()
            .register("machine_frame_1", BlockReg.MACHINE_FRAME_1)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem COAL_GENERATOR = (BlockItem) new ItemReg()
            .register("coal_generator", BlockReg.COAL_GENERATOR)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem ORE_GROWER = (BlockItem) new ItemReg()
            .register("ore_grower", BlockReg.ORE_GROWER)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item HARD_COAL_TINY = ((ItemReg) new ItemReg()
            .register("hard_coal_tiny", Item::new))
            .withFuel(200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item CHARCOAL_TINY = ((ItemReg) new ItemReg()
            .register("charcoal_tiny", Item::new))
            .withFuel(200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item COKE_COAL_TINY = ((ItemReg) new ItemReg()
            .register("coke_coal_tiny", Item::new))
            .withFuel(400)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item COKE_COAL_CHUNK = ((ItemReg) new ItemReg()
            .register("coke_coal_chunk", Item::new))
            .withFuel(3200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Wrench WRENCH = (Wrench) ((ItemReg) new ItemReg()
            .register(WRENCH_GROUP_ID.getPath(), Wrench::new))
            .withItemGroupSelf()
            .get();

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
