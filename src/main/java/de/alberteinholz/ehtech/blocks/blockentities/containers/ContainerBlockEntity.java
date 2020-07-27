package de.alberteinholz.ehtech.blocks.blockentities.containers;

import de.alberteinholz.ehmooshroom.registry.BlockRegistryEntry;
import de.alberteinholz.ehtech.blocks.components.container.ContainerDataProviderComponent;
import de.alberteinholz.ehtech.blocks.components.container.ContainerInventoryComponent;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ContainerBlockEntity extends BlockEntity implements BlockEntityClientSerializable, ExtendedScreenHandlerFactory {
    protected final BlockRegistryEntry registryEntry;
    public ContainerInventoryComponent inventory = initializeInventoryComponent();
    public ContainerDataProviderComponent data = initializeDataProviderComponent();

    public ContainerBlockEntity(BlockRegistryEntry registryEntry) {
        super(registryEntry.blockEntityType);
        this.registryEntry = registryEntry;
        inventory.setDataProvider(data);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (world != null) {
            if (tag.contains("Inventory", NbtType.COMPOUND)) {
                inventory.fromTag(tag.getCompound("Inventory"));
            }
            if (tag.contains("Data", NbtType.COMPOUND)) {
                data.fromTag(tag.getCompound("Data"));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (world != null) {
            CompoundTag inventoryTag = new CompoundTag();
            inventory.toTag(inventoryTag);
            if (!inventoryTag.isEmpty()) {
                tag.put("Inventory", inventoryTag);
            }
            CompoundTag dataTag = new CompoundTag();
            data.toTag(dataTag);
            if (!dataTag.isEmpty()) {
                tag.put("Data", dataTag);
            }
        }
        return tag;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(world.getBlockState(pos), tag);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(data.containerName.getLabel().asString());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        writeScreenOpeningData((ServerPlayerEntity) player, buf);
        return registryEntry.clientHandlerFactory.create(syncId, playerInv, buf);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    protected ContainerInventoryComponent initializeInventoryComponent() {
        return new ContainerInventoryComponent();
    }

    protected ContainerDataProviderComponent initializeDataProviderComponent() {
        return new ContainerDataProviderComponent("block.ehtech.container");
    }
}