package de.alberteinholz.ehtech.registry;

import de.alberteinholz.ehtech.itemgroups.ItemGroups;
import de.alberteinholz.ehtech.items.Wrench;
import de.alberteinholz.ehtech.util.Helper;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Deprecated
public enum ItemRegistryOld {
	WRENCH;

	//static

	private static void setupAll() {
		WRENCH.setup(new Wrench(getStandardItemSettings()));
	}

	private static Settings getStandardItemSettings() {
        return new Settings().group(ItemGroups.EH_TECH);
	}
	
	//non static

	public Item item;

	public Identifier getIdentifier() {
		return Helper.makeIdentifier(this.toString().toLowerCase());
	}

	private void setup(Item item) {
		this.item = item;
	}

	public static void registerItems() {
		setupAll();
		for (ItemRegistryOld entry : ItemRegistryOld.values()) {
			Registry.register(Registry.ITEM, entry.getIdentifier(), entry.item);
		}
	}
}