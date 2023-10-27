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

package de.einholz.ehtech.generators.recipes;

import java.util.function.Consumer;

import de.einholz.ehmooshroom.generators.recipes.CustomRecipeProvider;
import de.einholz.ehtech.registry.ItemReg;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;

public class RecipeProvider extends CustomRecipeProvider {
    public RecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        // SHAPELESS
        //ItemReg.CHARCOAL_BLOCK.getGroup();
        addCompression(exporter, ItemReg.CHARCOAL_BLOCK, Items.CHARCOAL);
        // SHAPED
        // COOKING
    }
}
