package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.registry.RegEntry;
import de.einholz.ehmooshroom.registry.RegEntryBuilder;
import de.einholz.ehmooshroom.util.LoggerHelper;
import de.einholz.ehtech.TechMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class RegistryEntryBuilder<B extends BlockEntity, G extends ScreenHandler, S extends HandledScreen<G>> extends RegEntryBuilder<B, G, S> {
    @Override
    protected LoggerHelper getLogger() {
        return TechMod.LOGGER;
    }

    @Override
    protected Function<String, Identifier> getEasyIdFactory() {
        return TechMod.HELPER::makeId;
    }

    @Override
    public RegEntry<B, G, S> build(String path) {
        return build(TechMod.HELPER.makeId(path));
    }
}
