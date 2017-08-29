package com.originalalex.github;

import com.originalalex.github.listener.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Created by Alex on 28/08/2017.
 */
public class Main {

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.CLIENT)
                    .setToken("MjY0MzU1MTEzNTAwNDA5ODU2.C0fXdQ.FUsDvDIIiVt_gn2ZMWLfrEEN2BI")
                    .buildBlocking();
            jda.addEventListener(new MessageListener(jda));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
