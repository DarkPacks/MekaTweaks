package net.artdude.mekatweaks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.Files;
import com.google.gson.Gson;
import crafttweaker.mc1120.commands.CTChatCommand;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import net.artdude.mekatweaks.common.util.References;
import net.artdude.mekatweaks.compat.jei.PluginMekanismTweaker;
import net.artdude.mekatweaks.data.CustomInfuseType;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Mod(modid = References.modID, name = References.modName, version = References.modVersion,
        acceptedMinecraftVersions = References.mcVersion)
public class MekaTweaks {
    public static final Logger LOG = LogManager.getLogger(References.modID);

    private static final Gson GSON = new Gson();

    private static final File infuseTypes = new File(new File("config"), "mtInfuseTypes.json");

    public static final ListMultimap<String, GasStack> GAS_STAGES = ArrayListMultimap.create();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        if (infuseTypes.exists()) {
            try (BufferedReader reader = Files.newReader(infuseTypes, StandardCharsets.UTF_8)) {
                final CustomInfuseType[] infuseTypes = GSON.fromJson(reader, CustomInfuseType[].class);
                for (CustomInfuseType customType : infuseTypes) {
                    if (customType.getName().length() == 0) {
                        LOG.warn("[Infuse Register] Name is empty or null for an entry!");
                        continue;
                    }
                    InfuseType infuseType = new InfuseType(customType.getName().toLowerCase(), customType.getResourceLocation());
                    // Workaround for Locale support. As "non" locale strings get parsed as normal strings.
                    infuseType.unlocalizedName = String.format("%s (MT)", StringUtils.capitalise(customType.getName()));

                    LOG.info(String.format("[Infuse Register] Registering custom Infuse Type %s to Mekanism.", customType.getName()));
                    InfuseRegistry.registerInfuseType(infuseType);
                }
            } catch (final IOException err) {
                LOG.error("Could not read {}.", infuseTypes.getName());
                LOG.catching(err);
            }
        }

        CTChatCommand.registerCommand(new GasCommands());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientSync(StagesSyncedEvent event) {
        if (Loader.isModLoaded("jei")) {
            PluginMekanismTweaker.syncHiddenItems(event.getEntityPlayer());
        }
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onClientLoadComplete(FMLLoadCompleteEvent event) {
        // Add a resource reload listener to keep up to sync with JEI.
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(listener -> {
            if (Loader.isModLoaded("jei")) {
                LOG.info("Resyncing JEI info.");
                PluginMekanismTweaker.syncHiddenItems(Minecraft.getMinecraft().player);
            }
        });
    }
}
