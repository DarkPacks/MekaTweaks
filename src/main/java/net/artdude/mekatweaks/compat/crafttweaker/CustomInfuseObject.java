package net.artdude.mekatweaks.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mekanism.api.infuse.InfuseObject;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.mekatweaks.InfuseObject")
public class CustomInfuseObject {

    @ZenMethod
    public static void addInfuseType(String infuseName, IItemStack itemStack, @Optional(valueLong = 5) int worthValue) {
        // Check that the infuseName is valid and registered to Mekanism.
        InfuseType infuseType = InfuseRegistry.get(infuseName.toLowerCase());
        if (infuseType == null) {
            CraftTweakerAPI.logError(String.format("[Infuse Object] Infuse name %s was not found. Is this registered?", infuseName));

            return;
        }
        if (worthValue > 50) {
            worthValue = 5;
        }
        InfuseRegistry.registerInfuseObject(CraftTweakerMC.getItemStack(itemStack), new InfuseObject(infuseType, worthValue));
    }
}
