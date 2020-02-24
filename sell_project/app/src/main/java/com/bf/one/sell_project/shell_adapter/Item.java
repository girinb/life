package com.bf.one.sell_project.shell_adapter;

/**
 * Created by charlie on 2017. 12. 7
 */

class Item {
    private String mName;
    private String mimgpath;

    Item(String name, String imgpath) {
        mName = name;
        mimgpath = imgpath;
    }

    String getName() {
        return mName;
    }

    String getImgpath() {
        return mimgpath;
    }
}
