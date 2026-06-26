package com.example.TravelApp.controller;

import com.example.TravelApp.model.*;
import com.example.TravelApp.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    public AdminController(UserService userService, DestinationService destinationService,
                           TourPackageService tourPackageService, BookingService bookingService,
                           ReviewService reviewService) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalDestinations", destinationService.findAll().size());
        model.addAttribute("totalPackages", tourPackageService.findAll().size());
        model.addAttribute("totalBookings", bookingService.findAll().size());
        model.addAttribute("totalRevenue", bookingService.findAll().stream().mapToDouble(Booking::getTotalPrice).sum());
        model.addAttribute("recentBookings", bookingService.findAll().stream().limit(5).toList());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.findAll());
        return "admin/users-list";
    }

    @GetMapping("/users/add")
    public String addUserForm(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/users-form";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String name, @RequestParam String email,
                          @RequestParam String password, @RequestParam String role,
                          @RequestParam String phone, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        User user = new User(name, email, password, role);
        user.setPhone(phone);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "admin/users-form";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @RequestParam String name,
                           @RequestParam String email, @RequestParam String role,
                           @RequestParam String phone, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var user = userService.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setName(name);
            u.setEmail(email);
            u.setRole(role);
            u.setPhone(phone);
            userService.save(u);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/destinations")
    public String listDestinations(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("destinations", destinationService.findAll());
        return "admin/destinations-list";
    }

    @GetMapping("/destinations/add")
    public String addDestinationForm(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/destinations-form";
    }

    @PostMapping("/destinations/add")
    public String addDestination(@RequestParam String name, @RequestParam String country,
                                 @RequestParam String category, @RequestParam String description,
                                 @RequestParam String imageUrl, @RequestParam Double price,
                                 @RequestParam Double rating, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Destination dest = new Destination(name, country, category, description, imageUrl, price, rating);
        destinationService.save(dest);
        return "redirect:/admin/destinations";
    }

    @GetMapping("/destinations/edit/{id}")
    public String editDestinationForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var dest = destinationService.findById(id);
        if (dest.isPresent()) {
            model.addAttribute("destination", dest.get());
            return "admin/destinations-form";
        }
        return "redirect:/admin/destinations";
    }

    @PostMapping("/destinations/edit/{id}")
    public String editDestination(@PathVariable Long id, @RequestParam String name,
                                  @RequestParam String country, @RequestParam String category,
                                  @RequestParam String description, @RequestParam String imageUrl,
                                  @RequestParam Double price, @RequestParam Double rating,
                                  HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var dest = destinationService.findById(id);
        if (dest.isPresent()) {
            Destination d = dest.get();
            d.setName(name);
            d.setCountry(country);
            d.setCategory(category);
            d.setDescription(description);
            d.setImageUrl(imageUrl);
            d.setPrice(price);
            d.setRating(rating);
            destinationService.save(d);
        }
        return "redirect:/admin/destinations";
    }

    @PostMapping("/destinations/delete/{id}")
    public String deleteDestination(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        destinationService.deleteById(id);
        return "redirect:/admin/destinations";
    }

    @GetMapping("/packages")
    public String listPackages(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("packages", tourPackageService.findAll());
        return "admin/packages-list";
    }

    @GetMapping("/packages/add")
    public String addPackageForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("destinations", destinationService.findAll());
        return "admin/packages-form";
    }

    // 🎯 අලුතින් පැකේජ් එකක් දාද්දී mapUrl එකයි includes එකයි දෙකම සේව් වෙන විදිහට හැදුවා
    @PostMapping("/packages/add")
    public String addPackage(@RequestParam String name, @RequestParam String category,
                             @RequestParam String description, @RequestParam Double price,
                             @RequestParam Double rating, @RequestParam String imageUrl,
                             @RequestParam Integer maxTravelers, @RequestParam Long destinationId,
                             @RequestParam String mapUrl, @RequestParam String includes, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var dest = destinationService.findById(destinationId);
        if (dest.isPresent()) {
            TourPackage pkg = new TourPackage(name, description, price, rating, imageUrl, category, maxTravelers, mapUrl, dest.get());
            pkg.setIncludes(includes); // 🎯 Includes ටික Object එකට එකතු කළා
            tourPackageService.save(pkg);
        }
        return "redirect:/admin/packages";
    }

    @GetMapping("/packages/edit/{id}")
    public String editPackageForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var pkg = tourPackageService.findById(id);
        if (pkg.isPresent()) {
            model.addAttribute("package", pkg.get());
            model.addAttribute("destinations", destinationService.findAll());
            return "admin/packages-form";
        }
        return "redirect:/admin/packages";
    }

    // 🎯 පැකේජ් එකක් Edit කරලා Update කරද්දී mapUrl එකයි includes එකයි දෙකම සේව් වෙන්න හැදුවා
    @PostMapping("/packages/edit/{id}")
    public String editPackage(@PathVariable Long id, @RequestParam String name,
                              @RequestParam String category, @RequestParam String description,
                              @RequestParam Double price, @RequestParam Double rating,
                              @RequestParam String imageUrl, @RequestParam Integer maxTravelers,
                              @RequestParam Long destinationId, @RequestParam String mapUrl,
                              @RequestParam String includes, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var pkg = tourPackageService.findById(id);
        if (pkg.isPresent()) {
            var dest = destinationService.findById(destinationId);
            TourPackage p = pkg.get();
            p.setName(name);
            p.setCategory(category);
            p.setDescription(description);
            p.setPrice(price);
            p.setRating(rating);
            p.setImageUrl(imageUrl);
            p.setMaxTravelers(maxTravelers);
            p.setMapUrl(mapUrl);
            p.setIncludes(includes); // 🎯 Includes ටික Update කළා
            if (dest.isPresent()) {
                p.setDestination(dest.get());
            }
            tourPackageService.save(p);
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/packages/delete/{id}")
    public String deletePackage(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        tourPackageService.deleteById(id);
        return "redirect:/admin/packages";
    }

    @GetMapping("/export/bookings")
    public void exportBookings(HttpSession session, HttpServletResponse response) throws IOException {
        if (!isAdmin(session)) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=travelapp-bookings-report.csv");

        List<Booking> bookings = bookingService.findAll();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Booking ID,User,Package,Travel Date,Travelers,Total Price,Status,Created At");
            for (Booking booking : bookings) {
                writer.println(String.join(",",
                        escapeCsv(String.valueOf(booking.getId())),
                        escapeCsv(booking.getUser() != null ? booking.getUser().getName() : ""),
                        escapeCsv(booking.getTourPackage() != null ? booking.getTourPackage().getName() : ""),
                        escapeCsv(booking.getTravelDate() != null ? booking.getTravelDate().toString() : ""),
                        escapeCsv(String.valueOf(booking.getTravelers())),
                        escapeCsv(String.format("%.2f", booking.getTotalPrice())),
                        escapeCsv(booking.getStatus()),
                        escapeCsv(booking.getCreatedAt() != null ? booking.getCreatedAt().toString() : "")));
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @GetMapping("/bookings")
    public String listBookings(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings-list";
    }

    @GetMapping("/reviews")
    public String listReviews(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviews-list";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        reviewService.deleteById(id);
        return "redirect:/admin/reviews";
    }
}