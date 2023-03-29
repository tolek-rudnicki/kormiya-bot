package org.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import org.bot.Main;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutionException;



public class Commands extends ListenerAdapter {

    public HypixelAPI api = Main.API;
    public List<UUID> uuids = Main.uuids;
    public List<UUID> on = new ArrayList<UUID>();
    public List<UUID> off = new ArrayList<UUID>();
    Timer timer = new Timer();
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String message = event.getMessage().getContentDisplay();
        MessageChannelUnion channel = event.getChannel();

        if (message.equalsIgnoreCase(".run")) {
            startLoginTimer(channel);
        } else if (message.equalsIgnoreCase("resetList")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Reset players");
            //embed.addField("Removed player from the \"said\" list", " ", false);
            List<String> names = new ArrayList<String>();
            for (UUID e : on) {
                try {
                    PlayerReply.Player p = api.getPlayerByUuid(e).get().getPlayer();
                    names.add(p.getName());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                }

            }
            for (String e : names) {
                embed.addField(e, e, false);
            }
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } else if (message.contains(".forceCheck")) {
            EmbedBuilder embed = new EmbedBuilder();
            String[] args = message.split(" ");
            for (int i = 1; i < args.length; i++) {
                if (checkLogin(uuids).get(i) == null) return;
                for (String s : checkLogin(uuids).keySet()) {
                    if (checkLogin(uuids).get(s)) {
                        embed.setDescription("Player " + s + " logged on");
                        event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    } else {
                        embed.setDescription("Player " + s + " logged off");
                        event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    }
                }
            }

        }

    }

    public HashMap<String, Boolean> checkLogin(List<UUID> uuis) {
        HashMap<String, Boolean> users = new HashMap<>();
        for (UUID e : uuis) {
            //if (on.contains(e) || off.contains(e)) continue;
            System.out.println("for uuids");



            try {

                PlayerReply.Player p = api.getPlayerByUuid(e).get().getPlayer();


                if (p.isOnline() && !on.contains(e)) {
                    users.put(p.getName(), true);
                    on.add(e);
                    off.remove(e);

                } else if (!p.isOnline() && !off.contains(e)){
                    users.put(p.getName(), false);
                    off.add(e);
                    on.remove(e);
                }

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }

        }
        return users;
    }

    public void startLoginTimer(MessageChannelUnion channel) {
        EmbedBuilder embed = new EmbedBuilder();
        //timer.cancel();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                for (String s : checkLogin(uuids).keySet()) {
                    if (checkLogin(uuids).get(s)) {
                        embed.setDescription("Player " + s + " logged on");
                        channel.sendMessageEmbeds(embed.build()).queue();
                    } else {
                        embed.setDescription("Player " + s + " logged off");
                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }

            }
        }, 0, 10*1000);


        //return embed.build();

    }
}

