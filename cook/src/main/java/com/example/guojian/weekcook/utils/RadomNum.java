package com.example.guojian.weekcook.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by guojian on 11/23/16.
 */
public class RadomNum {

    public static int[] makeCount() {
        int numList [] = new int[3];
        Random random = new Random();
        int i = random.nextInt(1000);
        int j = random.nextInt(1000);
        int k = random.nextInt(1000);
        while (i == j) {
            j = random.nextInt(1000);
        }
        while (k == j || k == i) {
            k = random.nextInt(1000);
        }
        numList[0] = i+1;
        numList[1] = j+1;
        numList[2] = k+1;
        System.out.println(i + "," + j + "," + k);
        return numList;
    }


    //System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
}
