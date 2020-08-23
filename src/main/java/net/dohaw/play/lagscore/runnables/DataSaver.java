package net.dohaw.play.lagscore.runnables;

import net.dohaw.play.lagscore.Storage;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DataSaver extends BukkitRunnable {

    private PlayerDataHolder playerDataHolder;

    public DataSaver(Storage storage){
        this.playerDataHolder = storage.playerDataHolder;
    }

    @Override
    public void run() {
        List<PlayerData> playerData = playerDataHolder.getData();
        for(PlayerData pd : playerData){
            playerDataHolder.savePlayerData(pd.getUUID());
        }
    }
}
