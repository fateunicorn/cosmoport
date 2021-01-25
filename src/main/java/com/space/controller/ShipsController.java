// Может понадобиться изменить import Date на sql-вскую версию (всюду)

package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.repository.SpaceShipsDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest")
public class ShipsController {
    @Autowired
    private ShipRepository shipRepository;



    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(/*@ModelAttribute("name") String name, @ModelAttribute("planet") String planet,
                           @ModelAttribute("shipType") ShipType shipType, @ModelAttribute("prodDate") Date prodDate,
                           @ModelAttribute("isUsed") boolean isUsed, @ModelAttribute("speed") double speed,
                           @ModelAttribute("crewSize") int crewSize*/
                           @RequestBody(required = false) Ship ship, @PathVariable("id") Long id) {
        /*// 1-ый вариант (нет):
        Ship updatedShip = null;
        if(shipRepository.findById(id).isPresent()) {
            updatedShip = shipRepository.findById(id).get();
        }
        return updatedShip;

        // 2-ой вариант (нет):
        List<Long> longsForOnlyFirstElement = new ArrayList<>();
        longsForOnlyFirstElement.add(id);
        List<Ship> shipsForOnlyFirstElement = shipRepository.findAllById(longsForOnlyFirstElement);
        return shipsForOnlyFirstElement.get(0);

        // 3-ий вариант:
        Ship shipForUpd = shipRepository.getOne(id);
        if (shipForUpd.getName() != null) shipForUpd.setName(name);
        if (shipForUpd.getPlanet() != null) shipForUpd.setPlanet(planet);
        if (shipForUpd.getShipType() != null) shipForUpd.setShipType(shipType);
        if (shipForUpd.getProdDate() != null) shipForUpd.setProdDate(prodDate);
        if (shipForUpd.isUsed() != null) shipForUpd.setUsed(isUsed);
        if (shipForUpd.getSpeed() != null) shipForUpd.setSpeed(speed);
        if (shipForUpd.getCrewSize() != null) shipForUpd.setCrewSize(crewSize);
        shipRepository.save(shipForUpd);
        return shipForUpd;

        // 4-ый вариант:
        if (id == null || id <= 0 || ship == null
                || ship.getName() != null && !(!ship.getName().isEmpty() && ship.getName().length() <= 50)
                || ship.getPlanet() != null && !(!ship.getPlanet().isEmpty() && ship.getPlanet().length() <= 50)
                || ship.getProdDate() != null && !isDateValid(ship.getProdDate())
                || ship.getSpeed() != null && !isSpeedValid(ship.getSpeed())
                || ship.getCrewSize() != null && !isCrewSizeValid(ship.getCrewSize())) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
*/
        // 5-ый вариант:
        if (id == null || id <= 0 ||
                ship.getName() != null && ship.getName().length() > 50 ||
                ship.getPlanet()  != null && ship.getPlanet().length() > 50 ||
                ship.getName() != null && ship.getName().isEmpty()||
                ship.getPlanet()  != null && ship.getPlanet().isEmpty()||
                ship.getSpeed() != null && ship.getSpeed() < 0.01 ||
                ship.getSpeed() != null && ship.getSpeed() > 0.99 ||
                ship.getCrewSize() != null && ship.getCrewSize() < 1 ||
                ship.getCrewSize() != null && ship.getCrewSize() > 9999 ||
                ship.getProdDate() != null && ship.getProdDate().getTime() < 0L ||
                ship.getProdDate() != null && ship.getProdDate().before(ship.getMinDateForAll()) ||
                ship.getProdDate() != null && ship.getProdDate().after(ship.getMaxDateForAll())
        ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (shipRepository.findById(id).isPresent()) {
            Ship shipForUpd = shipRepository.findById(id).get();
            if (shipForUpd.getName() != null && ship.getName() != null)
                shipForUpd.setName(ship.getName());
            if (shipForUpd.getPlanet() != null && ship.getPlanet() != null)
                shipForUpd.setPlanet(ship.getPlanet());
            if (shipForUpd.getShipType() != null && ship.getShipType() != null)
                shipForUpd.setShipType(ship.getShipType());
            if (shipForUpd.getProdDate() != null && ship.getProdDate() != null)
                shipForUpd.setProdDate(ship.getProdDate());
            if (shipForUpd.isUsed() != null && ship.isUsed() != null)
                shipForUpd.setUsed(ship.isUsed());
            if (shipForUpd.getSpeed() != null && ship.getSpeed() != null)
                shipForUpd.setSpeed(ship.getSpeed());
            if (shipForUpd.getCrewSize() != null && ship.getCrewSize() != null)
                shipForUpd.setCrewSize(ship.getCrewSize());
            /*if (shipForUpd.getRating() != null && ship.getRating() != null)*/
                shipForUpd.calculateRating();
            shipRepository.saveAndFlush(shipForUpd);

            return new ResponseEntity<>(shipForUpd, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    // Вытягивает не из базы (требуется (возможно) иначе)
    @GetMapping("/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> shipOfId(@PathVariable("id") Long id){
        // 1-ый вариант:
        /*for (Ship ship : sortedFractionlyList) {
            if (ship.getId() == id) {
                return ship;
            }
        }
        return null;*/

        // 2-ой вариант:
        if (id == null || id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(shipRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(shipRepository.findById(id).get(), HttpStatus.OK);
        } else  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*@GetMapping("/main")
    public String main(Map<String, Object> model) {
        // Далее есть копия следующих 2-ух строк
        Iterable<Ship> ships = shipRepository.findAll();
        model.put("ships", ships);

        return "main";
    }*/

    /*@GetMapping("/ships")
    public String add(@RequestParam String name, @RequestParam String planet, @RequestParam ShipType shipType,
                      @RequestParam Date prodDate, @RequestParam boolean isUsed, @RequestParam double speed,
                      @RequestParam int crewSize, @RequestParam double rating, Map<String, Object> model) {
        Ship ship = new Ship();

        // Можно заменить конструктором (создавая его в
        // Ship не забыть о конструкторе без параметров)
        ship.setName(name);
        ship.setPlanet(planet);
        ship.setShipType(shipType);
        ship.setProdDate(prodDate);
        ship.setUsed(isUsed);
        ship.setSpeed(speed);
        ship.setCrewSize(crewSize);
//        ship.setRating(rating);

        // Хард-код из main
        Iterable<Ship> ships = shipRepository.findAll();
        model.put("ships", ships);

        return "main";
    }*/

    private List<Ship> sortedUnfractionlyList = null;
    private List<Ship> sortedFractionlyList = null;

    @PostMapping("/ships")
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody(required = false) Ship ship) {
        if (ship.isUsed() == null) ship.setUsed(false);
        if (ship.getName() != null && ship.getName().length() > 50 ||
                ship.getPlanet()  != null && ship.getPlanet().length() > 50 ||
                ship.getName() != null && ship.getName().isEmpty()||
                ship.getPlanet()  != null && ship.getPlanet().isEmpty()||
                ship.getSpeed() != null && ship.getSpeed() < 0.01 ||
                ship.getSpeed() != null && ship.getSpeed() > 0.99 ||
                ship.getCrewSize() != null && ship.getCrewSize() < 1 ||
                ship.getCrewSize() != null && ship.getCrewSize() > 9999 ||
                ship.getProdDate() != null && ship.getProdDate().getTime() < 0L ||
                ship.getProdDate() != null && ship.getProdDate().before(ship.getMinDateForAll()) ||
                ship.getProdDate() != null && ship.getProdDate().after(ship.getMaxDateForAll()) ||
                ship.getSpeed() == null
        ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ship.calculateRating();
        shipRepository.saveAndFlush(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }


    // Получить все элементы таблицы по Accept (+без (дефолтно))
    @GetMapping("/ships")
    @ResponseBody
    public List<Ship> returnAllElementsFromTable(@RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "planet", required = false) String planet,
                                                 @RequestParam(value = "shipType",required = false) ShipType shipType,
                                                 @RequestParam(value = "after",required = false) Long after,
                                                 @RequestParam(value = "before",required = false) Long before,
                                                 @RequestParam(value = "isUsed",required = false) Boolean isUsed,
                                                 @RequestParam(value = "minSpeed",required = false) Double minSpeed,
                                                 @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                 @RequestParam(value = "minCrewSize",required = false) Integer minCrewSize,
                                                 @RequestParam(value = "maxCrewSize",required = false) Integer maxCrewSize,
                                                 @RequestParam(value = "minRating",required = false) Double minRating,
                                                 @RequestParam(value = "maxRating",required = false) Double maxRating,
                                                 @RequestParam(value = "order",required = false) ShipOrder order,
                                                 @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                                                 @RequestParam(value = "pageSize",required = false) Integer pageSize
                                                 ){
        if (after == null) after = Long.MIN_VALUE;
        if (before == null) before = Long.MAX_VALUE;

        Long finalAfter = after;
        Long finalBefore = before;

        if (minSpeed == null) minSpeed = Double.MIN_VALUE;
        if (maxSpeed == null) maxSpeed = Double.MAX_VALUE;

        Double finalMinSpeed = minSpeed;
        Double finalMaxSpeed = maxSpeed;

        if (minCrewSize == null) minCrewSize = Integer.MIN_VALUE;
        if (maxCrewSize == null) maxCrewSize = Integer.MAX_VALUE;

        Integer finalMinCrewSize = minCrewSize;
        Integer finalMaxCrewSize = maxCrewSize;

        if (minRating == null) minRating = Double.MIN_VALUE;
        if (maxRating == null) maxRating = Double.MAX_VALUE;

        Double finalMinRating = minRating;
        Double finalMaxRating = maxRating;


        List<Ship> neededShips = shipRepository.findAll().stream()
                .filter(o -> {
                    if (name != null) {
                        return o.getName().toLowerCase().contains(name.toLowerCase());
                    } else return true;
                })
                .filter(o -> {
                    if (planet != null) {
                        return o.getPlanet().toLowerCase().contains(planet.toLowerCase());
                    } else return true;
                })
                .filter(o -> {
                    if (shipType != null) {
                        return o.getShipType().equals(shipType);
                    } else return true;
                })
                .filter(o -> o.getProdDate().getTime() >= finalAfter && o.getProdDate().getTime() <= finalBefore)
                .filter(o -> {
                    if (isUsed != null) return o.isUsed() == isUsed;
                    else return true;
                })
                .filter(o -> o.getSpeed() >= finalMinSpeed && o.getSpeed() <= finalMaxSpeed)
                .filter(o -> o.getCrewSize() >= finalMinCrewSize && o.getCrewSize() <= finalMaxCrewSize)
                .filter(o -> o.getRating() >= finalMinRating && o.getRating() <= finalMaxRating)
                .collect(Collectors.toList());

        sortedUnfractionlyList = neededShips;

        if (order != null && order != ShipOrder.ID) {
            Comparator<Ship> ShipComparator;

            if (order == ShipOrder.DATE) {
                ShipComparator
                        = new Comparator<Ship>() {

                    public int compare(Ship ship1, Ship ship2) {
                        Long shipDate1 = ship1.getProdDate().getTime();
                        Long shipDate2 = ship2.getProdDate().getTime();

                        return shipDate1.compareTo(shipDate2);
                    }

                };
                neededShips.sort(ShipComparator);
            }
            if (order == ShipOrder.SPEED) {
                ShipComparator
                        = new Comparator<Ship>() {

                    public int compare(Ship ship1, Ship ship2) {
                        Double shipSpeed1 = ship1.getSpeed();
                        Double shipSpeed2 = ship2.getSpeed();

                        return shipSpeed1.compareTo(shipSpeed2);
                    }

                };
                neededShips.sort(ShipComparator);
            }
            if (order == ShipOrder.RATING) {
                ShipComparator
                        = new Comparator<Ship>() {

                    public int compare(Ship ship1, Ship ship2) {
                        Double shipRating1 = ship1.getRating();
                        Double shipRating2 = ship2.getRating();

                        return shipRating1.compareTo(shipRating2);
                    }

                };
                neededShips.sort(ShipComparator);
            }
        }

        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;

        List<List<Ship>> fractionlyListForPageConfines = new ArrayList<>();
        fractionlyListForPageConfines.add(new ArrayList<>());

        int currentPageNum = 0;
        for (int i = 0; i < neededShips.size(); i++) {
            fractionlyListForPageConfines.get(currentPageNum).add(neededShips.get(i));
            if ((i + 1) % pageSize == 0) {
                fractionlyListForPageConfines.add(new ArrayList<>());
                currentPageNum++;
            }
        }

        sortedFractionlyList = fractionlyListForPageConfines.get(pageNumber);

        return sortedFractionlyList;
    }
    /*@GetMapping("/rest/ships1")
    @ResponseBody
    public List<Ship> tableHardCodeTest(){

        Ship ship = new Ship();
        ship.setName("name1");
        ship.setPlanet("planet1");
        ship.setShipType(ShipType.TRANSPORT);
        ship.setProdDate(new Date());
        ship.setUsed(true);
        ship.setSpeed(1.1);
        ship.setCrewSize(1111);
        ship.setRating(11.11);

        Ship ship1 = new Ship("Funker", "Disco", ShipType.MERCHANT,
                              new Date(999999999  ), true, 19.72,
                      1_000_000,197.2);

        List<Ship> ships = new ArrayList<>();

        ships.add(ship);
        ships.add(ship1);

        System.out.println("\n\n" + "ships:");
        System.out.println(ships + "\n\n");

        return ships;
    }
*/

    @GetMapping("/ships/count")
    @ResponseBody
    public Long getSortedUnfractionlyListSize(@RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "planet", required = false) String planet,
                                              @RequestParam(value = "shipType",required = false) ShipType shipType,
                                              @RequestParam(value = "after",required = false) Long after,
                                              @RequestParam(value = "before",required = false) Long before,
                                              @RequestParam(value = "isUsed",required = false) Boolean isUsed,
                                              @RequestParam(value = "minSpeed",required = false) Double minSpeed,
                                              @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                              @RequestParam(value = "minCrewSize",required = false) Integer minCrewSize,
                                              @RequestParam(value = "maxCrewSize",required = false) Integer maxCrewSize,
                                              @RequestParam(value = "minRating",required = false) Double minRating,
                                              @RequestParam(value = "maxRating",required = false) Double maxRating,
                                              @RequestParam(value = "order",required = false) ShipOrder order,
                                              @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                                              @RequestParam(value = "pageSize",required = false) Integer pageSize
    ) {
        if (after == null) after = Long.MIN_VALUE;
        if (before == null) before = Long.MAX_VALUE;

        Long finalAfter = after;
        Long finalBefore = before;

        if (minSpeed == null) minSpeed = Double.MIN_VALUE;
        if (maxSpeed == null) maxSpeed = Double.MAX_VALUE;

        Double finalMinSpeed = minSpeed;
        Double finalMaxSpeed = maxSpeed;

        if (minCrewSize == null) minCrewSize = Integer.MIN_VALUE;
        if (maxCrewSize == null) maxCrewSize = Integer.MAX_VALUE;

        Integer finalMinCrewSize = minCrewSize;
        Integer finalMaxCrewSize = maxCrewSize;

        if (minRating == null) minRating = Double.MIN_VALUE;
        if (maxRating == null) maxRating = Double.MAX_VALUE;

        Double finalMinRating = minRating;
        Double finalMaxRating = maxRating;


        List<Ship> neededShips = shipRepository.findAll().stream()
                .filter(o -> {
                    if (name != null) {
                        return o.getName().toLowerCase().contains(name.toLowerCase());
                    } else return true;
                })
                .filter(o -> {
                    if (planet != null) {
                        return o.getPlanet().toLowerCase().contains(planet.toLowerCase());
                    } else return true;
                })
                .filter(o -> {
                    if (shipType != null) {
                        return o.getShipType().equals(shipType);
                    } else return true;
                })
                .filter(o -> o.getProdDate().getTime() >= finalAfter && o.getProdDate().getTime() <= finalBefore)
                .filter(o -> {
                    if (isUsed != null) return o.isUsed() == isUsed;
                    else return true;
                })
                .filter(o -> o.getSpeed() >= finalMinSpeed && o.getSpeed() <= finalMaxSpeed)
                .filter(o -> o.getCrewSize() >= finalMinCrewSize && o.getCrewSize() <= finalMaxCrewSize)
                .filter(o -> o.getRating() >= finalMinRating && o.getRating() <= finalMaxRating)
                .collect(Collectors.toList());
        return Long.valueOf(neededShips.size());
    }
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Ship> deleteShipById(@PathVariable Long id){
        if (id == null || id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(shipRepository.findById(id).isPresent()) {
            shipRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
/*
OK 200
BAD_REQUEST 400
NOT_FOUND 404
*/


/*
Ship oldShip = repository.findById(id).orElse(null);
repository.save(oldShip);
*/


/*
new ResponseEntity<>(HttpStatus.NOT_FOUND)
: new ResponseEntity<>(updateShip, HttpStatus.OK);
*/


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