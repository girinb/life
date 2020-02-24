package com.bf.lineplus;

public class note_item {
    int id;
    String title;
    String contents;
    String imagelist;

    note_item(int _id, String _title, String _contents, String _imagelist) {
        id = _id;
        title = _title;
        contents = _contents;
        imagelist = _imagelist;

    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagelist() {
        return imagelist;
    }

    public void setImagelist(String imagelist) {
        this.imagelist = imagelist;
    }

    public void setcontents(String contents) {
        this.contents = contents;
    }

    public int getId() {
        return id;
    }
}
