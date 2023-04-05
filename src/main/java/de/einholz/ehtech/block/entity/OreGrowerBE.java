package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehtech.registry.Registry;
import de.einholz.ehtech.storage.MachineInv;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

public class OreGrowerBE extends MachineBE {
    public OreGrowerBE() {
        this(Registry.ORE_GROWER.BLOCK_ENTITY_TYPE, null, null, null);
    }

    public OreGrowerBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(TransferablesReg.ITEMS, OreGrowerInv.of());

        //getConfigComp().setConfigAvailability(new Identifier[] {getFirstInputInvComp().getId()}, new ConfigBehavior[] {ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    /* TODO del
    public AdvancedInventoryComponent getFirstInputInvComp() {
        return (AdvancedInventoryComponent) getImmutableComps().get(TechMod.HELPER.makeId("ore_grower_input_inv_1"));
    }
    */

    @Override
    public boolean process() {
        //if (!getRecipe(). containsBlockIngredients(getRecipe().input.blocks)) {
        //    cancel();
        //    return false;
        //}
        return super.process();
    }

    @Override
    public void task() {
        super.task();
        AdvRecipe recipe = getRecipe();
        BlockPos target = pos.offset(world.getBlockState(pos).get(Properties.FACING));
        // TODO Make particle amount configurable?
        for (int i = 0; i < 4; i++) {
            int side = world.random.nextInt(5);
            double x = side == 0 ? 0 : side == 1 ? 1 : world.random.nextDouble();
            double y = side == 2 ? 0 : side == 3 ? 1 : world.random.nextDouble();
            double z = side == 4 ? 0 : side == 5 ? 1 : world.random.nextDouble();
            //world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, recipe.output.blocks[0]), target.getX() + x, target.getY() + y, target.getZ() + z, 0.1, 0.1, 0.1);
        }
    }

    @Override
    public void complete() {
        //world.setBlockState(pos.offset(world.getBlockState(pos).get(Properties.FACING)), getRecipe().output.blocks[0]);
        super.complete();
    }

    //@Override
    //public boolean containsBlockIngredients(Ingredient<Block>... ingredients) {
    //    return ingredients[0].ingredient.contains(world.getBlockState(pos.offset(world.getBlockState(pos).get(Properties.FACING))).getBlock());
    //}


    public static class OreGrowerInv extends MachineInv {
        public static final int SIZE = MachineInv.SIZE + 1;
        public static final int ORE_IN = MachineInv.SIZE + 0;

        public static InventoryStorage of() {
            return InventoryStorage.of(new OreGrowerInv(), null);
        }

        public OreGrowerInv() {
            this(0);
        }

        public OreGrowerInv(int add) {
            super(add + SIZE);
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            switch (slot) {
                case ORE_IN:
                    return true; // TODO check if part of recipe. probably requires data generators
                default:
                    return super.isValid(slot, stack);
            }
        }
    }
}
