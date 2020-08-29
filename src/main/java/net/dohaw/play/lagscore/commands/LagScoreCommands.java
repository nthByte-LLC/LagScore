package net.dohaw.play.lagscore.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.lagscore.LagScore;
import net.dohaw.play.lagscore.Storage;
import net.dohaw.play.lagscore.playerdata.PlayerData;
import net.dohaw.play.lagscore.playerdata.PlayerDataHolder;
import net.dohaw.play.lagscore.runnables.ScoreAdjuster;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LagScoreCommands implements CommandExecutor {

    private Storage storage;
    private PlayerDataHolder playerDataHolder;
    private ChatFactory chatFactory;

    public LagScoreCommands(Storage storage){
        this.storage = storage;
        this.playerDataHolder = storage.playerDataHolder;
        this.chatFactory = storage.chatFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender.hasPermission("lagscore.use")){

            if(args[0].equalsIgnoreCase("reload") && args.length == 1) {
                storage.baseConfig.reloadConfig();
                restartScoreAdjuster();
                storage.api.getChatFactory().sendPlayerMessage("LagScore has been reloaded!", false, sender, null);
        /*
            I understand this is probably not the most efficient way to sort the list, but I was lazy
         */
            }else if(args[0].equalsIgnoreCase("list") && args.length == 1){

                List<PlayerData> playerData = playerDataHolder.getData();
                Map<String, Double> playerScores = new HashMap<>();

                chatFactory.sendPlayerMessage("Lag Scores:", false, sender, null);
                for(PlayerData pd : playerData){

                    String name = pd.getName();
                    double score = pd.getScore();

                    BigDecimal bd = new BigDecimal(score);
                    bd = bd.round(new MathContext(3));
                    double rounded = bd.doubleValue();

                    playerScores.put(name, rounded);
                }

                Map<String, Double> sortedData = playerScores.entrySet().stream().sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                for(Map.Entry<String, Double> en : sortedData.entrySet()){
                    String name = en.getKey();
                    double score = en.getValue();
                    chatFactory.sendPlayerMessage("&e" + name + ": &c" + score, false, sender, null);
                }

            }else if(args.length == 2){

                String playerName = args[0];

                if(isValidPlayer(playerName)){

                    Player targetPlayer = Bukkit.getPlayer(playerName);
                    double score;
                    try{
                        score = Double.parseDouble(args[1]);
                    }catch(IllegalArgumentException e){
                        chatFactory.sendPlayerMessage("This is not a valid number!", false, sender, null);
                        return false;
                    }

                    PlayerData targetPlayerData = playerDataHolder.getPlayerData(targetPlayer.getUniqueId());
                    targetPlayerData.setScore(score);
                    playerDataHolder.updatePlayerData(targetPlayer.getUniqueId(), targetPlayerData);
                    chatFactory.sendPlayerMessage("You have changed &e" + playerName + "'s&f LagScore to &e" + score, false, sender, null);

                }else{
                    chatFactory.sendPlayerMessage("This player is either not online or isn't a valid player!", false, sender, null);
                }

            }

        }


        return false;
    }

    private void restartScoreAdjuster(){

        LagScore plugin = (LagScore) storage.plugin;
        BukkitRunnable scoreAdjuster = plugin.getScoreAdjuster();
        scoreAdjuster.cancel();

        double checkSpeed = storage.baseConfig.getCheckSpeed();

        ScoreAdjuster newScoreAdjuster = new ScoreAdjuster(storage);
        newScoreAdjuster.runTaskTimer(plugin, 0L, (long) (checkSpeed * 20) );
        plugin.setScoreAdjuster(newScoreAdjuster);
    }

    private boolean isValidPlayer(String playerName){
        List<PlayerData> playerData = playerDataHolder.getData();
        for(PlayerData pd : playerData){
            if(pd.getName().equalsIgnoreCase(playerName)){
                return true;
            }
        }
        return false;
    }

}
