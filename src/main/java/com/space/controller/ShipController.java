package com.space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class ShipController {

    @GetMapping("/areYouLiveHere")
    public String areYouLiveHere(
            @RequestParam(value = "id")
            String id,

            @RequestParam(value = "speed")
            String speed,

            @RequestParam(value = "date")
            String date /*or long/double*/,

            @RequestParam(value = "rating")
            String rating,

            Model model
    ) {
        model.addAttribute("idSpeedDateRating",
                id + " and " + speed + " and " + date + " and " + rating + " ...");
        return "views/ship/areYouLiveHere";
//        return "index";
    }
}
