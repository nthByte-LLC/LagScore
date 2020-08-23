package net.dohaw.play.lagscore;

import me.c10coding.coreapi.BetterJavaPlugin;
import net.dohaw.play.lagscore.events.PlayerWatcher;
import net.dohaw.play.lagscore.runnables.DataSaver;
import net.dohaw.play.lagscore.runnables.ScoreAdjuster;
import net.dohaw.play.lagscore.runnables.TPSChecker;
import org.bukkit.Bukkit;

public final class LagScore extends BetterJavaPlugin {

    public Storage storage;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles("config.yml", "playerData.yml");

        this.storage = new Storage(this);

        startRunnables();
        registerEvents(new PlayerWatcher(storage));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void startRunnables(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSChecker(), 100L, 1L);
        new ScoreAdjuster(storage).runTaskTimer(this, 0L, 20L);
        //Data saves every 3 seconds
        new DataSaver(storage).runTaskTimer(this, 0L, 60L);
    }

}
