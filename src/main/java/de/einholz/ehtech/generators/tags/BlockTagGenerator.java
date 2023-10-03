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

package de.einholz.ehtech.generators.tags;

import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.registry.BlockReg;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockTagGenerator extends BlockTagProvider {
    // TECHMOD
    private static final TagKey<Block> MACHINE_RECIPES_ORE_GROWER_DEEPSLATE_BASE = TagKey.of(Registry.BLOCK_KEY,
            TechMod.HELPER.makeId("machine_recipe/ore_grower/deepslate_base"));
    private static final TagKey<Block> MACHINE_RECIPES_ORE_GROWER_NETHERRACK_BASE = TagKey.of(Registry.BLOCK_KEY,
            TechMod.HELPER.makeId("machine_recipe/ore_grower/netherrack_base"));
    private static final TagKey<Block> MACHINE_RECIPES_ORE_GROWER_STONE_BASE = TagKey.of(Registry.BLOCK_KEY,
            TechMod.HELPER.makeId("machine_recipe/ore_grower/stone_base"));
    // MINECRAFT
    private static final TagKey<Block> MINEABLE_PICKAXE = TagKey.of(Registry.BLOCK_KEY,
            new Identifier("minecraft", "mineable/pickaxe"));

    public BlockTagGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        // TECHMOD
        // XXX this adds are vanilla tags so the optional should not be needed but this
        // XXX throws an exception otherwise
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_DEEPSLATE_BASE)
                .addOptionalTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_NETHERRACK_BASE)
                .addOptionalTag(BlockTags.BASE_STONE_NETHER);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_STONE_BASE)
                .addOptionalTag(BlockTags.BASE_STONE_OVERWORLD);
        // MINECRAFT
        getOrCreateTagBuilder(MINEABLE_PICKAXE)
                .add(BlockReg.CHARCOAL_BLOCK, BlockReg.COAL_GENERATOR, BlockReg.COKE_COAL_BLOCK,
                        BlockReg.MACHINE_FRAME_1, BlockReg.ORE_GROWER);
    }
}
