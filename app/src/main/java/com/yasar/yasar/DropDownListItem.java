package com.yasar.yasar;

/**
 * Created by r_mcconkey on 3/25/15.
 */
public class DropDownListItem {

    private String name;
    private String number;

    public DropDownListItem(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
