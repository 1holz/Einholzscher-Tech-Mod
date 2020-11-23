package de.alberteinholz.ehtech.blocks.blockentities.containers.machines.consumers;

import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent.ConfigBehavior;
import de.alberteinholz.ehmooshroom.registry.RegistryEntry;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.blocks.directionals.DirectionalBlock;
import de.alberteinholz.ehtech.blocks.recipes.Input;
import de.alberteinholz.ehtech.blocks.recipes.MachineRecipe;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class OreGrowerBlockEntity extends ConsumerBlockEntity {
    public OreGrowerBlockEntity() {
        this(RegistryHelper.getEntry(Helper.makeId("ore_grower")));
    }

    public OreGrowerBlockEntity(RegistryEntry registryEntry) {
        super(registryEntry);
        inventory.addSlots(Type.INPUT);
        getConfigComp().setConfigAvailability(new Identifier[]{ConfigType.ITEM}, new ConfigBehavior[]{ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    @Override
    public boolean process() {
        if (!containsBlockIngredients(((MachineRecipe) getMachineDataComp().getRecipe(world)).input.blocks)) {
            cancle();
            return false;
        } else return super.process();
    }

    @Override
    public void task() {
        super.task();
        MachineRecipe recipe = (MachineRecipe) getMachineDataComp().getRecipe(world);
        BlockPos target = pos.offset(world.getBlockState(pos).get(DirectionalBlock.FACING));
        //TODO: Make particle amount configurable
        for (int i = 0; i < 4; i++) {
            int side = world.random.nextInt(5);
            double x = side == 0 ? 0 : side == 1 ? 1 : world.random.nextDouble();
            double y = side == 2 ? 0 : side == 3 ? 1 : world.random.nextDouble();
            double z = side == 4 ? 0 : side == 5 ? 1 : world.random.nextDouble();
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, recipe.output.blocks[0]), target.getX() + x, target.getY() + y, target.getZ() + z, 0.1, 0.1, 0.1);
        }
    }

    @Override
    public void finish() {
        world.setBlockState(pos.offset(world.getBlockState(pos).get(DirectionalBlock.FACING)), ((MachineRecipe) getMachineDataComp().getRecipe(world)).output.blocks[0]);
        super.finish();
    }

    @Override
    public boolean containsBlockIngredients(Input.BlockIngredient... ingredients) {
        return ingredients[0].ingredient.contains(world.getBlockState(pos.offset(world.getBlockState(pos).get(DirectionalBlock.FACING))).getBlock());
    }
}