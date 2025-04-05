package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.UserDao;
import kg.attractor.payment_system.exception.RecordAlreadyExistsException;
import kg.attractor.payment_system.model.User;
import kg.attractor.payment_system.dto.UserDto;
import kg.attractor.payment_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void registerUser(UserDto userDto) {
        if (userDao.existsByPhone(userDto.getPhoneNumber())) {
            throw new RecordAlreadyExistsException("User with this phone number already exists.");
        }

        User user = new User();
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDao.save(user);
    }

    @Override
    public UserDto getUserDtoByPhone(String phone) {
        User user = userDao.findByPhone(phone);
        return mapToDto(user);
    }

    @Override
    public boolean isUserExistsByPhone(String phone) {
        return userDao.existsByPhone(phone);
    }

    @Override
    public boolean isUserExistsById(Long id) {
        return userDao.existsById(id);
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        User user = userDao.findById(id);
        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(user.getPhoneNumber(), user.getUsername(), user.getPassword());
    }
}
