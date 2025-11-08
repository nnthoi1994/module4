package com.example.ordermeal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_state")
public class AppState {
    @Id
    private int id; // Sẽ luôn là 1 (Singleton)

    @Column(nullable = false)
    private boolean isOrderingLocked = false;

    @Column(nullable = false)
    private boolean hasBeenSpun = false; // Đã quay số hay chưa

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picker_user_id")
    private User pickerUser; // Người đã bấm nút quay

    @Column(columnDefinition = "TEXT")
    private String selectedUsersJson; // Lưu danh sách người được chọn

    // *** BẮT ĐẦU THÊM MỚI ***
    private String bankBin; // Mã BIN ngân hàng (VD: 970436)
    private String bankAccountNo; // Số tài khoản
    private String bankAccountName; // Tên chủ tài khoản
    private String bankName; // Tên ngân hàng (VD: Vietcombank)
    // *** KẾT THÚC THÊM MỚI ***
}