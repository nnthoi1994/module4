package com.example.form_dang_ky.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    //    @Size(min = 1, max = 45, message = "Firstname phải từ 1 đến 45 ký tự")
    private String firstName;
    //    @Size(min = 1, max = 45, message = "Lastname phải từ 1 đến 45 ký tự")
    private String lastName;
    //    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0)")
    private String phoneNumber;
    //    @Min(value = 18, message = "Tuổi phải >= 18")
    private Integer age;
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

}
