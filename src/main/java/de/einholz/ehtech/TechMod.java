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

package de.einholz.ehtech;

import de.einholz.ehmooshroom.util.Helper;
import de.einholz.ehmooshroom.util.LoggerHelper;
import de.einholz.ehtech.generators.tags.BlockTagGenerator;
import de.einholz.ehtech.generators.tags.ItemTagGenerator;
import de.einholz.ehtech.registry.BlockEntityTypeReg;
import de.einholz.ehtech.registry.BlockReg;
import de.einholz.ehtech.registry.ItemReg;
import de.einholz.ehtech.registry.RecipeSerializerReg;
import de.einholz.ehtech.registry.RecipeTypeReg;
import de.einholz.ehtech.registry.ScreenHandlerReg;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TechMod implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint {
    public static final Helper HELPER = new Helper("ehtech");
    public static final LoggerHelper LOGGER = new LoggerHelper(HELPER.MOD_ID,
            "https://github.com/Albert-Einholz/Einholzscher-Tech-Mod/issues");

    @Override
    public void onInitialize() {
        BlockReg.registerAll();
        ItemReg.registerAll();
        BlockEntityTypeReg.registerAll();
        ScreenHandlerReg.registerAll();
        RecipeTypeReg.registerAll();
        RecipeSerializerReg.registerAll();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        // use this for future versions:
        // Pack pack = generator.createPack();
        generator.addProvider(new BlockTagGenerator(generator));
        generator.addProvider(new ItemTagGenerator(generator));
    }
}
