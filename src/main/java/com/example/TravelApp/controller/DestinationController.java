package com.example.TravelApp.controller;

import com.example.TravelApp.model.Destination;
import com.example.TravelApp.service.DestinationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/destinations")
    public String destinations(Model model,
                               @RequestParam(required = false) String country,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice) {
        List<Destination> destinations;

        if (country != null && !country.isBlank()) {
            destinations = destinationService.findByCountry(country);
        } else if (category != null && !category.isBlank()) {
            destinations = destinationService.findByCategory(category);
        } else if (minPrice != null && maxPrice != null) {
            destinations = destinationService.findByPriceRange(minPrice, maxPrice);
        } else {
            destinations = destinationService.findAll();
        }

        model.addAttribute("destinations", destinations);
        return "destinations";
    }
}
