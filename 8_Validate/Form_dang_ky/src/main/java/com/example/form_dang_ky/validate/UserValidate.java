package com.example.form_dang_ky.validate;

import com.example.form_dang_ky.dto.UserDto;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidate implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target,  Errors errors) {
        UserDto userDto = (UserDto) target;String firstName  = userDto.getFirstName();
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.rejectValue("firstName", "firstName.empty", "Firstname không được để trống");
        } else if (firstName.length() < 1 || firstName.length() > 45) {
            errors.rejectValue("firstName", "firstName.size", "Firstname phải từ 1 đến 45 ký tự");
        } else if (!firstName.matches("^[A-Za-zÀ-ỹ\\s]+$")) {
            errors.rejectValue("firstName", "firstName.format", "Firstname phải chỉ chứa chữ cái");
        }
        String lastName = userDto.getLastName();
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.rejectValue("lastName", "lastName.empty", "Lastname không được để trống");
        } else if (lastName.length() < 1 || lastName.length() > 45) {
            errors.rejectValue("lastName", "lastName.size", "Lastname phải từ 1 đến 45 ký tự");
        } else if (!lastName.matches("^[A-Za-zÀ-ỹ\\s]+$")) {
            errors.rejectValue("lastName", "lastName.format", "Lastname phải chỉ chứa chữ cái");
        }
        String phone = userDto.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            errors.rejectValue("phoneNumber", "phone.empty", "Số điện thoại không được để trống");
        } else if (!phone.matches("^0\\d{9}$")) {
            errors.rejectValue("phoneNumber", "phone.format", "Số điện thoại phải bắt đầu bằng 0 và đủ 10 số");
        }
        Integer age = userDto.getAge();
        if (age == null) {
            errors.rejectValue("age", "age.empty", "Tuổi không được để trống");
        } else if (age < 18) {
            errors.rejectValue("age", "age.invalid", "Tuổi phải >= 18");
        }
    }

}
