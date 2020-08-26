package net.dohaw.play.lagscore.commands;

import net.dohaw.play.lagscore.LagScore;
import net.dohaw.play.lagscore.Storage;
import net.dohaw.play.lagscore.runnables.ScoreAdjuster;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class LagScoreCommands implements CommandExecutor {

    private Storage storage;

    public LagScoreCommands(Storage storage){
        this.storage = storage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("reload") && args.length == 1){

            storage.baseConfig.reloadConfig();
            restartScoreAdjuster();

            storage.api.getChatFactory().sendPlayerMessage("LagScore has been reloaded!", false, sender, null);
        }

        return false;
    }

    private void restartScoreAdjuster(){

        LagScore plugin = (LagScore) storage.plugin;
        BukkitRunnable scoreAdjuster = plugin.getScoreAdjuster();
        scoreAdjuster.cancel();

        double checkSpeed = storage.baseConfig.getCheckSpeed();

        ScoreAdjuster newScoreAdjuster = new ScoreAdjuster(storage);
        newScoreAdjuster.runTaskTimer(plugin, 0L, (long) (checkSpeed * 20) );
        plugin.setScoreAdjuster(newScoreAdjuster);
    }
}
