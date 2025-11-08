package com.example.ordermeal.controller;

import com.example.ordermeal.entity.Order;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final OrderService orderService;

    @GetMapping("/personal")
    public String showPersonalHistory(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Order> personalHistory = orderService.getPersonalHistory(loggedInUser.getId());
        model.addAttribute("historyOrders", personalHistory);
        model.addAttribute("loggedInUser", loggedInUser);

        return "history/personal";
    }

    @GetMapping("/general")
    public String showGeneralHistory(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Order> generalHistory = orderService.getGeneralHistory();

        Map<Object, List<Order>> groupedByDate = generalHistory.stream()
                .collect(Collectors.groupingBy(Order::getOrderDate));

        model.addAttribute("groupedHistory", groupedByDate);
        model.addAttribute("loggedInUser", loggedInUser);

        return "history/general";
    }
}