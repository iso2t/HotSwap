package com.iso2t.hotswap;

import com.iso2t.hotswap.config.ModConfig;
import com.iso2t.hotswap.platform.Services;
import com.iso2t.superconfig.manager.ConfigManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HotSwap {

    public static final boolean IS_ALPHA = false;

    public static ModConfig CONFIG;

    public static void init() {
        if (Services.PLATFORM.isModLoaded("hotswap")) {
            Constants.LOG.info("Initializing HotSwap...");
            logAction("HotSwap is running in verbose mode. Did you mean to enable this?");
        }
    }

    public static void logAction (String message, Object... args) {
        if (CONFIG.DEBUG.DEBUG_LOGGING.get()) {
            var logLevel = CONFIG.DEBUG.LOG_LEVEL.get();
            switch (logLevel) {
                case OFF -> {}
                case ERROR -> Constants.LOG.error(message, args);
                case WARN -> Constants.LOG.warn(message, args);
                case INFO -> Constants.LOG.info(message, args);
                case DEBUG -> Constants.LOG.debug(message, args);
            }
        }
    }

    static {
        Path configPath = Paths.get("config", Constants.MOD_ID + ".json5");
        ConfigManager<ModConfig> configManager = new ConfigManager<>(ModConfig.class, configPath);
		try {
			CONFIG = configManager.load();
            configManager.save(CONFIG);
		} catch (IOException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
    }
}