package com.space.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ship")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "planet")
    private String planet;
    @Enumerated(EnumType.STRING)
    @Column(name = "shipType")
    private ShipType shipType;
    @Column(name = "prodDate")
    private Date prodDate;
    @Column(name = "isUsed")
    private Boolean isUsed;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "crewSize")
    private Integer crewSize;
    @Column(name = "rating")
    private Double rating;

    public Ship() {
    }

    public Ship(String name, String planet, ShipType shipType, Date date, Boolean isUsed, Double speed, Integer crewSize) {
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

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return Objects.equals(getId(), ship.getId()) &&
                Objects.equals(getName(), ship.getName()) &&
                Objects.equals(getPlanet(), ship.getPlanet()) &&
                getShipType() == ship.getShipType() &&
                Objects.equals(getProdDate().getTime(), ship.getProdDate().getTime()) &&
                Objects.equals(isUsed, ship.isUsed) &&
                Objects.equals(getSpeed(), ship.getSpeed()) &&
                Objects.equals(getCrewSize(), ship.getCrewSize()) &&
                Objects.equals(getRating(), ship.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPlanet(), getShipType(), getProdDate(), isUsed, getSpeed(), getCrewSize(), getRating());
    }
}
