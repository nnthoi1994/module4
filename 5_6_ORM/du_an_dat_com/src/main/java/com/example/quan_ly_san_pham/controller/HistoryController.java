package com.example.quan_ly_san_pham.controller;



import com.example.quan_ly_san_pham.entity.Order;
import com.example.quan_ly_san_pham.entity.User;
import com.example.quan_ly_san_pham.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/history") // All URLs in this controller start with /history
public class HistoryController {

    @Autowired
    private OrderService orderService;

    /**
     * Displays the personal order history for the currently logged-in user.
     */
    @GetMapping("/personal")
    public String showPersonalHistory(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // If not logged in, redirect to the login page
            return "redirect:/login";
        }

        // Fetch the personal history from the service
        List<Order> personalHistory = orderService.getPersonalHistory(loggedInUser.getId());
        model.addAttribute("historyOrders", personalHistory);
        model.addAttribute("loggedInUser", loggedInUser); // Pass user info to the view for the header

        return "history/personal"; // Renders the personal.html file inside a 'history' folder
    }

    /**
     * Displays the general order history from all previous days for all users.
     */
    @GetMapping("/general")
    public String showGeneralHistory(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Fetch the general history from the service
        List<Order> generalHistory = orderService.getGeneralHistory();

        // Group the orders by date to display them clearly on the page
        // The result will be a Map where the key is the date and the value is the list of orders for that date
        Map<Object, List<Order>> groupedByDate = generalHistory.stream()
                .collect(Collectors.groupingBy(Order::getOrderDate));

        model.addAttribute("groupedHistory", groupedByDate);
        model.addAttribute("loggedInUser", loggedInUser);

        return "history/general"; // Renders the general.html file
    }
}
