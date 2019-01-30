package net.artdude.mekatweaks.compat.jei;

import mekanism.api.gas.GasStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IIngredientType;
import net.artdude.mekatweaks.MekaTweaks;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;

@JEIPlugin
public class PluginMekanismTweaker implements IModPlugin {
    public static IIngredientBlacklist blacklist;
    public static IIngredientRegistry ingredientRegistry;
    public static IIngredientType<GasStack> gasIngredientType;
    public static IIngredientHelper<GasStack> ingredientHelper;

    @Override
    public void register(IModRegistry registry) {
        blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        ingredientRegistry = registry.getIngredientRegistry();
        gasIngredientType = ingredientRegistry.getIngredientType(GasStack.class);
        ingredientHelper = ingredientRegistry.getIngredientHelper(gasIngredientType);
    }

    @SideOnly(Side.CLIENT)
    public static void syncHiddenItems(EntityPlayer player) {
        if (player != null && player.getEntityWorld().isRemote) {
            // JEI only allows blacklisting from the main client thread.
            if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
                // Reschedules the sync to the correct thread.
                Minecraft.getMinecraft().addScheduledTask( () -> syncHiddenItems(player));
                return;
            }

            MekaTweaks.LOG.info("Syncing {} gasses with JEI!.", MekaTweaks.GAS_STAGES.size());
            final long time = System.currentTimeMillis();

            final Collection<GasStack> gasBlacklist = new ArrayList<>();
            final Collection<GasStack> gasWhitelist = new ArrayList<>();

            for (final String key : MekaTweaks.GAS_STAGES.keySet()) {
                if (GameStageHelper.clientHasStage(player, key)) {
                    gasWhitelist.addAll(MekaTweaks.GAS_STAGES.get(key));
                } else {
                    gasBlacklist.addAll(MekaTweaks.GAS_STAGES.get(key));
                }
            }

            if (!gasBlacklist.isEmpty()) {

                ingredientRegistry.removeIngredientsAtRuntime(gasIngredientType, gasBlacklist);
            }

            if (!gasWhitelist.isEmpty()) {

                ingredientRegistry.addIngredientsAtRuntime(gasIngredientType, gasWhitelist);
            }

            MekaTweaks.LOG.info("Finished JEI Sync, took " + (System.currentTimeMillis() - time) + "ms. "
                    + gasBlacklist.size() + " hidden, " + gasWhitelist.size() + " shown.");
        }
    }
}
