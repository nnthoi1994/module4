package com.lunchorder.controller;

import com.lunchorder.model.Order;
import com.lunchorder.model.User;
import com.lunchorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/personal")
    public String personalHistory(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        List<Order> orders = orderService.getUserOrderHistory(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("orders", orders);

        return "history-personal";
    }

    @GetMapping("/all")
    public String allHistory(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<LocalDate, List<Order>> orderHistory = orderService.getAllOrderHistory();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("orderHistory", orderHistory);

        return "history-all";
    }
}