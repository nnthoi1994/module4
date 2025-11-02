package com.example.quan_ly_san_pham.service;



import com.example.quan_ly_san_pham.entity.User;
import com.example.quan_ly_san_pham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService { // Implementing the interface

    @Autowired
    private UserRepository userRepository;

    /**
     * Đăng ký một người dùng mới.
     * @param user Đối tượng User chứa thông tin từ form đăng ký.
     * @return Người dùng đã được lưu vào database.
     */
    @Override
    public User register(User user) {
        // Luôn mặc định vai trò của người dùng mới là CUSTOMER
        user.setRole("CUSTOMER");
        return userRepository.save(user);
    }

    /**
     * Xác thực thông tin đăng nhập của người dùng.
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return Optional chứa User nếu đăng nhập thành công, ngược lại trả về Optional rỗng.
     */
    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Chỉ trả về user nếu tìm thấy và mật khẩu khớp
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            return userOptional;
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Kiểm tra xem một username đã tồn tại trong hệ thống chưa.
     * @param username Tên đăng nhập cần kiểm tra.
     * @return true nếu đã tồn tại, false nếu chưa.
     */
    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Kiểm tra xem một email đã tồn tại trong hệ thống chưa.
     * @param email Email cần kiểm tra.
     * @return true nếu đã tồn tại, false nếu chưa.
     */
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
