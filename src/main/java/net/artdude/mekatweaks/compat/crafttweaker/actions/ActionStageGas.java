package net.artdude.mekatweaks.compat.crafttweaker.actions;

import crafttweaker.IAction;
import mekanism.api.gas.GasStack;
import mekanism.common.integration.crafttweaker.gas.IGasStack;
import mekanism.common.integration.crafttweaker.helpers.GasHelper;
import net.artdude.mekatweaks.MekaTweaks;

public class ActionStageGas implements IAction {

    private final IGasStack stack;
    private final String stage;

    public ActionStageGas(String stage, IGasStack stack) {
        this.stage = stage;
        this.stack = stack;
    }

    @Override
    public void apply() {
        if (this.stack == null) {
            throw new IllegalArgumentException("Could not stage null gas");
        }

        final GasStack gas = GasHelper.toGas(this.stack);
        MekaTweaks.GAS_STAGES.put(this.stage, gas);
    }

    @Override
    public String describe() {
        return "Staging gas " + this.stack.getName() + " to " + this.stage;
    }
}
