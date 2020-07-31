package de.alberteinholz.ehtech;

import org.apache.logging.log4j.LogManager;

import de.alberteinholz.ehtech.registry.Registry;
import de.alberteinholz.ehtech.util.LoggerHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class TechMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "ehtech";
	public static final LoggerHelper LOGGER = new LoggerHelper(LogManager.getLogger());

	@Override
	public void onInitialize() {
		Registry.registrerItems();
		Registry.registerBlocks();
        //BlockRegistryOld.registerBlocks();
		//ItemRegistryOld.registerItems();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
        //BlockRegistryOld.registerBlocksClient();
    }
}
