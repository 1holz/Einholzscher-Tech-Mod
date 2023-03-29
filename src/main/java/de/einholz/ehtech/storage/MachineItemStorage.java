package de.einholz.ehtech.storage;

import de.einholz.ehmooshroom.storage.AdvInventory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MachineItemStorage extends AdvInventory {
    public static InventoryStorage of() {
        return InventoryStorage.of(new MachineItemStorage(), null);
    }

    /*
     * 0: electricity input
     * 1: electricity output
     * 2: upgrade
     * 3: network
     */
    public MachineItemStorage() {
        this(0);
    }


    public MachineItemStorage(int add) {
        super(add + 4);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        switch (slot) {
            case 0, 1:
                return Items.BEDROCK.equals(stack.getItem());
            case 2, 3:
                return false;
            default:
                return super.isValid(slot, stack);
        }
    }
}
