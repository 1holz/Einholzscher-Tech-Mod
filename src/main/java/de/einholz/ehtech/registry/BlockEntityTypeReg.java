package de.einholz.ehtech.registry;

import java.util.function.Function;

import de.einholz.ehmooshroom.registry.BlockEntityTypeRegistry;
import de.einholz.ehmooshroom.registry.TransferableRegistry;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.block.entity.CoalGeneratorBE;
import de.einholz.ehtech.block.entity.OreGrowerBE;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class BlockEntityTypeReg<T extends BlockEntity> extends BlockEntityTypeRegistry<T> {
    public static final BlockEntityType<?> COAL_GENERATOR = ((BlockEntityTypeReg<BlockEntity>) new BlockEntityTypeReg<>()
            .register("coal_generator", CoalGeneratorBE::new))
            .withMachine()
            .get();
    public static final BlockEntityType<?> ORE_GROWER = ((BlockEntityTypeReg<BlockEntity>) new BlockEntityTypeReg<>()
            .register("ore_grower", OreGrowerBE::new))
            .withMachine()
            .get();

    public BlockEntityTypeReg<T> withMachine() {
        return (BlockEntityTypeReg<T>) withBlockApiLookup(TransferableRegistry.ITEMS, TransferableRegistry.ELECTRICITY);
    }

    @Override
    protected Function<String, Identifier> idFactory() {
        return TechMod.HELPER::makeId;
    }

    public static void registerAll() {
    }
}
