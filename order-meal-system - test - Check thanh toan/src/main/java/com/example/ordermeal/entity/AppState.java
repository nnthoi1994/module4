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

    // *** CÁC TRƯỜNG MỚI ***

    @Column(nullable = false)
    private boolean hasBeenSpun = false; // Đã quay số hay chưa

    @ManyToOne(fetch = FetchType.EAGER) // Lấy thông tin người quay ngay
    @JoinColumn(name = "picker_user_id")
    private User pickerUser; // Người đã bấm nút quay

    @Column(columnDefinition = "TEXT")
    private String selectedUsersJson; // Lưu danh sách người được chọn (dưới dạng JSON)
}