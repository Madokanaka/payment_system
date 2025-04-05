package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.UserDto;

public interface UserService {
    void registerUser(UserDto userDto);

    UserDto getUserDtoByPhone(String phone);

    boolean isUserExistsByPhone(String phone);

    boolean isUserExistsById(Long id);

    UserDto getUserDtoById(Long id);
}
