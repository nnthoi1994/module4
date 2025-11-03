package com.lunchorder.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_settings")
public class OrderSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_date", nullable = false, unique = true)
    private LocalDate settingDate;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "picker1_username")
    private String picker1Username;

    @Column(name = "picker2_username")
    private String picker2Username;

    // Constructors
    public OrderSetting() {}

    public OrderSetting(LocalDate settingDate) {
        this.settingDate = settingDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(LocalDate settingDate) {
        this.settingDate = settingDate;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getPicker1Username() {
        return picker1Username;
    }

    public void setPicker1Username(String picker1Username) {
        this.picker1Username = picker1Username;
    }

    public String getPicker2Username() {
        return picker2Username;
    }

    public void setPicker2Username(String picker2Username) {
        this.picker2Username = picker2Username;
    }
}