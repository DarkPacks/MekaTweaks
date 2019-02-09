package net.artdude.mekatweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class GasCommands extends CraftTweakerCommand {
    public GasCommands() {
        super("gasses");
    }

    @Override
    protected void init() {
        setDescription(getClickableCommandText("\u00A72/ct " + subCommandName, "/ct " + subCommandName, true),
                getNormalMessage(" \u00A73Outputs a list of all " + subCommandName + "names in the game to the crafttweaker.log"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        CraftTweakerAPI.logCommand(subCommandName + ":");

        List<Gas> gasses = GasRegistry.getRegisteredGasses();
        for (Gas gas : gasses) {
            CraftTweakerAPI.logCommand("<gas:" + gas.getName().toLowerCase() + ">");
        }

        sender.sendMessage(getLinkToCraftTweakerLog("List of " + subCommandName + " generated;", sender));
    }
}
