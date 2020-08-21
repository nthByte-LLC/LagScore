package net.dohaw.play.lagscore;

import me.c10coding.coreapi.BaseStorage;
import me.c10coding.coreapi.BetterJavaPlugin;
import net.dohaw.play.lagscore.files.BaseConfig;
import net.dohaw.play.lagscore.files.PlayerDataConfig;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;

public class Storage extends BaseStorage {

    public BaseConfig baseConfig;
    public PlayerDataConfig playerDataConfig;
    public PlayerDataHolder playerDataHolder;

    public Storage(BetterJavaPlugin plugin) {
        super(plugin);
        this.baseConfig = new BaseConfig(plugin);
        this.playerDataConfig = new PlayerDataConfig(plugin);
        this.playerDataHolder = new PlayerDataHolder(this);
    }

}
