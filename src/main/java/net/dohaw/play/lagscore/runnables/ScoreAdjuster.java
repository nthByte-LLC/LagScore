package net.dohaw.play.lagscore.runnables;

import net.dohaw.play.lagscore.Storage;
import net.dohaw.play.lagscore.files.BaseConfig;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreAdjuster extends BukkitRunnable {

    private Storage storage;
    private PlayerDataHolder playerDataHolder;
    private BaseConfig baseConfig;
    private List<String> kickedPlayers = new ArrayList<>();

    public ScoreAdjuster(Storage storage){
        this.storage = storage;
        this.playerDataHolder = storage.playerDataHolder;
        this.baseConfig = storage.baseConfig;
    }

    @Override
    public void run() {

        for(Player player : storage.plugin.getServer().getOnlinePlayers()){

            UUID uuid = player.getUniqueId();
            PlayerData pd = playerDataHolder.getPlayerData(uuid);
            double tps = TPSChecker.getTPS();
            double score = pd.getScore();

            double scoreLine = baseConfig.getScoreLine();
            double safeLine = baseConfig.getSafeLine();

            double newScore;
            if(tps < scoreLine){
                double hurtFactor = baseConfig.getHurtFactor();
                newScore = score + (( 20 - score ) * hurtFactor * (scoreLine - tps));
            }else{
                double healFactor = baseConfig.getHealFactor();
                newScore = score - (( score ) * healFactor * (tps - scoreLine));
            }

            if(newScore >= 0){
                pd.setScore(newScore);
                playerDataHolder.setPlayerData(uuid, pd);
            }

            if(tps < newScore && tps < safeLine){
                String kickMsg = baseConfig.getKickMessage();
                player.kickPlayer(kickMsg);
            }

        }

    }

}
