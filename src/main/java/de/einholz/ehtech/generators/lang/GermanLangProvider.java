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

public class GermanLangProvider extends CustomLangProvider {
    public GermanLangProvider(FabricDataGenerator generator, String code) {
        super(generator, code);
    }

    public void generateTranslations(TranslationBuilder builder) {
        add(builder, "block", "charcoal_block", "Holzkohleblock");
        add(builder, "block", "coal_generator", "Kohle Generator");
        add(builder, "block", "coke_coal_block", "Koksblock");
        add(builder, "block", "config.coal_generator_items", "Gegenstände (Kohle Generator)");
        add(builder, "block", "config.machine_electricity", "Elektrizität (Maschine)");
        add(builder, "block", "config.ore_grower", "Gegenstände (Erz Wachser)");
        add(builder, "block", "machine.configuration_button", "KON");
        add(builder, "block", "machine_frame_1", "Machinenrahmen Stufe 1");
        add(builder, "block", "ore_grower", "Erz Wachser");
        add(builder, "item", "wrench", "Schraubenschlüssel");
        add(builder, "item", "charcoal_tiny", "Winzige Holzkohle");
        add(builder, "item", "coke_coal_chunk", "Koks Stück");
        add(builder, "item", "coke_coal_tiny", "Winziges Koks");
        add(builder, "item", "hard_coal_tiny", "Winzige Steinkohle");
        add(builder, "itemGroup", "wrench", "Einholzscher Technik Mod");
        add(builder, "title", "wrench", "Modus wurde zu %s gesetzt");
        add(builder, "tooltip", "activation_button", "%s");
        add(builder, "tooltip", "coal_generator.heat_bar", "Hitze: %s / %s / %s");
        add(builder, "tooltip", "machine.configuration_button", "Seitenconfiguration");
        add(builder, "tooltip", "machine.power_bar_amount", "Strom: %2$s / %3$s");
        add(builder, "tooltip", "machine.power_bar_trend", "%d%s");
        add(builder, "tooltip", "machine.progress_bar", "%2$s");
        add(builder, "tooltip", "wrench.withmode", "Modus: %s");
        add(builder, "tooltip", "wrench.withoutmode", "Schleich rechtsklicke um Modus zu setzen");
        // try {
        // builder.add(getPath());
        // } catch (NoSuchElementException | IOException e) {
        // MooshroomLib.LOGGER.errorBug("Failed to add existing language file for " +
        // code, e);
        // }
    }
}
