package net.dohaw.play.lagscore;

import me.c10coding.coreapi.BetterJavaPlugin;

public final class LagScore extends BetterJavaPlugin {

    public Storage storage;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles("config.yml", "playerData.yml");

        this.storage = new Storage(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
