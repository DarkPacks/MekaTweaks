package net.artdude.mekatweaks.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import mekanism.common.integration.crafttweaker.gas.IGasStack;
import net.artdude.mekatweaks.compat.crafttweaker.actions.ActionStageGas;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.mekatweaks.GasStages")
public class GasStages {
    @ZenMethod
    public static void addGasStage(String stage, IGasStack gas) {
        CraftTweakerAPI.apply(new ActionStageGas(stage, gas));
    }
}
