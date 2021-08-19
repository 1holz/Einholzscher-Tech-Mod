package de.einholz.ehtech;

import de.alberteinholz.ehmooshroom.util.Helper;
import de.alberteinholz.ehmooshroom.util.LoggerHelper;
import de.einholz.ehtech.registry.Registry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class TechMod implements ModInitializer, ClientModInitializer {
	public static final Helper HELPER = new Helper("ehtech");
	public static final LoggerHelper LOGGER = new LoggerHelper(HELPER.MOD_ID, "https://github.com/Albert-Einholz/Einholzscher-Tech-Mod/issues");

	@Override
	public void onInitialize() {
		Registry.register();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		
	}
}
