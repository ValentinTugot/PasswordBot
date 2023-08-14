package me.juval.password.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenCommand extends ListenerAdapter {

    public CommandData genCommand(){
        OptionData genPlateforme = new OptionData(OptionType.STRING, "plateforme", "Le nom de la plateforme associau MDP", true);
        OptionData longeur = new OptionData(OptionType.INTEGER, "longeur", "Longeur du mot de passe (~ 8-32)", true);

        return Commands.slash("gen", "Génère mot de passe").addOptions(genPlateforme,longeur);
    }
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

        String prePassword = selectChar(min, length / 2) + selectChar(caps, length / 4) + selectChar(number, length / 8) + selectChar(special, length / 8);

        return shuffledString(prePassword);

    }
    @Override
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

        }
    }
}
