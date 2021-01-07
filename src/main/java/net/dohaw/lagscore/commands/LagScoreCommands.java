package net.dohaw.lagscore.commands;

import net.dohaw.corelib.ChatSender;
import net.dohaw.lagscore.LagScore;
import net.dohaw.lagscore.files.BaseConfig;
import net.dohaw.lagscore.files.PlayerDataConfig;
import net.dohaw.lagscore.playerdata.PlayerData;
import net.dohaw.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.lagscore.runnables.ScoreAdjuster;
import net.dohaw.lagscore.utils.ScoreUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class LagScoreCommands implements CommandExecutor {

    private LagScore plugin;
    private PlayerDataHolder playerDataHolder;
    private PlayerDataConfig playerDataConfig;
    private BaseConfig baseConfig;

    public LagScoreCommands(LagScore plugin){
        this.playerDataHolder = plugin.getPlayerDataHolder();
        this.playerDataConfig = plugin.getPlayerDataConfig();
        this.baseConfig = plugin.getBaseConfig();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender.hasPermission("lagscore.use")){

            if(args.length != 0){

                if(args[0].equalsIgnoreCase("reload") && args.length == 1) {
                    plugin.getBaseConfig().reloadConfig();
                    restartScoreAdjuster();
                    ChatSender.sendPlayerMessage("LagScore has been reloaded!", false, sender, null);
                /*
                    I understand this is probably not the most efficient way to sort the list, but I was lazy
                 */
                }else if(args[0].equalsIgnoreCase("list") && args.length == 1) {

                    Map<UUID, PlayerData> playerData = playerDataHolder.getData();
                    Map<String, Double> playerScores = new HashMap<>();

                    ChatSender.sendPlayerMessage("Lag Scores:", false, sender, null);
                    for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {

                        PlayerData pd = entry.getValue();
                        String name = pd.getName();
                        double score = pd.getScore();
                        double rounded = ScoreUtils.roundToDecimalPoints(score, 3);

                        playerScores.put(name, rounded);
                    }

                    Map<String, Double> sortedData = playerScores.entrySet().stream().sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                    for (Map.Entry<String, Double> en : sortedData.entrySet()) {
                        String name = en.getKey();
                        double score = en.getValue();
                        ChatSender.sendPlayerMessage("&e" + name + ": &c" + score, false, sender, null);
                    }
                }else if(args[0].equalsIgnoreCase("see") && args.length == 2){
                    String playerName = args[1];
                    double score = playerDataConfig.getScore(playerName);
                    score = ScoreUtils.roundToDecimalPoints(score, 3);
                    if(score != -1){
                        ChatSender.sendPlayerMessage("&e" + playerName + "'s&f LagScore is &e" + score, false, sender, null);
                    }else{
                        ChatSender.sendPlayerMessage("This player does not have any recorded LagScore!", false, sender, null);
                    }
                }else if(args.length == 2){

                    String playerName = args[0];

                    if(isValidPlayer(playerName)){

                        Player targetPlayer = Bukkit.getPlayer(playerName);
                        double score;
                        try{
                            score = Double.parseDouble(args[1]);
                        }catch(IllegalArgumentException e){
                            ChatSender.sendPlayerMessage("This is not a valid number!", false, sender, null);
                            return false;
                        }

                        PlayerData targetPlayerData = playerDataHolder.getPlayerData(targetPlayer.getUniqueId());
                        targetPlayerData.setScore(score);
                        playerDataHolder.updatePlayerData(targetPlayerData);
                        ChatSender.sendPlayerMessage("You have changed &e" + playerName + "'s&f LagScore to &e" + score, false, sender, null);

                    }else{
                        ChatSender.sendPlayerMessage("This player is either not online or isn't a valid player!", false, sender, null);
                    }

                }

            }

        }

        return false;
    }

    private void restartScoreAdjuster(){

        BukkitRunnable scoreAdjuster = plugin.getScoreAdjuster();
        scoreAdjuster.cancel();

        double checkSpeed = baseConfig.getCheckSpeed();

        ScoreAdjuster newScoreAdjuster = new ScoreAdjuster(plugin);
        newScoreAdjuster.runTaskTimer(plugin, 0L, (long) (checkSpeed * 20) );
        plugin.setScoreAdjuster(newScoreAdjuster);
    }

    private boolean isValidPlayer(String playerName){
        Map<UUID, PlayerData> playerData = playerDataHolder.getData();
        for(Map.Entry<UUID, PlayerData> entry : playerData.entrySet()){
            PlayerData pd = entry.getValue();
            if(pd.getName().equalsIgnoreCase(playerName)){
                return true;
            }
        }
        return false;
    }

}
