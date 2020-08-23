package net.dohaw.play.lagscore.playerdata;

import net.dohaw.play.lagscore.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataHolder {

    private List<PlayerData> data = new ArrayList<>();
    private Storage storage;

    public PlayerDataHolder(Storage storage){
        this.storage = storage;
    }

    public PlayerData loadPlayerData(UUID uuid){
        PlayerData playerData = storage.playerDataConfig.load(uuid);
        data.add(playerData);
        return playerData;
    }

    public PlayerData createPlayerData(UUID uuid){
        PlayerData playerData = storage.playerDataConfig.create(uuid);
        data.add(playerData);
        return playerData;
    }

    public void savePlayerData(UUID uuid){

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

}