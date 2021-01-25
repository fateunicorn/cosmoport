package com.space.repository;

import com.space.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpaceShipsDBRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpaceShipsDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //возможна связь с методом shipOfId из ShipsController
    public List<Ship> index() {
        return jdbcTemplate.query("SELECT * FROM ship", new ShipsMapper());
    }

    public Ship show(long id) {
        return jdbcTemplate.query("SELECT * FROM ship WHERE id=?", new Object[]{id}, new ShipsMapper())
                .stream().findAny().orElse(null /*случай отсутсвия ship по id*/);
    }

    //save

    public void update(long id, Ship updatedShip){
        jdbcTemplate.update(
                "UPDATE ship SET name=?, planet=?, shipType=?, prodDate=?, " +
                        "isUsed=?, speed=?, crewSize=?, rating=? WHERE id=?",
                updatedShip.getName(), updatedShip.getPlanet(), updatedShip.getShipType(),
                updatedShip.getProdDate(), updatedShip.isUsed(), updatedShip.getSpeed(),
                updatedShip.getCrewSize(), updatedShip.getRating(), id);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM ship WHERE id=?", id);
    }
}

/*
    private long id;
    private String name;
    private String planet;
    private ShipType shipType;
    private Date prodDate;
    private boolean isUsed;
    private double speed;
    private int crewSize;
    private double rating;
*/