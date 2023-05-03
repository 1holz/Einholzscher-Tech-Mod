package de.einholz.ehtech.registry;

import de.einholz.ehmooshroom.registry.RegEntryBuilder;
import de.einholz.ehmooshroom.registry.RegTemplates;
import de.einholz.ehtech.block.MachineBlock;
import de.einholz.ehtech.block.entity.MachineBE;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;

public class RegistryTemplates {
    public static <B extends BlockEntity, G extends ScreenHandler, S extends HandledScreen<G>, R extends Recipe<?>> RegEntryBuilder<B, G, S, R> machine(RegEntryBuilder<B, G, S, R> entry) {
        entry.withBlockRaw((blockEntry) -> new MachineBlock(blockEntry.getId(), MachineBE::tick));
        RegTemplates.container(entry);
        entry.withBlockItemBuild(new Item.Settings());
        return entry;
    }
}
