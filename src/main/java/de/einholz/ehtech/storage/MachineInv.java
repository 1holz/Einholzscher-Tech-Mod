package de.einholz.ehtech.storage;

import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.AdvItemStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Deprecated // TODO del
public class MachineInv extends AdvInv {
    public static final int SIZE = 0 + 4;
    public static final int ELECTRIC_IN = SIZE - 4;
    public static final int ELECTRIC_OUT = SIZE - 3;
    public static final int UPGRADE = SIZE - 2;
    public static final int NETWORK = SIZE - 1;

    public static AdvItemStorage of(BlockEntity dirtyMarker) {
        return new AdvItemStorage(dirtyMarker);
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
