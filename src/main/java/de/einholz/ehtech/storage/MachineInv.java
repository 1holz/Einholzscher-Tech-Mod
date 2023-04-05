package de.einholz.ehtech.storage;

import de.einholz.ehmooshroom.storage.AdvInv;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MachineInv extends AdvInv {
    public static final int SIZE = AdvInv.SIZE + 4;
    public static final int ELECTRIC_IN = AdvInv.SIZE + 0;
    public static final int ELECTRIC_OUT = AdvInv.SIZE + 1;
    public static final int UPGRADE = AdvInv.SIZE + 2;
    public static final int NETWORK = AdvInv.SIZE + 3;

    public static InventoryStorage of() {
        return InventoryStorage.of(new MachineInv(), null);
    }

    public MachineInv() {
        this(0);
    }

    public MachineInv(int add) {
        super(add + SIZE);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        switch (slot) {
            case ELECTRIC_IN, ELECTRIC_OUT:
                return Items.BEDROCK.equals(stack.getItem());
            case UPGRADE, NETWORK:
                return false;
            default:
                return super.isValid(slot, stack);
        }
    }
}
