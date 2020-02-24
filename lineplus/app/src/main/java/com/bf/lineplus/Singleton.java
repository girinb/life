package com.bf.lineplus;

import android.content.Context;

public class Singleton {
    public static final int potion_plag_createnote = 1;
    public static final int potion_plag_readnote = 2;
    public static final int potion_plag_editnote = 3;
    public static final int PICK_FROM_ALBUM = 4;
    public static final int PICK_FROM_CAMERA = 5;
    //    public ArrayList<note_item> note_items;
    public DbOpenHelper mDbOpenHelper;
    public static Context Scontext;

    private Singleton() {
//        note_items = new ArrayList<>();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static final Singleton INSTANCE = new Singleton();
    }
}