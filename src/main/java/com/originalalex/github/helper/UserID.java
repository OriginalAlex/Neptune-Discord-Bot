package com.originalalex.github.helper;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UserID {

    public boolean isValidUserID(MessageReceivedEvent e, String id) {
        return getMember(e, id) != null;
    }

    public Member getMember(MessageReceivedEvent e, String id) {
        return e.getGuild().getMemberById(id);
    }

}
