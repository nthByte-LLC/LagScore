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

    public double getSafeLine(){
        return config.getDouble("Safe Line");
    }

    public double getScoreLine(){
        return config.getDouble("Score Line");
    }

    public double getHealFactor(){
        return config.getDouble("Heal Factor");
    }

    public double getHurtFactor(){
        return config.getDouble("Hurt Factor");
    }

    public double getCheckSpeed(){
        return config.getDouble("Check Speed");
    }

    public String getKickMessage(){
        return config.getString("Kick Message");
    }

    public String getConnectionDenyMessage(){
        return config.getString("Connection Deny Message");
    }

}
