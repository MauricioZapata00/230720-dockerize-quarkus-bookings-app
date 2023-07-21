package quarkus.with.docker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
@NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private Double latitude;
    @Column(unique = true)
    private Double longitude;
    private Long dateTime;

    public Booking() {
    }

    public Booking(String name, Double latitude, Double longitude, Long dateTime) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
