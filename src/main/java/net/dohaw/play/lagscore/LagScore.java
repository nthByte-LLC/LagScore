package net.dohaw.play.lagscore;

import me.c10coding.coreapi.BetterJavaPlugin;
import net.dohaw.play.lagscore.commands.LagScoreCommands;
import net.dohaw.play.lagscore.events.PlayerWatcher;
import net.dohaw.play.lagscore.files.PlayerDataConfig;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.play.lagscore.runnables.ScoreAdjuster;
import net.dohaw.play.lagscore.runnables.TPSChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public final class LagScore extends BetterJavaPlugin {

    public Storage storage;
    private BukkitRunnable scoreAdjuster;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles("config.yml", "playerData.yml");

        this.storage = new Storage(this);

        startRunnables();
        registerEvents(new PlayerWatcher(storage));
        registerCommand("lagscore", new LagScoreCommands(storage));
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

        double checkSpeed = storage.baseConfig.getCheckSpeed();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSChecker(), 100L, 1L);

        this.scoreAdjuster = new ScoreAdjuster(storage);
        scoreAdjuster.runTaskTimer(this, 100L, (long) (checkSpeed * 20));

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

    public BukkitRunnable getScoreAdjuster(){
        return scoreAdjuster;
    }

    public void setScoreAdjuster(ScoreAdjuster scoreAdjuster){
        this.scoreAdjuster = scoreAdjuster;
    }

}
