package net.dohaw.play.lagscore;

import me.c10coding.coreapi.BetterJavaPlugin;
import net.dohaw.play.lagscore.events.PlayerWatcher;
import net.dohaw.play.lagscore.files.PlayerDataConfig;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.play.lagscore.runnables.DataSaver;
import net.dohaw.play.lagscore.runnables.ScoreAdjuster;
import net.dohaw.play.lagscore.runnables.TPSChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class LagScore extends BetterJavaPlugin {

    public Storage storage;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles("config.yml", "playerData.yml");

        this.storage = new Storage(this);

        startRunnables();
        registerEvents(new PlayerWatcher(storage));
        //Whenever the plugin is reloaded with plugman, it will reload the data for player's that are online on the server
        loadPlayerData();
    }

    @Override
    public void onDisable() {
        PlayerDataHolder pdh = storage.playerDataHolder;
        List<PlayerData> data = pdh.getData();
        for(PlayerData pd : data){
            pdh.savePlayerData(pd.getUUID());
        }
    }

    private void startRunnables(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSChecker(), 100L, 1L);
        new ScoreAdjuster(storage).runTaskTimer(this, 0L, 20L);
        //Data saves every 3 seconds
        new DataSaver(storage).runTaskTimer(this, 0L, 60L);
    }

    private void loadPlayerData(){
        PlayerDataHolder pdh = storage.playerDataHolder;
        PlayerDataConfig pdc = storage.playerDataConfig;
        for(Player p : Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();
            if(pdc.hasRecordedData(uuid)){
                storage.playerDataHolder.loadPlayerData(uuid);
            }else{
                pdh.createPlayerData(uuid);
            }
        }
    }

}
