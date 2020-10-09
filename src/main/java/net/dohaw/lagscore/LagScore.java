package net.dohaw.lagscore;

import lombok.Getter;
import net.dohaw.corelib.CoreLib;
import net.dohaw.lagscore.commands.LagScoreCommands;
import net.dohaw.lagscore.events.PlayerWatcher;
import net.dohaw.lagscore.files.BaseConfig;
import net.dohaw.lagscore.files.PlayerDataConfig;
import net.dohaw.lagscore.playerdata.PlayerData;
import net.dohaw.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.lagscore.runnables.ScoreAdjuster;
import net.dohaw.lagscore.runnables.TPSChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static net.dohaw.corelib.JPUtils.*;

public final class LagScore extends JavaPlugin {

    private BukkitRunnable scoreAdjuster;
    @Getter private PlayerDataHolder playerDataHolder;
    @Getter private PlayerDataConfig playerDataConfig;
    @Getter private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        CoreLib.setInstance(this);
        validateFiles("config.yml", "playerData.yml");

        this.baseConfig = new BaseConfig(this);
        this.playerDataConfig = new PlayerDataConfig(this);
        this.playerDataHolder = new PlayerDataHolder(this);
        startRunnables();

        registerEvents(new PlayerWatcher(this));
        registerCommand("lagscore", new LagScoreCommands(this));
        //Whenever the plugin is reloaded with plugman, it will reload the data for player's that are online on the server
        loadPlayerData();
    }

    @Override
    public void onDisable() {
        List<PlayerData> data = playerDataHolder.getData();
        for(PlayerData pd : data){
            playerDataHolder.savePlayerData(pd.getUUID());
        }
    }

    private void startRunnables(){

        double checkSpeed = baseConfig.getCheckSpeed();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSChecker(), 100L, 1L);

        this.scoreAdjuster = new ScoreAdjuster(this);
        scoreAdjuster.runTaskTimer(this, 100L, (long) (checkSpeed * 20));

    }

    private void loadPlayerData(){
        for(Player p : Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();
            if(playerDataConfig.hasRecordedData(uuid)){
                playerDataHolder.loadPlayerData(uuid);
            }else{
                playerDataHolder.createPlayerData(uuid);
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
