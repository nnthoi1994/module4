package com.example.quan_ly_san_pham.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "app_state")
public class AppState {
    @Id
    private int id;

    @Column(nullable = false)
    private boolean isOrderingLocked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOrderingLocked() {
        return isOrderingLocked;
    }

    public void setOrderingLocked(boolean orderingLocked) {
        isOrderingLocked = orderingLocked;
    }
// Getters and Setters
}
