package me.juval.password;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import me.juval.password.commands.GenCommand;
import me.juval.password.commands.PasswordCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.ArrayList;
import java.util.List;

public class Main extends ListenerAdapter {
    public final Dotenv config = Dotenv.load();

    public final ShardManager shardManager;

    public Main() throws LoginException {
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[] { GatewayIntent.GUILD_MEMBERS });
        this.shardManager = builder.build();
        this.shardManager.addEventListener(new Object[]
                {
                new PasswordCommand(),
                new GenCommand()
        });
    }
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        commandData.add(new GenCommand().genCommand());
        commandData.add(new PasswordCommand().passCommand());
        event.getGuild().updateCommands().addCommands(commandData).queue();
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