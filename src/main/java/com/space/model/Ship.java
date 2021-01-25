package com.space.model;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "ship")
public class Ship{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String planet;

    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    private Date prodDate;

    private Boolean isUsed;

    private Double speed;

    private Integer crewSize;

    private Double rating;

    public Ship(){}

    public void calculateRating() {
        int y0 = 3019;
        Calendar calendarProdDate = new GregorianCalendar();
        calendarProdDate.setTime(prodDate);
        int y1 = calendarProdDate.get(Calendar.YEAR);
        double k = 1.0;
        if (isUsed) k = 0.5;
        double notRoundRating = ((80.00 * speed) * k)/((y0 - y1) + 1);
        this.rating = Math.round(notRoundRating * 100) / 100.00;
    }

    public Date getMaxDateForAll() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 3019);
        return calendar.getTime();
    }

    public Date getMinDateForAll() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2800);
        return calendar.getTime();
    }

    //Setters & Getters:

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

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.round(speed * 100) / 100.00;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(int crewSize) {
        this.crewSize = crewSize;
    }

    // Ошибка 0.4 0.43 из БД
    public Double getRating() {/*
        // Постоянная в формуле для удобочитаеости
        // результата (выбрана случайно):
        final long overallCoefficient = 80;

        // Дихотомичный коэфициент новизны
        // (использованности) корабля:
        double degreeOfNovelty = Double.MIN_VALUE;
        if (isUsed) degreeOfNovelty = 0.5;
        else degreeOfNovelty = 1;

        // Хардкодное 4-ехсимвольное выражение
        // года преобразуемое в Date для
        // текущего (по лору) года:
        Date pseudoCurrentDate = null;
        long hardCodeYear = 3019L;
        try { pseudoCurrentDate = new SimpleDateFormat("yyyy").parse(hardCodeYear + "");
        } catch (ParseException e) { e.printStackTrace(); }

        // Рейтинг корабля расчитываемый по формуле:
        double formulaRating =
                (overallCoefficient * speed * degreeOfNovelty)
                        / (pseudoCurrentDate.getTime() - prodDate.getTime());

        System.out.println(name + ": " + formulaRating/3.3);

        return formulaRating;*/
        return rating;
    }
}
