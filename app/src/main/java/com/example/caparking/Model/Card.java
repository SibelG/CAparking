package com.example.caparking.Model;

public class Card {
    private String Card_number;
    private String Card_expiry_date;
    private String CardCVV;
    private String Postal_Code;
    private String Phone_Number;

    public Card(){

    }

    public Card(String card_number, String card_expiry_date, String cardCVV, String postal_code, String phone_number){

        Card_number = card_number;
        Card_expiry_date = card_expiry_date;
        CardCVV = cardCVV;
        Postal_Code = postal_code;
        Phone_Number = phone_number;
    }

    public String getCard_number() {
        return Card_number;
    }

    public void setCard_number(String card_number) {
        Card_number = card_number;
    }

    public String getCard_expiry_date() {
        return Card_expiry_date;
    }

    public void setCard_expiry_date(String card_expiry_date) {
        Card_expiry_date = card_expiry_date;
    }

    public String getCardCVV() {
        return CardCVV;
    }

    public void setCardCVV(String cardCVV) {
        CardCVV = cardCVV;
    }

    public String getPostal_Code() {
        return Postal_Code;
    }

    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }
}
