package net.dohaw.play.lagscore.files;

import me.c10coding.coreapi.files.Config;
import net.dohaw.play.lagscore.LagScore;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;

public class PlayerDataConfig extends Config {

    public PlayerDataConfig(JavaPlugin plugin) {
        super(plugin, "playerData.yml");
    }

    public PlayerData load(UUID uuid){
        PlayerData playerData = new PlayerData(uuid);
        double playerScore = config.getDouble("Data." + uuid.toString() + ".Score");
        playerData.setScore(playerScore);
        return playerData;
    }

    public PlayerData create(UUID uuid){
        PlayerData playerData = new PlayerData(uuid);
        BaseConfig baseConfig = ((LagScore)plugin).storage.baseConfig;
        int startingScore = baseConfig.getStartingLagScore();
        playerData.setScore(startingScore);
        return playerData;
    }

    public void save(PlayerData data){
        double score = data.getScore();
        UUID uuid = data.getUUID();
        String name = data.getName();
        config.set("Data." + uuid.toString() + ".Score", score);
        config.set("Data." + uuid.toString() + ".Name", name);
        saveConfig();
    }

    public boolean hasRecordedData(UUID uuid){
        ConfigurationSection cs = config.getConfigurationSection("Data");
        if(cs != null){
            return cs.contains(uuid.toString());
        }else{
            return false;
        }
    }

    public double getScore(String playerName){

        ConfigurationSection cs = config.getConfigurationSection("Data");
        if(cs != null){
            for(String uuid : cs.getKeys(false)){
                String name = config.getString("Data." + uuid + ".Name");
                if(playerName.equalsIgnoreCase(name)){
                    return config.getDouble("Data." + uuid + ".Score");
                }
            }
        }
        return -1;
    }

}
