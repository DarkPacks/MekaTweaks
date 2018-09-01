package net.artdude.mekatweaks;

import com.google.common.io.Files;
import com.google.gson.Gson;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import net.artdude.mekatweaks.common.util.References;
import net.artdude.mekatweaks.data.CustomInfuseType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Mod(modid = References.modID, name = References.modName, version = References.modVersion,
        acceptedMinecraftVersions = References.mcVersion)
public class MekaTweaks {
    private static final Gson GSON = new Gson();

    private static final File infuseTypes = new File(new File("config"), "mtInfuseTypes.json");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logger logger = event.getModLog();

        if (infuseTypes.exists()) {
            try (BufferedReader reader = Files.newReader(infuseTypes, StandardCharsets.UTF_8)) {
                final CustomInfuseType[] infuseTypes = GSON.fromJson(reader, CustomInfuseType[].class);
                for (CustomInfuseType customType : infuseTypes) {
                    if (customType.getName().length() == 0) {
                        logger.warn("[Infuse Register] Name is empty or null for an entry!");
                        continue;
                    }
                    InfuseType infuseType = new InfuseType(customType.getName().toLowerCase(), customType.getResourceLocation());
                    // Workaround for Locale support. As "non" locale strings get parsed as normal strings.
                    infuseType.unlocalizedName = String.format("%s (MT)", StringUtils.capitalise(customType.getName()));

                    logger.info(String.format("[Infuse Register] Registering custom Infuse Type %s to Mekanism.", customType.getName()));
                    InfuseRegistry.registerInfuseType(infuseType);
                }
            } catch (final IOException err) {
                logger.error("Could not read {}.", infuseTypes.getName());
                logger.catching(err);
            }
        }
    }
}
