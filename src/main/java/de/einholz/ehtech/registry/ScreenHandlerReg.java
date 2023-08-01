package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.gui.screen.ContainerScreen;
import de.einholz.ehmooshroom.registry.ScreenHandlerRegistry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.CoalGeneratorGui;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens.Provider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerReg<T extends ScreenHandler> extends ScreenHandlerRegistry<T> {
    @SuppressWarnings("unchecked")
    public static final ScreenHandlerType<CoalGeneratorGui> COAL_GENERATOR = (ScreenHandlerType<CoalGeneratorGui>) new ScreenHandlerReg<CoalGeneratorGui>()
            .register("coal_generator", ExtendedScreenHandlerType::new, CoalGeneratorGui::init)
            .withScreen((Provider<CoalGeneratorGui, ContainerScreen<CoalGeneratorGui>>) ContainerScreen::new)
            .get();
    @SuppressWarnings("unchecked")
    public static final ScreenHandlerType<OreGrowerGui> ORE_GROWER = (ScreenHandlerType<OreGrowerGui>) new ScreenHandlerReg<OreGrowerGui>()
            .register("ore_grower", ExtendedScreenHandlerType::new, OreGrowerGui::init)
            .withScreen((Provider<OreGrowerGui, ContainerScreen<OreGrowerGui>>) ContainerScreen::new)
            .get();

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
