package de.alberteinholz.ehtech.itemgroups;

import de.alberteinholz.ehmooshroom.registry.ItemRegistryHelper;
import de.alberteinholz.ehtech.util.Helper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroups {
    public static final ItemGroup EH_TECH = FabricItemGroupBuilder.create(Helper.makeIdentifier("eh_tech")).icon(() -> new ItemStack(ItemRegistryHelper.ITEMS.get(Helper.makeIdentifier("wrench")).item)).build();
}