package com.originalalex.github;

import com.originalalex.github.functionalities.BeautifulPic;
import com.originalalex.github.listener.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;

public class Main {

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.CLIENT)
                    .setToken([my token])
                    .buildBlocking();
            jda.addEventListener(new MessageListener(jda));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
