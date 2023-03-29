package org.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;
import org.bot.commands.Commands;


import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {

    public static final HypixelAPI API;
    public static List<UUID> uuids = new ArrayList<UUID>();

    static {
        String key = System.getProperty("apiKey", "86b72c7d-9eb9-48c6-80da-b6936b078506");
        API = new HypixelAPI(new ApacheHttpClient(UUID.fromString(key)));
    }
    public static void main(String[] args) throws LoginException {
        final String TOKEN = "MTA4OTg4NDc4NTYyODk1ODczMA.GUgJYL.bSFQ5kqR25ktGJwIj3uybEB-xwA0d1ZAHLUFJ4";
        JDABuilder builder = JDABuilder.createDefault(TOKEN);

        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT).addEventListeners(new Commands()).build();


        // Add all players
        addUUID(UUID.fromString("d1c83371-3b2e-42a8-9c36-f715864bbab5")); // bear
        addUUID(UUID.fromString("8cf7f913-3b71-471c-886a-cdf01b3d7275")); // kormiya


    }

    public static void addUUID(UUID id) {
        uuids.add(id);
    }

}