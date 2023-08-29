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

package de.einholz.ehtech.storage.storages;

import de.einholz.ehmooshroom.storage.storages.SingleBlockStorage;
import de.einholz.ehmooshroom.storage.variants.BlockVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

// based on how CauldronStorage works
public class OreGrowerBlockStorage extends SingleBlockStorage {
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

    @Override
    public void writeNbt(NbtCompound nbt) {
    }

    @Override
    public void readNbt(NbtCompound nbt) {
    }
}
