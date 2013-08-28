package com.gmail.risterral.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

    public static void shootFireworks(Player player) {
        Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        Random r = new Random();
        int fType = r.nextInt(5) + 1;
        FireworkEffect.Type type = null;
        switch (fType) {
            case 1:
            default:
                type = FireworkEffect.Type.BALL;
            case 2:
                type = FireworkEffect.Type.BALL_LARGE;
            case 3:
                type = FireworkEffect.Type.BURST;
            case 4:
                type = FireworkEffect.Type.CREEPER;
            case 5:
                type = FireworkEffect.Type.STAR;
        }

        int c1i = r.nextInt(17) + 1;
        int c2i = r.nextInt(17) + 1;
        Color c1 = getColor(c1i);
        Color c2 = getColor(c2i);
        FireworkEffect effect = FireworkEffect.builder()
                                .flicker(r.nextBoolean())
                                .withColor(c1)
                                .withFade(c2)
                                .with(type)
                                .trail(r.nextBoolean())
                                .build();
        fm.addEffect(effect);
        int power = r.nextInt(1) + 1;
        fm.setPower(power);
        fw.setFireworkMeta(fm);
    }

    private static Color getColor(int c) {
        switch (c) {
            case 1:
            default:
                return Color.AQUA;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.FUCHSIA;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIME;
            case 8:
                return Color.MAROON;
            case 9:
                return Color.NAVY;
            case 10:
                return Color.OLIVE;
            case 11:
                return Color.ORANGE;
            case 12:
                return Color.PURPLE;
            case 13:
                return Color.RED;
            case 14:
                return Color.SILVER;
            case 15:
                return Color.TEAL;
            case 16:
                return Color.WHITE;
            case 17:
        }return Color.YELLOW;
    }

}
