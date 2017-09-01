package com.originalalex.github;

import com.originalalex.github.listener.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("MzUyNTczODI4OTc2NzM4MzA2.DIrdxw.Wme-AQopVx0YkGfXwxIkLGHJbIM")
                    .buildBlocking();
            jda.addEventListener(new MessageListener(jda));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
