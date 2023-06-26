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
        return rand.nextInt(range);
    }

    public String shuffledString(String str) {
        List<String> caracts = Arrays.asList(str.split(""));
        Collections.shuffle(caracts);
        StringBuilder ans = new StringBuilder(110);
        for (String caract : caracts)
            ans.append(caract);
        return ans.toString();
    }

    public String selectChar(char[] liste, int range){
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < range; i++) {
            int randomIndex = randomInt(liste.length);
            ans.append(liste[randomIndex]);
        }
        return ans.toString();
    }

    public String genPassword(int length) {
        char[] min = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] number = "0123456789".toCharArray();
        char[] special = "&!.@?".toCharArray();

        String prePassword = selectChar(min,length/2) + selectChar(caps,length/4) + selectChar(number,length/8) + selectChar(special,length/8);

        return shuffledString(prePassword);

    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equalsIgnoreCase("gen")) {
            OptionMapping plateformeOption = event.getOption("plateforme");
            OptionMapping longeur = event.getOption("longeur");
            assert plateformeOption != null;
            String plateforme = plateformeOption.getAsString();
            assert longeur != null;
            int passLength = longeur.getAsInt();

            if (passLength < 8 || passLength > 32){
                event.reply("Longeur incorrect").queue();

            }else{
                String password = genPassword(passLength);
                try {
                    event.reply("**" + plateforme.toUpperCase() + "**").queue();
                    Thread.sleep(1000L);
                    event.getChannel().sendMessage("||" + password + "||").queue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (command.equalsIgnoreCase("password")) {
            OptionMapping plateformeOption = event.getOption("plateforme");
            assert plateformeOption != null;
            String plateforme = plateformeOption.getAsString();
            OptionMapping pwdOption = event.getOption("password");
            assert pwdOption != null;
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
        // Options Gen Command
        OptionData genPlateforme = new OptionData(OptionType.STRING, "plateforme", "Le nom de la plateforme associau MDP", true);
        OptionData longeur = new OptionData(OptionType.INTEGER, "longeur", "Longeur du mot de passe (~ 8-32)", true);
        // Options Password Command
        OptionData plateforme = new OptionData(OptionType.STRING, "plateforme", "Le nom de la plateforme associau MDP", true);
        OptionData password = new OptionData(OptionType.STRING, "password", "Entrez le mot de passe associé");
        commandData.add(Commands.slash("gen", "Génère mot de passe").addOptions(genPlateforme,longeur));
        commandData.add(Commands.slash("password", "Ajoute un mot de passe deja existant").addOptions(plateforme, password));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}