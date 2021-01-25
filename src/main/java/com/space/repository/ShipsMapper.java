package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipsMapper implements RowMapper<Ship> {

    @Override
    public Ship mapRow(ResultSet resultSet, int i) throws SQLException {
        Ship ship = new Ship();

        //ship.setId(resultSet.getLong("id")); (не требуется т.к. значение генерируемое (аннотировано в BaseClassGeneratedId))
        ship.setName(resultSet.getString("name"));
        ship.setPlanet(resultSet.getString("planet"));
        //место для ошибки приведения типов:
        ship.setShipType((ShipType) resultSet.getObject("shipType"));
        ship.setProdDate(resultSet.getDate("prodDate"));
        ship.setUsed(resultSet.getBoolean("isUsed"));
        ship.setSpeed(resultSet.getDouble("speed"));
        ship.setCrewSize(resultSet.getInt("crewSize"));
//        ship.setRating(resultSet.getDouble("rating"));


        return ship;
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
