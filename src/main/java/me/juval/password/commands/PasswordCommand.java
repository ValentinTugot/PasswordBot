package me.juval.password.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PasswordCommand extends ListenerAdapter {

    public CommandData passCommand(){
        OptionData plateforme = new OptionData(OptionType.STRING, "plateforme", "Le nom de la plateforme associau MDP", true);
        OptionData password = new OptionData(OptionType.STRING, "password", "Entrez le mot de passe associé");

        return Commands.slash("password", "Ajoute un mot de passe deja existant").addOptions(plateforme, password);
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equalsIgnoreCase("password")) {
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
}
