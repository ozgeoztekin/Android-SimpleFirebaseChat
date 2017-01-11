package com.oztekino.simplefirebasechat;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {

    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
    }
}
