package me.juval.password.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandManager extends ListenerAdapter {
    public int randomInt(int range) {
        Random rand = new Random();
        int randomNb = rand.nextInt(range);
        return randomNb;
    }

    public String shuffledString(String str) {
        List<String> caracts = Arrays.asList(str.split(""));
        Collections.shuffle(caracts);
        String ans = "";
        for (String caract : caracts)
            ans = ans + caract;
        return ans;
    }

    public String selectChar(char[] liste, int range){
        String ans = "";
        for (int i = 0; i < range; i++) {
            int randomIndex = randomInt(liste.length);
            ans = ans + liste[randomIndex];
        }
        return ans;
    }

    public String genPassword() {
        char[] min = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] number = "0123456789".toCharArray();
        char[] special = "&!.@?".toCharArray();
        String prePassword = "";
        prePassword = selectChar(min,8) + selectChar(caps,4) + selectChar(number,3) + selectChar(special,1);

        String password = shuffledString(prePassword);
        return password;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equalsIgnoreCase("gen")) {
            OptionMapping plateformeOption = event.getOption("plateforme");
            String plateforme = plateformeOption.getAsString();
            String password = genPassword();
            try {
                event.reply("**" + plateforme.toUpperCase() + "**").queue();
                Thread.sleep(1000L);
                event.getChannel().sendMessage("||" + password + "||").queue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (command.equalsIgnoreCase("password")) {
            OptionMapping plateformeOption = event.getOption("plateforme");
            String plateforme = plateformeOption.getAsString();
            OptionMapping pwdOption = event.getOption("password");
            String password = pwdOption.getAsString();
            try {
                event.reply("**" + plateforme.toUpperCase() + "**").queue();
                Thread.sleep(1000L);
                event.getChannel().sendMessage("||" + password + "||").queue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        OptionData plateforme = new OptionData(OptionType.STRING, "plateforme", "Le nom de la plateforme associau MDP", true);
        OptionData password = new OptionData(OptionType.STRING, "password", "Entrez le mot de passe associé");
                commandData.add(Commands.slash("gen", "Genun mot de passe").addOptions(new OptionData[] { plateforme }));
        commandData.add(Commands.slash("password", "Ajoute un mot de passe deja existant").addOptions(new OptionData[] { plateforme, password }));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}