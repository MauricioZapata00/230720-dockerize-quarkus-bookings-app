package quarkus.with.docker.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("dateTime")
    private Long dateTime;

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getDateTime() {
        return dateTime;
    }
}
