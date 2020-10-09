package net.dohaw.lagscore.events;

import net.dohaw.lagscore.LagScore;
import net.dohaw.lagscore.files.PlayerDataConfig;
import net.dohaw.lagscore.files.BaseConfig;
import net.dohaw.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.lagscore.runnables.TPSChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerWatcher implements Listener {

    private PlayerDataConfig playerDataConfig;
    private PlayerDataHolder playerDataHolder;
    private BaseConfig baseConfig;

    public PlayerWatcher(LagScore plugin){
        this.playerDataConfig = plugin.getPlayerDataConfig();
        this.playerDataHolder = plugin.getPlayerDataHolder();
        this.baseConfig = plugin.getBaseConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        UUID uuid = e.getPlayer().getUniqueId();
        if(!playerDataHolder.hasLoadedData(uuid)){
            if(playerDataConfig.hasRecordedData(uuid)){
                playerDataHolder.loadPlayerData(uuid);
            }else{
                playerDataHolder.createPlayerData(uuid);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        playerDataHolder.savePlayerData(uuid);
        playerDataHolder.unloadPlayerData(uuid);
    }

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e){

        double tps = TPSChecker.getTPS();
        double safeLine = baseConfig.getSafeLine();

        if(tps < safeLine){
            String denyMsg = baseConfig.getConnectionDenyMessage();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, denyMsg);
        }

    }

}
