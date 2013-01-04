package org.inigma.shared.tracking;

public class TrackingResponse {
    private String number;
    private TrackingType type;
    private String url; // for convinience

    public String getNumber() {
        return number;
    }

    public TrackingType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(TrackingType type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
