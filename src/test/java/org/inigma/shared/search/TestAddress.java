package org.inigma.shared.search;

public class TestAddress {
    private String street;
    private String city;
    private String state;
    private String postal;

    public String getCity() {
        return city;
    }

    public String getPostal() {
        return postal;
    }

    public String getState() {
        return state;
    }

    public String getStreets() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStreets(String street) {
        this.street = street;
    }
}
