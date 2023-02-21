package com.clientserver;

public class Tools {
    public static boolean DEBUG = true;
    public static void dprint(String s){
        if (DEBUG){
            System.out.println(s);
        }
    }
}
