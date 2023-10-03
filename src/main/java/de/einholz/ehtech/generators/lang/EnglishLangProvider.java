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

package de.einholz.ehtech.generators.lang;

import de.einholz.ehmooshroom.generators.lang.CustomLangProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EnglishLangProvider extends CustomLangProvider {
    public EnglishLangProvider(FabricDataGenerator generator, String code) {
        super(generator, code);
    }

    public void generateTranslations(TranslationBuilder builder) {
        add(builder, "block", "charcoal_block", "Charcoal Block");
        add(builder, "block", "coal_generator", "Coal Generator");
        add(builder, "block", "coke_coal_block", "Coke Coal Block");
        add(builder, "block", "config.coal_generator_items", "Items (Coal Generator)");
        add(builder, "block", "config.machine_electricity", "Electricity (Machine)");
        add(builder, "block", "config.ore_grower", "Items (Ore Grower)");
        add(builder, "block", "machine.configuration_button", "CON");
        add(builder, "block", "machine_frame_1", "Machine Frame Tier 1");
        add(builder, "block", "ore_grower", "Ore Grower");
        add(builder, "item", "wrench", "Wrench");
        add(builder, "item", "charcoal_tiny", "Tiny Charcoal");
        add(builder, "item", "coke_coal_chunk", "Coke Coal Chunk");
        add(builder, "item", "coke_coal_tiny", "Tiny Coke Coal");
        add(builder, "item", "hard_coal_tiny", "Tiny Hard Coal");
        add(builder, "itemGroup", "wrench", "Einholzscher Tech Mod");
        add(builder, "title", "wrench", "Set mode to %s");
        add(builder, "tooltip", "activation_button", "%s");
        add(builder, "tooltip", "coal_generator.heat_bar", "Heat: %s / %s / %s");
        add(builder, "tooltip", "machine.configuration_button", "Sideconfig");
        add(builder, "tooltip", "machine.power_bar_amount", "Power: %2$s / %3$s");
        add(builder, "tooltip", "machine.power_bar_trend", "%d%s");
        add(builder, "tooltip", "machine.progress_bar", "%2$s");
        add(builder, "tooltip", "wrench.withmode", "Mode: %s");
        add(builder, "tooltip", "wrench.withoutmode", "Sneak right click to set mode");
        // try {
        // builder.add(getPath());
        // } catch (NoSuchElementException | IOException e) {
        // MooshroomLib.LOGGER.errorBug("Failed to add existing language file for " +
        // code, e);
        // }
    }
}
