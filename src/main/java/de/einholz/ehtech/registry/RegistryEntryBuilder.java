package de.einholz.ehtech.registry;

import de.einholz.ehmooshroom.registry.RegEntry;
import de.einholz.ehmooshroom.registry.RegEntryBuilder;
import de.einholz.ehmooshroom.util.LoggerHelper;
import de.einholz.ehtech.TechMod;

public class RegistryEntryBuilder extends RegEntryBuilder {
    @Override
    protected LoggerHelper getLogger() {
        return TechMod.LOGGER;
    }

    @Override
    public RegEntry build(String path) {
        return build(TechMod.HELPER.makeId(path));
    }
}
