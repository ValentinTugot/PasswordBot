package me.juval.password;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import me.juval.password.commands.CommandManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {
    public final Dotenv config = Dotenv.load();

    private final ShardManager shardManager;

    public Main() throws LoginException {
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[] { GatewayIntent.GUILD_MEMBERS });
        this.shardManager = builder.build();
        this.shardManager.addEventListener(new Object[] { new CommandManager() });
    }

    public Dotenv getConfig() {
        return this.config;
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public static void main(String[] args) {
        try {
            Main main = new Main();
        } catch (LoginException e) {
            System.out.println("Erreur de connexion");
        }
    }
}