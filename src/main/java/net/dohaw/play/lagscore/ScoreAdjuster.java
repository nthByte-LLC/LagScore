package net.dohaw.play.lagscore;

import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ScoreAdjuster extends BukkitRunnable {

    private Storage storage;
    private PlayerDataHolder playerDataHolder;

    public ScoreAdjuster(Storage storage){
        this.storage = storage;
    }

    @Override
    public void run() {
        for(Player player : storage.plugin.getServer().getOnlinePlayers()){
            UUID uuid = player.getUniqueId();
            PlayerData pd = playerDataHolder.getPlayerData(uuid);
        }
    }

}
