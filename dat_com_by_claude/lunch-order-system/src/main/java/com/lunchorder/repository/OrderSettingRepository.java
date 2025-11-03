package com.lunchorder.repository;

import com.lunchorder.model.OrderSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderSettingRepository extends JpaRepository<OrderSetting, Long> {

    Optional<OrderSetting> findBySettingDate(LocalDate settingDate);
}