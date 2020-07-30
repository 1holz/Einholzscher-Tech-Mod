package de.alberteinholz.ehtech.itemgroups;

import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.util.Helper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroups {
    public static final ItemGroup EH_TECH = FabricItemGroupBuilder.create(Helper.makeId("eh_tech")).icon(() -> new ItemStack(RegistryHelper.getEntry(Helper.makeId("wrench")).item)).build();
}