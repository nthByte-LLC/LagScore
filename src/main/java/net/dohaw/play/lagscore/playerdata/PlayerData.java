package net.dohaw.play.lagscore.playerdata;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerData {

    @Getter @Setter private double score;
    @Getter private final UUID UUID;
    @Getter private String name;

    public PlayerData(final UUID uuid){
        this.UUID = uuid;
        setName();
    }

    private void setName(){
        this.name = Bukkit.getPlayer(UUID).getName();
    }

}
