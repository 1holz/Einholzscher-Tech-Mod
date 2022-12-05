package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.registry.RegEntryBuilder;
import de.einholz.ehtech.block.MachineBlock;

public class RegistryTemplates {
    public static final Function<RegEntryBuilder, RegEntryBuilder> MACHINE = (entry) -> entry.withBlockRaw((block_entry) -> new MachineBlock(block_entry.getId()));
}
