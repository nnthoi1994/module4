package org.example.demo2.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {

    // Hiển thị trang máy tính khi người dùng truy cập lần đầu
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showCalculator() {
        return "home";
    }

    // Xử lý yêu cầu tính toán được gửi từ form
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public String calculate(
            @RequestParam("firstNumber") double firstNumber,
            @RequestParam("secondNumber") double secondNumber,
            @RequestParam("operator") String operator,
            Model model
    ) {
        double result = 0;
        String errorMessage = "";

        switch (operator) {
            case "Addition(+)":
                result = firstNumber + secondNumber;
                break;
            case "Subtraction(-)":
                result = firstNumber - secondNumber;
                break;
            case "Multiplication(X)":
                result = firstNumber * secondNumber;
                break;
            case "Division(/)":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    errorMessage = "Cannot divide by zero!";
                }
                break;
        }

        // Đưa các giá trị cũ và kết quả vào model để hiển thị lại trên form
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("secondNumber", secondNumber);

        if (!errorMessage.isEmpty()) {
            model.addAttribute("result", errorMessage);
        } else {
            // Lấy tên phép toán từ chuỗi "Addition(+)" -> "Addition"
            String operationName = operator.substring(0, operator.indexOf('('));
            model.addAttribute("result", "Result " + operationName + " : " + result);
        }

        return "home";
    }
}
