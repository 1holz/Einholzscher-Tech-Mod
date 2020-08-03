package de.alberteinholz.ehtech;

import de.alberteinholz.ehmooshroom.util.LoggerHelper;
import de.alberteinholz.ehtech.registry.Registry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class TechMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "ehtech";
	public static final LoggerHelper LOGGER = new LoggerHelper(MOD_ID, "https://github.com/Albert-Einholz/Einholzscher-Tech-Mod/issues");

	@Override
	public void onInitialize() {
		Registry.register();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {}
}
