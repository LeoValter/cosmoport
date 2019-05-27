package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShipService {

    @Autowired
    ShipRepository shipRepository;

    public ShipService() {
    }

    public List<Ship> getFilteredShipList(String name, String planet, ShipType shipType,
            Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        List<Ship> ships = new ArrayList<>();
        Iterator<Ship> iterator = shipRepository.findAll().iterator();

        while (iterator.hasNext()) {
            Ship ship = iterator.next();

            if (name != null && !name.isEmpty())
                if (!ship.getName().toUpperCase().contains(name.toUpperCase())) continue;

            if (planet != null && !planet.isEmpty())
                if (!ship.getPlanet().toUpperCase().contains(planet.toUpperCase())) continue;

            if (shipType != null)
                if (shipType != ship.getShipType()) continue;

            if (after != null)
                if (after > ship.getProdDate().getTime()) continue;

            if (before != null)
                if (before < ship.getProdDate().getTime()) continue;

            if (isUsed != null)
                if (isUsed != ship.isUsed()) continue;

            if (minSpeed != null)
                if (minSpeed > ship.getSpeed()) continue;

            if (maxSpeed != null)
                if (maxSpeed < ship.getSpeed()) continue;

            if (minCrewSize != null)
                if (minCrewSize > ship.getCrewSize()) continue;

            if (maxCrewSize != null)
                if (maxCrewSize < ship.getCrewSize()) continue;

            if (minRating != null)
                if (minRating > ship.getRating()) continue;

            if (maxRating != null)
                if (maxRating < ship.getRating()) continue;

            ships.add(ship);
        }
        return ships;
    }

    public List<Ship> getSortedShipList(
            List<Ship> list, Integer pageNumber, Integer pageSize, ShipOrder order) {

        if (pageSize == null && pageNumber == null && order == null)
            return list.stream().limit(3).collect(Collectors.toList());

        if (pageSize != null && pageNumber == null && order == null)
            return list.stream().limit(pageSize).collect(Collectors.toList());

        if (pageSize == null && pageNumber != null && order == null)
            return list.stream().skip(pageNumber * 3).limit(3).collect(Collectors.toList());

        if (pageSize == null && pageNumber == null)
            return list.stream().sorted(getComparatorByOrder(order)).limit(3).collect(Collectors.toList());

        if (pageSize == null)
            return list.stream().sorted(getComparatorByOrder(order)).limit(3).collect(Collectors.toList());

        if (pageNumber == null)
            return list.stream().sorted(getComparatorByOrder(order)).limit(pageSize).skip(pageSize).collect(Collectors.toList());

        return list.stream().sorted(getComparatorByOrder(order)).skip(pageNumber * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public Comparator<Ship> getComparatorByOrder(ShipOrder order) {
        Comparator<Ship> comparator = null;

        switch (order.getFieldName()) {
            case "id": {
                comparator = Comparator.comparing(Ship::getId);
                break;
            }
            case "speed": {
                comparator = Comparator.comparing(Ship::getSpeed);
                break;
            }
            case "date": {
                comparator = Comparator.comparing(Ship::getProdDate);
                break;
            }
            case "rating": {
                comparator = Comparator.comparing(Ship::getRating);
            }
        }
        return comparator;
    }

    public Boolean isValidShip(Ship ship) {

        return (checkBodyParamsNotNull(ship)
                || checkNameLength(ship)
                || checkPlanetLength(ship)
                || checkCrewSize(ship)
                || checkSpeed(ship)
                || checkProdDate(ship));
    }

    public boolean checkProdDate(Ship ship) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());

            return (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019);
    }

    public boolean checkSpeed(Ship ship) {
        return ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99;
    }

    public boolean checkCrewSize(Ship ship) {
        return ship.getCrewSize() < 1 || ship.getCrewSize() > 9999;
    }

    public boolean checkPlanetLength(Ship ship) {
        return ship.getPlanet().length() < 1 || ship.getPlanet().length() > 50;
    }

    public boolean checkNameLength(Ship ship) {
        return ship.getName().length() < 1 || ship.getName().length() > 50;
    }

    public Ship addShip(Ship ship) {
        if (ship.isUsed() == null) ship.setUsed(false);
        Double rating = calculateRating(ship);
        ship.setRating(rating);
        return shipRepository.saveAndFlush(ship);
    }

    public boolean checkBodyParamsNotNull(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null) return true;
        
        return false;
    }

    public Double calculateRating(Ship ship) {
        if (ship.getProdDate() != null && ship.getSpeed() != null) {
            double k;
            boolean isUsed = false;

            if (ship.isUsed() != null) {
                isUsed = ship.isUsed();
            }

            if (isUsed) {
                k = 0.5;
            } else {
                k = 1D;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ship.getProdDate().getTime());
            int prodYear = calendar.get(Calendar.YEAR);

            double ratingNoRound = (80 * ship.getSpeed() * k) / (3019 - prodYear + 1);

            return (double) Math.round(ratingNoRound * 100) / 100;
        }
        return null;
    }

}