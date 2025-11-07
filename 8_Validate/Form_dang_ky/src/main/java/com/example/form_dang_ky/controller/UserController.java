package com.example.form_dang_ky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/form")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/form";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("userDto") UserDto userDto,
                          BindingResult bindingResult) {
        UserValidate userValidate= new UserValidate();
        userValidate.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/form";
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());

        userService.addUser(user);

        return "user/result";
    }
}
