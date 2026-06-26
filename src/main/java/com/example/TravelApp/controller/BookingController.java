package com.example.TravelApp.controller;

import com.example.TravelApp.model.Booking;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.model.User;
import com.example.TravelApp.service.BookingService;
import com.example.TravelApp.service.EmailService; // 🎯 EmailService එක Import කළා
import com.example.TravelApp.service.TourPackageService;
import com.example.TravelApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final TourPackageService tourPackageService;
    private final UserService userService;
    private final EmailService emailService; // 🎯 EmailService එක Inject කරන්න Field එකක් හැදුවා

    // Constructor එක ඇතුලට EmailService එකත් ඇතුලත් කළා
    public BookingController(BookingService bookingService, TourPackageService tourPackageService, UserService userService, EmailService emailService) {
        this.bookingService = bookingService;
        this.tourPackageService = tourPackageService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/book/{packageId}")
    public String bookPackage(@PathVariable Long packageId, Model model) {
        TourPackage tourPackage = tourPackageService.findById(packageId).orElse(null);
        if (tourPackage == null) {
            return "redirect:/packages";
        }
        model.addAttribute("package", tourPackage);
        model.addAttribute("booking", new Booking());
        return "book-package";
    }

    @PostMapping("/book")
    public String placeBooking(@ModelAttribute Booking booking,
                               @RequestParam Long packageId,
                               @RequestParam String userEmail,
                               Model model) {
        Optional<TourPackage> optionalPackage = tourPackageService.findById(packageId);
        Optional<User> optionalUser = userService.findByEmail(userEmail);
        if (optionalPackage.isEmpty()) {
            return "redirect:/packages";
        }
        TourPackage tourPackage = optionalPackage.get();

        if (optionalUser.isEmpty()) {
            // user not found - return to booking page with error and package info
            model.addAttribute("error", "User not found. Please register or use a valid email.");
            model.addAttribute("package", tourPackage);
            model.addAttribute("booking", booking == null ? new Booking() : booking);
            return "book-package";
        }
        User user = optionalUser.get();
        booking.setTourPackage(tourPackage);
        booking.setUser(user);

        // 💰 Total Price එක ගණනය කිරීම
        double totalPrice = tourPackage.getPrice() * booking.getTravelers();
        booking.setTotalPrice(totalPrice);

        // 💾 Booking එක Database එකට සේව් කිරීම
        bookingService.save(booking);

        // 📧 🎯 බුකින් එක සේව් වුණු ගමන් යූසර්ට Email එකක් යවන කොටස:
        try {
            emailService.sendBookingSuccessEmail(user.getEmail(), user.getName(), tourPackage.getName(), totalPrice);
        } catch (Exception e) {
            System.out.println("❌ Email Sending Failed: " + e.getMessage());
            // Email එක යැවීමේදී ගැටළුවක් වුවත් බුකින් එක සාර්ථක නිසා confirmation එක පෙන්වීමට ඉඩ හරිනවා
        }

        model.addAttribute("booking", booking);
        return "booking-confirmation";
    }

    @GetMapping("/bookings")
    public String bookingHistory(@RequestParam String userEmail, Model model) {
        Optional<User> user = userService.findByEmail(userEmail);
        if (user.isEmpty()) {
            model.addAttribute("bookings", List.of());
            model.addAttribute("userEmail", userEmail);
            return "booking-history";
        }
        List<Booking> bookings = bookingService.findByUser(user.get());
        model.addAttribute("bookings", bookings);
        model.addAttribute("userEmail", userEmail);
        return "booking-history";
    }
}