package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.recipe.Exgredient;
import de.einholz.ehmooshroom.registry.TransferableRegistry;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.SideConfigType;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.SingleBlockStorage;
import de.einholz.ehmooshroom.storage.variants.BlockVariant;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import de.einholz.ehtech.registry.BlockEntityTypeReg;
import de.einholz.ehtech.registry.RecipeTypeReg;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
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
    public static final Identifier ORE_GROWER_BLOCK = TechMod.HELPER.makeId("ore_grower_block");

    public OreGrowerBE(BlockPos pos, BlockState state) {
        this(BlockEntityTypeReg.ORE_GROWER, pos, state, OreGrowerGui::init);
    }

    public OreGrowerBE(BlockEntityType<?> type, BlockPos pos, BlockState state,
            ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(ORE_GROWER_ITEMS, TransferableRegistry.ITEMS, makeItemStorage());
        getStorageMgr().withStorage(ORE_GROWER_BLOCK, TransferableRegistry.BLOCKS, new OreGrowerBlockStorage(this));
        getStorageMgr().getEntry(ORE_GROWER_ITEMS).change(SideConfigType.OUT_PROC);
        getStorageMgr().getEntry(ORE_GROWER_ITEMS).setAvailability(false, new SideConfigType[] {
                SideConfigType.SELF_OUT_D, SideConfigType.SELF_OUT_U, SideConfigType.SELF_OUT_N,
                SideConfigType.SELF_OUT_S, SideConfigType.SELF_OUT_W, SideConfigType.SELF_OUT_E,
                SideConfigType.FOREIGN_OUT_D, SideConfigType.FOREIGN_OUT_U, SideConfigType.FOREIGN_OUT_N,
                SideConfigType.FOREIGN_OUT_S, SideConfigType.FOREIGN_OUT_W, SideConfigType.FOREIGN_OUT_E
        });
        getStorageMgr().getEntry(ORE_GROWER_BLOCK).setAvailability(false, (SideConfigType[]) null);
    }

    public Inventory getOreGrowerInv() {
        return ((AdvItemStorage) getStorageMgr().getEntry(ORE_GROWER_ITEMS).storage).getInv();
    }

    public SingleBlockStorage getOreGrowerBlock() {
        return (SingleBlockStorage) getStorageMgr().getEntry(ORE_GROWER_BLOCK).storage;
    }

    @Override
    public boolean process() {
        // if (!getRecipe().containsBlockIngredients(getRecipe().input.blocks)) {
        // cancel();
        // return false;
        // }
        return super.process();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void task() {
        super.task();
        Exgredient<Block, BlockVariant> blockEx = null;
        for (Exgredient<?, ?> ex : getRecipe().output)
            if (TransferableRegistry.BLOCKS.equals(ex.getType())) {
                blockEx = (Exgredient<Block, BlockVariant>) ex;
                break;
            }
        if (blockEx == null) {
            TechMod.LOGGER.warnBug("The recipe", getRecipe().getId().toString(),
                    "has no block output which is needed for the OreGrower to generate particles.");
            return;
        }
        if (true)
            return;
        BlockPos target = getPos().offset(world.getBlockState(getPos()).get(Properties.FACING));
        // TODO Make particle amount configurable?
        for (int i = 0; i < 4; i++) {
            int side = world.random.nextInt(5);
            double x = side == 0 ? 0 : side == 1 ? 1 : world.random.nextDouble();
            double y = side == 2 ? 0 : side == 3 ? 1 : world.random.nextDouble();
            double z = side == 4 ? 0 : side == 5 ? 1 : world.random.nextDouble();
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockEx.getOutput().getDefaultState()),
                    target.getX() + x, target.getY() + y, target.getZ() + z, 0.1, 0.1, 0.1);
        }
    }

    @Override
    public void complete() {
        // world.setBlockState(pos.offset(world.getBlockState(pos).get(Properties.FACING)),
        // getRecipe().output.blocks[0]);
        super.complete();
    }

    @Override
    public RecipeType<AdvRecipe> getRecipeType() {
        return RecipeTypeReg.ORE_GROWER;
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, ORE_IN);
        ((AdvInv) storage.getInv()).setAccepter((stack) -> true, ORE_IN);
        return storage;
    }

    // based on how CauldronStorage works
    public static class OreGrowerBlockStorage extends SingleBlockStorage {
        private Block lastReleasedSnapshot;

        public OreGrowerBlockStorage(BlockEntity dirtyMarker) {
            super(dirtyMarker);
        }

        protected BlockPos getPos() {
            BlockPos bePos = getDirtyMarker().getPos();
            return bePos.offset(getDirtyMarker().getWorld().getBlockState(bePos).get(Properties.FACING));
        }

        @Override
        public long insert(BlockVariant insertedVariant, long maxAmount, TransactionContext transaction) {
            if (!supportsInsertion() || maxAmount < 1)
                return 0;
            updateSnapshots(transaction);
            getDirtyMarker().getWorld().setBlockState(getPos(), insertedVariant.getObject().getDefaultState(), 0);
            return 1;
        }

        @Override
        public long extract(BlockVariant extractedVariant, long maxAmount, TransactionContext transaction) {
            if (!supportsExtraction() || maxAmount < 1 || getResource() == null || isResourceBlank()
                    || !getResource().equals(extractedVariant))
                return 0;
            // getDirtyMarker().getWorld().setBlockState(getPos(),
            // BlockVariant.blank().getObject().getDefaultState(), 0);
            return 1;
        }

        @Override
        public BlockVariant getResource() {
            return new BlockVariant(getDirtyMarker().getWorld().getBlockState(getPos()).getBlock());
        }

        @Override
        public ResourceAmount<BlockVariant> createSnapshot() {
            if (isResourceBlank())
                return new ResourceAmount<BlockVariant>(BlockVariant.blank(), 0);
            return new ResourceAmount<BlockVariant>(getResource(), 1);
        }

        @Override
        protected void releaseSnapshot(ResourceAmount<BlockVariant> snapshot) {
            lastReleasedSnapshot = snapshot.resource().getObject();
            super.releaseSnapshot(snapshot);
        }

        @Override
        public void readSnapshot(ResourceAmount<BlockVariant> savedState) {
            getDirtyMarker().getWorld().setBlockState(getPos(), savedState.resource().getObject().getDefaultState(), 0);
        }

        @Override
        public void onFinalCommit() {
            Block block = createSnapshot().resource().getObject();
            if (lastReleasedSnapshot.equals(block))
                return;
            getDirtyMarker().getWorld().setBlockState(getPos(), lastReleasedSnapshot.getDefaultState(), 0);
            getDirtyMarker().getWorld().setBlockState(getPos(), block.getDefaultState());
        }
    }
}
