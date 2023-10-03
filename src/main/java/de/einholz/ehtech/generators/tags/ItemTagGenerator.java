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
import de.einholz.ehtech.registry.ItemReg;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ItemTagGenerator extends ItemTagProvider {
    // TECHMOD
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_BLOCK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/charcoal_block"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_CHUNK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/charcoal_chunk"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_TINY = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/charcoal_tiny"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_BLOCK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/coke_coal_block"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_CHUNK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/coke_coal_chunk"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_TINY = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/coke_coal_tiny"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_BLOCK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/hard_coal_block"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_CHUNK = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/hard_coal_chunk"));
    private static final TagKey<Item> MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_TINY = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/coal_generator/hard_coal_tiny"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_ANCIENT_DEBRIS_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/ancient_debris_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_COAL_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/coal_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_COPPER_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/copper_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_DIAMOND_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/diamond_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_EMERALD_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/emerald_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_GOLD_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/gold_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_IRON_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/iron_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_LAPIS_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/lapis_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_QUARTZ_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/quartz_seed"));
    private static final TagKey<Item> MACHINE_RECIPES_ORE_GROWER_REDSTONE_SEED = TagKey.of(Registry.ITEM_KEY,
            TechMod.HELPER.makeId("machine_recipes/ore_growewr/redstone_seed"));

    public ItemTagGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_BLOCK)
                .add(ItemReg.CHARCOAL_BLOCK);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_CHUNK)
                .add(Items.CHARCOAL);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_CHARCOAL_TINY)
                .add(ItemReg.CHARCOAL_TINY);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_BLOCK)
                .add(ItemReg.COKE_COAL_BLOCK);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_CHUNK)
                .add(ItemReg.COKE_COAL_CHUNK);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_COKE_COAL_TINY)
                .add(ItemReg.COKE_COAL_TINY);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_BLOCK)
                .add(Items.COAL_BLOCK);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_CHUNK)
                .add(Items.COAL);
        getOrCreateTagBuilder(MACHINE_RECIPES_COAL_GENERATOR_HARD_COAL_TINY)
                .add(ItemReg.HARD_COAL_TINY);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_ANCIENT_DEBRIS_SEED)
                .add(Items.NETHERITE_SCRAP);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_COAL_SEED)
                .add(Items.COAL, Items.CHARCOAL);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_COPPER_SEED)
                .add(Items.RAW_COPPER, Items.COPPER_INGOT);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_DIAMOND_SEED)
                .add(Items.DIAMOND);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_EMERALD_SEED)
                .add(Items.EMERALD);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_GOLD_SEED)
                .add(Items.RAW_GOLD, Items.GOLD_INGOT);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_IRON_SEED)
                .add(Items.RAW_IRON, Items.IRON_INGOT);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_LAPIS_SEED)
                .add(Items.LAPIS_LAZULI);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_QUARTZ_SEED)
                .add(Items.QUARTZ);
        getOrCreateTagBuilder(MACHINE_RECIPES_ORE_GROWER_REDSTONE_SEED)
                .add(Items.REDSTONE);
    }
}
