package net.dohaw.lagscore.playerdata;

import net.dohaw.lagscore.LagScore;
import net.dohaw.lagscore.files.PlayerDataConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataHolder {

    private List<PlayerData> data = new ArrayList<>();
    private PlayerDataConfig playerDataConfig;

    public PlayerDataHolder(LagScore plugin){
        this.playerDataConfig = plugin.getPlayerDataConfig();
    }

    public List<PlayerData> getData(){
        return data;
    }

    public PlayerData loadPlayerData(UUID uuid){
        PlayerData playerData = playerDataConfig.load(uuid);
        data.add(playerData);
        return playerData;
    }

    public PlayerData createPlayerData(UUID uuid){
        PlayerData playerData = playerDataConfig.create(uuid);
        data.add(playerData);
        return playerData;
    }

    public void unloadPlayerData(UUID uuid){
        PlayerData pd = getPlayerData(uuid);
        data.remove(pd);
    }

    public void savePlayerData(UUID uuid){
        playerDataConfig.save(getPlayerData(uuid));
    }

    public PlayerData getPlayerData(UUID uuid){
        for(PlayerData pd : data){
            if(pd.getUUID().equals(uuid)){
                return pd;
            }
        }
        return null;
    }

    public boolean hasLoadedData(UUID uuid){
        return getPlayerData(uuid) != null;
    }

    public void updatePlayerData(UUID uuid, PlayerData pd){
        PlayerData dataInList = getPlayerData(uuid);
        data.remove(dataInList);
        data.add(pd);
    }

}
