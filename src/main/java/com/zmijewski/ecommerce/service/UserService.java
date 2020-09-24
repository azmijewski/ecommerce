package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.RegistrationDTO;
import com.zmijewski.ecommerce.dto.UserDTO;
import com.zmijewski.ecommerce.dto.UserWithAddressesDTO;
import com.zmijewski.ecommerce.dto.UserWithRoleDTO;
import com.zmijewski.ecommerce.enums.UserSearchCriteria;
import com.zmijewski.ecommerce.enums.UserSortType;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserService {
    UserDTO findUserByEmail(String email);
    Page<UserWithRoleDTO> findUsersWithRoles(int page, int size, UserSortType userSortType);
    Page<UserWithRoleDTO> findUsersWithRolesByCriteria(int page, int size, UserSortType userSortType,  Map<UserSearchCriteria, String> criteriaMap);
    Page<UserWithRoleDTO> searchUsersWithRoles(int page, int size, UserSortType userSortType, String searchWords);
    UserWithAddressesDTO findUserWithAddresses(String email);
    void registerUser(RegistrationDTO registration);
    Long addUser(UserWithRoleDTO userWithRole);
    void modifyUser(String email, UserDTO user);
    void deleteAccount(String email);
    void confirmAccount(String token);
    void resetPassword(String email);
    void changePassword(String email, String newPassword);
    void sendNewToken(String email);
}
