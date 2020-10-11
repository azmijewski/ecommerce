package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.RegistrationDTO;
import com.zmijewski.ecommerce.dto.UserDTO;
import com.zmijewski.ecommerce.dto.UserWithAddressesDTO;
import com.zmijewski.ecommerce.dto.UserWithRoleDTO;
import com.zmijewski.ecommerce.model.enums.UserSortType;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO findUserByEmail(String email);
    Page<UserWithRoleDTO> findUsersWithRoles(int page, int size, UserSortType userSortType);
    Page<UserWithRoleDTO> searchUsersWithRoles(int page, int size, UserSortType userSortType, String searchWords);
    UserWithAddressesDTO findUserWithAddresses(String email);
    void registerUser(RegistrationDTO registration);
    Long addUser(UserWithRoleDTO userWithRole);
    void modifyUser(String email, UserDTO user);
    void deleteAccount(String email);
    void confirmAccount(String token);
    void resetPassword(String token, String newPassword);
    void changePassword(String email, String newPassword);
    void sendNewRegistrationToken(String email);
    void sendResetPasswordToken(String email);
}
