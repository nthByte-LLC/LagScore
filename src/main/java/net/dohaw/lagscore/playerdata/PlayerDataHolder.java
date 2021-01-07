package net.dohaw.lagscore.playerdata;

import net.dohaw.lagscore.LagScore;
import net.dohaw.lagscore.files.PlayerDataConfig;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class PlayerDataHolder {

    private Map<UUID, PlayerData> data = new HashMap<>();
    private PlayerDataConfig playerDataConfig;

    public PlayerDataHolder(LagScore plugin){
        this.playerDataConfig = plugin.getPlayerDataConfig();
    }

    public Map<UUID, PlayerData> getData(){
        return data;
    }

    public PlayerData loadPlayerData(UUID uuid){
        PlayerData playerData = playerDataConfig.load(uuid);
        data.put(uuid, playerData);
        return playerData;
    }

    public PlayerData createPlayerData(UUID uuid){
        PlayerData playerData = playerDataConfig.create(uuid);
        data.put(uuid, playerData);
        return playerData;
    }

    public void unloadPlayerData(UUID uuid){
        data.remove(uuid);
    }

    public void savePlayerData(UUID uuid){
        playerDataConfig.save(getPlayerData(uuid));
    }

    public void savePlayerData(PlayerData pd){
        playerDataConfig.save(pd);
    }

    public void saveAllData(){
        for(Map.Entry<UUID, PlayerData> entry : data.entrySet()){
            PlayerData pd = entry.getValue();
            savePlayerData(pd);
        }
    }

    public PlayerData getPlayerData(UUID uuid){
        return data.get(uuid);
    }

    public boolean hasLoadedData(UUID uuid){
        return data.containsKey(uuid);
    }

    public void updatePlayerData(PlayerData pd){
        data.replace(pd.getUUID(), pd);
    }

}
