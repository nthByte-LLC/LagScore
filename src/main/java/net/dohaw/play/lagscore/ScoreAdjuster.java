package net.dohaw.play.lagscore;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreAdjuster extends BukkitRunnable {

    private Storage storage;

    public ScoreAdjuster(Storage storage){
        this.storage = storage;
    }

    @Override
    public void run() {
        for(Player player : storage.plugin.getServer().getOnlinePlayers()){

        }
    }

}
