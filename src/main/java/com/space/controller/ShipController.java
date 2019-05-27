package com.space.controller;

import com.space.exceptions.BadRequestException;
import com.space.exceptions.ForbiddenException;
import com.space.exceptions.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getSortedShipsList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,
            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) ShipOrder order) {

        List<Ship> ships = shipService.getFilteredShipList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipService.getSortedShipList(ships, pageNumber, pageSize, order);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer count(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,
            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {

        List<Ship> ships = shipService.getFilteredShipList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return ships.size();
    }

    @RequestMapping(value = "/ships/", method = RequestMethod.POST)
    public Ship createShip(@RequestBody Ship ship) {
        if (shipService.isValidShip(ship)) throw new BadRequestException();
            return shipService.addShip(ship);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    public Ship getShip(@PathVariable Long id) {
        if (id == null || id == 0) throw new BadRequestException();
        if (!shipRepository.existsById(id)) throw new NotFoundException();

        return shipRepository.findById(id).get();
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship) {

        Ship updatedShip = getShip(id);

        if (ship.getName() != null) updatedShip.setName(ship.getName());
        if (ship.getPlanet() != null) updatedShip.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null) updatedShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null) updatedShip.setProdDate(ship.getProdDate());
        if (ship.getSpeed() != null) updatedShip.setSpeed(ship.getSpeed());
        if (ship.isUsed() != null) updatedShip.setUsed(ship.isUsed());
        if (ship.getCrewSize() != null) updatedShip.setCrewSize(ship.getCrewSize());
        updatedShip.setRating(shipService.calculateRating(updatedShip));

        if (shipService.isValidShip(updatedShip)) throw new BadRequestException();

        return shipRepository.saveAndFlush(updatedShip);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    public void deleteShip(@PathVariable Long id) {
        Ship ship = getShip(id);
        shipRepository.delete(ship);
    }
}