package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.item.Tool;
import de.einholz.ehmooshroom.registry.ItemRegistry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.item.Wrench;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;

public class ItemReg extends ItemRegistry {
    public static final Identifier WRENCH_GROUP_ID = TechMod.HELPER.makeId("wrench");

    public static final BlockItem CHARCOAL_BLOCK = (BlockItem) new ItemReg()
            .register("charcoal_block", BlockReg.CHARCOAL_BLOCK, new Settings())
            .withFuel(16000)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem COKE_COAL_BLOCK = (BlockItem) new ItemReg()
            .register("coke_coal_block", BlockReg.COKE_COAL_BLOCK, new Settings())
            .withFuel(32000)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem MACHINE_FRAME_1 = (BlockItem) new ItemReg()
            .register("machine_frame_1", BlockReg.MACHINE_FRAME_1, new Settings())
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem COAL_GENERATOR = (BlockItem) new ItemReg()
            .register("coal_generator", BlockReg.COAL_GENERATOR, new Settings())
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final BlockItem ORE_GROWER = (BlockItem) new ItemReg()
            .register("ore_grower", BlockReg.ORE_GROWER, new Settings())
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item HARD_COAL_TINY = ((ItemReg) new ItemReg()
            .register("hard_coal_tiny", Item::new))
            .withFuel(200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item CHARCOAL_TINY = ((ItemReg) new ItemReg()
            .register("charcoal_tiny", Item::new))
            .withFuel(200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item COKE_COAL_TINY = ((ItemReg) new ItemReg()
            .register("coke_coal_tiny", Item::new))
            .withFuel(400)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Item COKE_COAL_CHUNK = ((ItemReg) new ItemReg()
            .register("coke_coal_chunk", Item::new))
            .withFuel(3200)
            .withItemGroupAdd(WRENCH_GROUP_ID)
            .get();
    public static final Tool WRENCH = (Tool) ((ItemReg) new ItemReg()
            .register(WRENCH_GROUP_ID.getPath(), new Wrench(new Settings())))
            .withItemGroupSelf()
            .get();

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
