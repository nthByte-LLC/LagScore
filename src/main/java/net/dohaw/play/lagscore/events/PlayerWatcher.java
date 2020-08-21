package net.dohaw.play.lagscore.events;

import net.dohaw.play.lagscore.Storage;
import net.dohaw.play.lagscore.files.PlayerDataConfig;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerWatcher implements Listener {

    private Storage storage;
    private PlayerDataConfig playerDataConfig;
    private PlayerDataHolder playerDataHolder;

    public PlayerWatcher(Storage storage){
        this.storage = storage;
        this.playerDataConfig = storage.playerDataConfig;
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

}
