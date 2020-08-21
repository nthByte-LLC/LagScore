package net.dohaw.play.lagscore.playerdata;

import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerData {

    private double score;
    private final UUID UUID;
    private String name;

    public PlayerData(final UUID uuid){
        this.UUID = uuid;
        setName();
    }

    public double getScore(){
        return score;
    }

    public void setScore(double score){
        this.score = score;
    }

    public UUID getUUID(){
        return UUID;
    }

    public String getName(){
        return name;
    }

    private void setName(){
        this.name = Bukkit.getPlayer(UUID).getName();
    }

}
