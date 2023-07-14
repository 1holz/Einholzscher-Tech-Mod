package de.einholz.ehtech.block.entity;

import de.einholz.ehmooshroom.recipe.AdvRecipe;
import de.einholz.ehmooshroom.registry.TransferablesReg;
import de.einholz.ehmooshroom.storage.AdvInv;
import de.einholz.ehmooshroom.storage.storages.AdvItemStorage;
import de.einholz.ehmooshroom.storage.storages.HeatStorage;
import de.einholz.ehtech.TechMod;
import de.einholz.ehtech.gui.gui.CoalGeneratorGui;
import de.einholz.ehtech.registry.Registry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.ExtendedClientHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CoalGeneratorBE extends MachineBE {
    public static final Identifier COAL_IN = TechMod.HELPER.makeId("coal_in");
    public static final Identifier COAL_GENERATOR_ITEMS = TechMod.HELPER.makeId("coal_generator_items");
    public static final Identifier COAL_GENERATOR_HEAT = TechMod.HELPER.makeId("coal_generator_heat");

    public CoalGeneratorBE(BlockPos pos, BlockState state) {
        this(Registry.COAL_GENERATOR.BLOCK_ENTITY_TYPE, pos, state, CoalGeneratorGui::init);
    }

    public CoalGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState state, ExtendedClientHandlerFactory<? extends ScreenHandler> clientHandlerFactory) {
        super(type, pos, state, clientHandlerFactory);
        getStorageMgr().withStorage(COAL_GENERATOR_ITEMS, TransferablesReg.ITEMS, makeItemStorage());
        getStorageMgr().withStorage(COAL_GENERATOR_HEAT, TransferablesReg.HEAT, new HeatStorage(this));

        //getConfigComp().setConfigAvailability(new Identifier[] {getFirstInputInvComp().getId()}, new ConfigBehavior[] {ConfigBehavior.SELF_INPUT, ConfigBehavior.FOREIGN_INPUT}, null, true);
    }

    public Inventory getCoalGeneratorInv() {
        return ((AdvItemStorage) getStorageMgr().getEntry(COAL_GENERATOR_ITEMS).storage).getInv();
    }

    public HeatStorage getCoalGeneratorHeat() {
        return (HeatStorage) getStorageMgr().getEntry(COAL_GENERATOR_HEAT).storage;
    }

    @Override
    public void operate() {
        super.operate();
        // originally 0.1 instead of 1
        if (!getCoalGeneratorHeat().isResourceBlank() && !getMachineElectricity().isFull())
            getMachineElectricity().increase((long) ((getCoalGeneratorHeat().getAmount() * 3 + 1) * getEfficiency()));
    }

    @Override
    public void idle() {
        super.idle();
        // originally 0.1 instead of 1
        if (!getCoalGeneratorHeat().isResourceBlank()) getCoalGeneratorHeat().decrease(1);
    }

    @Override
    public RecipeType<AdvRecipe> getRecipeType() {
        return Registry.COAL_GENERATOR.RECIPE_TYPE;
    }

    private AdvItemStorage makeItemStorage() {
        AdvItemStorage storage = new AdvItemStorage(this, COAL_IN);
        ((AdvInv) storage.getInv()).setAccepter((stack) -> true, COAL_IN);
        return storage;
    }
}
