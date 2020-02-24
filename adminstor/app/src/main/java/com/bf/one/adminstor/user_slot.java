package com.bf.one.adminstor;

public class user_slot {
    private String mName;
    private String mCallnumber;
    private boolean mSelected = false;

    user_slot() {
    }

    user_slot(String name, String callnumber, boolean selected) {
        mName = name;
        mCallnumber = callnumber;
        mSelected = selected;
    }

    String getName() {
        return mName;
    }

    String getCallnumber() {
        if(mCallnumber != null && mCallnumber.indexOf("1") == 0)
        {
            mCallnumber = "0"+mCallnumber;
        }
        return mCallnumber;
    }

    boolean getSelected() {
        return mSelected;
    }

    void setName(String name) {
         mName = name;
    }

    void setCallnumber(String callnumber) {
         mCallnumber = callnumber;
    }

    void setSelected(boolean selected)
    {
        mSelected = selected;
    }



}
