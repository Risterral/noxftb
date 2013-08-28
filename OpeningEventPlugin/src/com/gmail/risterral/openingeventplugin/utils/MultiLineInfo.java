package com.gmail.risterral.openingeventplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MultiLineInfo {
    private static final int CHARACTERS_IN_ROW = 50;
    private static final int LINES_PER_PAGE = 8;

    public static boolean sendInfo(Player player, String title, String command, String[] texts, int page) {
        if (title != null) {
            player.sendRawMessage(createTitle(title, texts.length, page));
        }
        if (command != null) {
            player.sendRawMessage(ChatColor.GRAY + ("Use /" + command + " [n] to get page of " + command));
        }
        if (texts.length < (page - 1) * LINES_PER_PAGE) {
            return false;
        } else {
            for(int i=(page - 1) * LINES_PER_PAGE; i<page * LINES_PER_PAGE; i++) {
                if (i == texts.length) {
                    return true;
                }
                player.sendRawMessage(texts[i]);
            }
        }
        return true;
    }

    private static String createTitle(String title, int numberOfTexts, int page) {
        StringBuilder titleSB = new StringBuilder();
        StringBuilder tempSB = new StringBuilder();
        tempSB.append(" ");
        tempSB.append(title);
        tempSB.append(": Index (");
        tempSB.append(page);
        tempSB.append("/");
        tempSB.append((numberOfTexts / LINES_PER_PAGE) + 1);
        tempSB.append(") ");
        String temp = tempSB.toString();

        int i;
        titleSB.append(ChatColor.YELLOW);
        for (i=0; i<(CHARACTERS_IN_ROW/2 - temp.length()/2); i++) {
            titleSB.append("-");
        }
        titleSB.append(ChatColor.WHITE);
        titleSB.append(temp);
        titleSB.append(ChatColor.YELLOW);
        for (int j=i + temp.length(); j<CHARACTERS_IN_ROW; j++) {
            titleSB.append("-");
        }
        return titleSB.toString();
    }
}
