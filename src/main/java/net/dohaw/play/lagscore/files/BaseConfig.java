package net.dohaw.play.lagscore.files;

import me.c10coding.coreapi.files.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class BaseConfig extends Config {

    public BaseConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public String getPrefix(){
        return config.getString("Prefix");
    }

    public int getStartingLagScore(){
        return config.getInt("Starting Lag Score");
    }

}
