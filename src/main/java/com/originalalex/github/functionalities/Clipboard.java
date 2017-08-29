package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public class Clipboard implements Function {

    @Override
    public void handle(MessageReceivedEvent e) {
        try {
            String data = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor); // Copy and pasted code from SO
            e.getChannel().sendMessage(data).queue(); // Send what is on your clipboard
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
