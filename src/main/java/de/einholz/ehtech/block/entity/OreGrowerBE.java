package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.recipe.Exgredient;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.AdvItemStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import de.einholz.ehtech.registry.Registry;
import de.einholz.ehtech.storage.MachineInv;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class OreGrowerBE extends MachineBE {
    public static final Identifier ORE_IN = TechMod.HELPER.makeId("ore_in");
    public static final Identifier ORE_GROWER_ITEMS = TechMod.HELPER.makeId("ore_grower_items");
    public static final Identifier ORE_GROWER_HEAT = TechMod.HELPER.makeId("ore_grtower_heat");

    public OreGrowerBE(BlockPos pos, BlockState state) {
        this(Registry.ORE_GROWER.BLOCK_ENTITY_TYPE, pos, state, OreGrowerGui::init);
    }

    public OreGrowerBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(ORE_GROWER_ITEMS, TransferablesReg.ITEMS, makeItemStorage());

        //getConfigComp().setConfigAvailability(new Identifier[] {getFirstInputInvComp().getId()}, new ConfigBehavior[] {ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    // XXX protected?
    public Inventory getrowerOreInv() {
        return ((AdvItemStorage) getStorageMgr().getEntry(ORE_GROWER_ITEMS).storage).getInv();
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

    @SuppressWarnings("unchecked")
    @Override
    public void task() {
        super.task();
        AdvRecipe recipe = getRecipe();
        Exgredient<Block> blockEx = null;
        for (Exgredient<?> ex : recipe.output) if (TransferablesReg.BLOCKS.equals(ex.getType())) {
            blockEx = (Exgredient<Block>) ex;
            break;
        }
        if (blockEx == null) {
            TechMod.LOGGER.smallBug(new NullPointerException("The recipe " + recipe.getId().toString() + " has no block output which is needed for the OreGrower to generate particles."));
            return;
        }
        BlockPos target = pos.offset(world.getBlockState(pos).get(Properties.FACING));
        // TODO Make particle amount configurable?
        for (int i = 0; i < 4; i++) {
            int side = world.random.nextInt(5);
            double x = side == 0 ? 0 : side == 1 ? 1 : world.random.nextDouble();
            double y = side == 2 ? 0 : side == 3 ? 1 : world.random.nextDouble();
            double z = side == 4 ? 0 : side == 5 ? 1 : world.random.nextDouble();
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockEx.getOutput().getDefaultState()), target.getX() + x, target.getY() + y, target.getZ() + z, 0.1, 0.1, 0.1);
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

    @Override
    public RecipeType<AdvRecipe> getRecipeType() {
        return Registry.ORE_GROWER.RECIPE_TYPE;
    }

    private static AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(ORE_IN);
        ((AdvInv) storage.getInv()).setAccepter((stack) -> true, ORE_IN);
        return storage;
    }

    @Deprecated // TODO del
    public static class OreGrowerInv extends MachineInv {
        public static final int SIZE = 0 + 1;
        public static final int ORE_IN = SIZE - 1;

        public static AdvItemStorage of() {
            return new AdvItemStorage();
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
