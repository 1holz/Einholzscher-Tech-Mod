package de.einholz.ehtech.item;

import java.util.List;

import de.einholz.ehmooshroom.block.DirectionalBlock;
import de.einholz.ehmooshroom.block.entity.ContainerBE;
import de.einholz.ehmooshroom.item.Tool;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Wrench extends Tool {
    public Wrench(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        System.out.println("Air");
        if (!user.isSneaking()) return super.use(world, user, hand);
        NbtCompound nbt = user.getStackInHand(hand).getOrCreateNbt();
        nbt.putString("Mode", WrenchMode.fromString(nbt.getString("Mode"), true).toString());
        if (world.isClient)
            ((ClientPlayerEntity) user).sendMessage((Text) (new TranslatableText("title.ehtech.wrench", user.getStackInHand(hand).getNbt().getString("Mode"))), true);
        return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        System.out.println("Block");
        ItemStack wrench = context.getStack();
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        Block block = state.getBlock();
        BlockEntity be = context.getWorld().getBlockEntity(context.getBlockPos());
        if(!context.getPlayer().isSneaking() || !(be instanceof ContainerBE) || !wrench.hasNbt() || !wrench.getNbt().contains("Mode", NbtType.STRING))
            return super.useOnBlock(context);
        WrenchMode mode = WrenchMode.fromString(wrench.getOrCreateNbt().getString("Mode"), false);
        if (mode == WrenchMode.ROTATE && block instanceof DirectionalBlock) context.getWorld().setBlockState(context.getBlockPos(), state.with(Properties.FACING, Direction.values()[(state.get(Properties.FACING).ordinal() + 1) % Direction.values().length]));
        else if (mode == WrenchMode.POWER && context.getWorld().isClient()) ((ClientPlayerEntity) context.getPlayer()).sendMessage(new TranslatableText("chat.ehtech.wip"), false);
        else if (mode == WrenchMode.ITEM && context.getWorld().isClient()) ((ClientPlayerEntity) context.getPlayer()).sendMessage(new TranslatableText("chat.ehtech.wip"), false);
        else if (mode == WrenchMode.FLUID && context.getWorld().isClient()) ((ClientPlayerEntity) context.getPlayer()).sendMessage(new TranslatableText("chat.ehtech.wip"), false);
        else if (mode == WrenchMode.CONFIGURE && !context.getWorld().isClient()) context.getPlayer().openHandledScreen((ContainerBE) context.getWorld().getBlockEntity(context.getBlockPos()));
        //else if (mode == WrenchMode.BREAK && context.getWorld().isClient()) ((ClientPlayerEntity) context.getPlayer()).sendMessage(new TranslatableText("chat.ehtech.wip"), false);
        else if (mode == WrenchMode.BLOCK_SPECIFIC && context.getWorld().isClient()) ((ClientPlayerEntity) context.getPlayer()).sendMessage(new TranslatableText("chat.ehtech.wip"), false);
        else return super.useOnBlock(context);
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (itemStack.getNbt() != null && itemStack.getNbt().getString("Mode") != null) 
            tooltip.add(new TranslatableText("tooltip.ehtech.wrench.withmode", itemStack.getNbt().getString("Mode")));
        else tooltip.add(new TranslatableText("tooltip.ehtech.wrench.withoutmode"));
    }

    public static enum WrenchMode {
        ROTATE,
        POWER,
        ITEM,
        FLUID,
        CONFIGURE,
        //BREAK,
        BLOCK_SPECIFIC;

        public WrenchMode next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public static WrenchMode fromString(String name, boolean next) {
            for (WrenchMode mode : values()) if (mode.name().equals(name.toUpperCase())) return next ? mode.next() : mode;
            return values()[0];
        }
    }
}
