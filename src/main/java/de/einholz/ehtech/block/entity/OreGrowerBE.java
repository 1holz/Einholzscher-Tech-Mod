/*
 * Copyright 2023 Einholz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.einholz.ehtech.block.entity;

import org.jetbrains.annotations.Nullable;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.recipe.Exgredient;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.SideConfigType;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.SingleBlockStorage;
import de.einholz.ehmooshroom.storage.variants.BlockVariant;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.OreGrowerGui;
import de.einholz.ehtech.registry.BlockEntityTypeReg;
import de.einholz.ehtech.registry.RecipeTypeReg;
import de.einholz.ehtech.storage.storages.OreGrowerBlockStorage;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class OreGrowerBE extends MachineBE {
    public static final Identifier ORE_IN = TechMod.HELPER.makeId("ore_in");
    public static final Identifier ORE_GROWER_ITEMS = TechMod.HELPER.makeId("ore_grower_items");
    public static final Identifier ORE_GROWER_BLOCK = TechMod.HELPER.makeId("ore_grower_block");
    private final BlockPos bugPos;
    private World bugWorld;

    public OreGrowerBE(BlockPos pos, BlockState state) {
        this(BlockEntityTypeReg.ORE_GROWER, pos, state, OreGrowerGui::init);
    }

    public OreGrowerBE(BlockEntityType<?> type, BlockPos pos, BlockState state,
            ExtendedFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        this.bugPos = pos;
        getStorageMgr().withStorage(ORE_GROWER_ITEMS, makeItemStorage());
        getStorageMgr().withStorage(ORE_GROWER_BLOCK, new OreGrowerBlockStorage(this));
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
        return ((AdvItemStorage) getStorageMgr().getEntry(ORE_GROWER_ITEMS).getStorage()).getInv();
    }

    public SingleBlockStorage getOreGrowerBlock() {
        return (SingleBlockStorage) getStorageMgr().getEntry(ORE_GROWER_BLOCK).getStorage();
    }

    @SuppressWarnings({ "unchecked", "resource" })
    @Override
    public void task() {
        super.task();
        Exgredient<Block, BlockVariant> blockEx = null;
        for (Exgredient<?, ?> ex : getRecipe().output)
            if (Registry.BLOCK_KEY.getValue().equals(ex.getTypeId())) {
                blockEx = (Exgredient<Block, BlockVariant>) ex;
                break;
            }
        if (blockEx == null) {
            TechMod.LOGGER.warnBug("The recipe", getRecipe().getId().toString(),
                    "has no block output which is needed for the OreGrower to generate particles.");
            return;
        }
        if (!hasWorld())
            return;
        BlockPos target = getPos().offset(getWorld().getBlockState(getPos()).get(Properties.FACING));
        // TODO Make particle amount configurable?
        for (int i = 0; i < 4; i++) {
            int side = getWorld().random.nextInt(5);
            double x = side == 0 ? 0 : side == 1 ? 1 : getWorld().random.nextDouble();
            double y = side == 2 ? 0 : side == 3 ? 1 : getWorld().random.nextDouble();
            double z = side == 4 ? 0 : side == 5 ? 1 : getWorld().random.nextDouble();
            getWorld().addParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, blockEx.getOutput().getDefaultState()),
                    target.getX() + x, target.getY() + y, target.getZ() + z, 0.1, 0.1, 0.1);
        }
    }

    @Override
    public RecipeType<AdvRecipe> getRecipeType() {
        return RecipeTypeReg.ORE_GROWER;
    }

    // otherwise it says the method would not exist. remove once the error is gone
    @Override
    public BlockPos getPos() {
        // try {
        // return super.getPos();
        // } catch (Throwable e) {
        return bugPos;
        // }
    }

    @Override
    @Nullable
    public World getWorld() {
        // try {
        // return super.getWorld();
        // } catch (Throwable e) {
        return bugWorld;
        // }
    }

    @Override
    public void setWorld(World world) {
        // try {
        // super.setWorld(world);
        // } finally {
        bugWorld = world;
        // }
    }

    @Override
    public boolean hasWorld() {
        // try {
        // return super.hasWorld();
        // } catch (Throwable e) {
        return getWorld() != null;
        // }
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, ORE_IN);
        ((AdvInv) storage.getInv()).setAccepter((stack) -> true, ORE_IN);
        return storage;
    }
}
