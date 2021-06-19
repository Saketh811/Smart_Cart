package com.example.smartcart2;

public class ucart {
    String title;
    int price;
    String id;
    long brcode;
    public ucart()
    {}
    public ucart(String title,int price, String id, long brcode){
        this.title = title;
        this.price = price;
        this.id = id;
        this.brcode = brcode;
    }
    public String getTitle() {
        return title;
    }

    public long getBrcode() {
        return brcode;
    }

    public int getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }
}