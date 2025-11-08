package com.example.ordermeal.service;

import com.example.ordermeal.entity.AppState;
import com.example.ordermeal.entity.User;
import com.example.ordermeal.repository.AppStateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppStateService {
    private final AppStateRepository appStateRepository;
    private final ObjectMapper objectMapper; // Spring Boot tự động cung cấp

    // Hàm tiện ích để luôn lấy AppState (ID=1)
    public AppState getAppState() {
        return appStateRepository.findById(1).orElseGet(() -> {
            AppState newAppState = new AppState();
            newAppState.setId(1);
            newAppState.setOrderingLocked(false);
            newAppState.setHasBeenSpun(false);
            return appStateRepository.save(newAppState);
        });
    }

    public boolean isOrderingLocked() {
        return getAppState().isOrderingLocked();
    }

    public void setOrderingLocked(boolean isLocked) {
        AppState appState = getAppState();
        appState.setOrderingLocked(isLocked);
        appStateRepository.save(appState);
    }

    // *** CÁC HÀM MỚI ***

    // Ghi lại kết quả quay
    public void recordSpin(User picker, List<User> selectedUsers) {
        AppState appState = getAppState();
        appState.setHasBeenSpun(true);
        appState.setPickerUser(picker);
        try {
            // Chuyển List<User> thành chuỗi JSON để lưu
            String json = objectMapper.writeValueAsString(selectedUsers);
            appState.setSelectedUsersJson(json);
        } catch (Exception e) {
            e.printStackTrace();
            appState.setSelectedUsersJson("[]"); // Lưu rỗng nếu lỗi
        }
        appStateRepository.save(appState);
    }

    // Lấy danh sách người được chọn (từ JSON)
    public List<User> getSelectedUsers() {
        AppState appState = getAppState();
        if (appState.getSelectedUsersJson() == null || appState.getSelectedUsersJson().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            // Đọc chuỗi JSON và chuyển lại thành List<User>
            return objectMapper.readValue(appState.getSelectedUsersJson(), new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Reset trạng thái quay số (khi Admin reset ngày hoặc mở khóa)
    public void resetPickerState() {
        AppState appState = getAppState();
        appState.setHasBeenSpun(false);
        appState.setPickerUser(null);
        appState.setSelectedUsersJson(null);
        appStateRepository.save(appState);
    }

    // *** BẮT ĐẦU THÊM MỚI ***
    // Cập nhật thông tin ngân hàng
    public void updateBankInfo(String bin, String accountNo, String accountName, String bankName) {
        AppState appState = getAppState();
        appState.setBankBin(bin);
        appState.setBankAccountNo(accountNo);
        appState.setBankAccountName(accountName);
        appState.setBankName(bankName);
        appStateRepository.save(appState);
    }
    // *** KẾT THÚC THÊM MỚI ***
}