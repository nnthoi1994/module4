package com.example.quan_ly_san_pham.service;



import com.example.quan_ly_san_pham.entity.AppState;
import com.example.quan_ly_san_pham.repository.AppStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppStateService {

    @Autowired
    private AppStateRepository appStateRepository;

    /**
     * Kiểm tra xem chức năng đặt hàng có đang bị khóa hay không.
     * @return true nếu đang bị khóa, false nếu đang được phép.
     */
    public boolean isOrderingLocked() {
        // Luôn tìm bản ghi có id = 1.
        // Nếu bản ghi tồn tại, trả về giá trị isOrderingLocked của nó.
        // Nếu vì lý do nào đó mà bản ghi chưa tồn tại, mặc định là không khóa (false).
        return appStateRepository.findById(1)
                .map(AppState::isOrderingLocked)
                .orElse(false);
    }

    /**
     * Cập nhật trạng thái khóa/mở khóa chức năng đặt hàng.
     * @param isLocked true để khóa, false để mở khóa.
     */
    public void setOrderingLocked(boolean isLocked) {
        // Cố gắng tìm bản ghi có id = 1.
        // Nếu không tìm thấy, tạo một đối tượng AppState mới với id = 1.
        AppState appState = appStateRepository.findById(1).orElse(new AppState());
        appState.setId(1); // Đảm bảo id luôn là 1

        // Cập nhật trạng thái và lưu lại vào database.
        appState.setOrderingLocked(isLocked);
        appStateRepository.save(appState);
    }
}
