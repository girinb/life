package com.bf.lineplus;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        /*
        dma..음....
        ID는 추가할때마다 증가할꺼고
        일단 타이틀
        본문
        이미지들 이름
        이렇게 만들면 되겠다.

         */
        public static final String TITLE = "TITLE";
        public static final String CONTENTS = "CONTENTS";
        public static final String IMAGES = "IMAGES";
        public static final String _TABLENAME = "NOTEDB";
        public static final String _CREATE =
                "create table " + _TABLENAME + "("
                        + _ID + " integer primary key autoincrement, "
                        + TITLE + " text not null , "
                        + CONTENTS + " text not null , "
                        + IMAGES + " text not null );";
    }
}