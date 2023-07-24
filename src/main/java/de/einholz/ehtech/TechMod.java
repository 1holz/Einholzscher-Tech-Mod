package de.einholz.ehtech;

import de.einholz.ehmooshroom.util.Helper;
import de.einholz.ehmooshroom.util.LoggerHelper;
import de.einholz.ehtech.registry.BlockEntityTypeReg;
import de.einholz.ehtech.registry.BlockReg;
import de.einholz.ehtech.registry.ItemReg;
import de.einholz.ehtech.registry.RecipeSerializerReg;
import de.einholz.ehtech.registry.RecipeTypeReg;
import de.einholz.ehtech.registry.ScreenHandlerReg;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class TechMod implements ModInitializer, ClientModInitializer {
    public static final Helper HELPER = new Helper("ehtech");
    public static final LoggerHelper LOGGER = new LoggerHelper(HELPER.MOD_ID,
            "https://github.com/Albert-Einholz/Einholzscher-Tech-Mod/issues");

    @Override
    public void onInitialize() {
        BlockReg.registerAll();
        ItemReg.registerAll();
        BlockEntityTypeReg.registerAll();
        ScreenHandlerReg.registerAll();
        RecipeTypeReg.registerAll();
        RecipeSerializerReg.registerAll();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }
}
