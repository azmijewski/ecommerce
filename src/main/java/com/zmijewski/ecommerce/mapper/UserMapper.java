package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.RegistrationDTO;
import com.zmijewski.ecommerce.dto.UserDTO;
import com.zmijewski.ecommerce.dto.UserWithAddressesDTO;
import com.zmijewski.ecommerce.dto.UserWithRoleDTO;
import com.zmijewski.ecommerce.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {

    static final String DELETED_ACCOUNT_DATA = "###";

    UserDTO mapToUserDTO(User user);
    User mapToUser(UserDTO userDTO);
    User mapRegistrationToUser(RegistrationDTO registrationDTO);
    UserWithAddressesDTO mapToUserWithAddressesDTO(User user);
    UserWithRoleDTO mapToUserWithRoleDTO(User user);
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "isActive", ignore = true)
    })
    void mapDataToUpdate(@MappingTarget User userToUpdate, UserDTO userDTO);
    @Mappings({
            @Mapping(target = "email", constant = DELETED_ACCOUNT_DATA),
            @Mapping(target = "firstName", constant = DELETED_ACCOUNT_DATA),
            @Mapping(target = "lastName", constant = DELETED_ACCOUNT_DATA),
            @Mapping(target = "phoneNumber", constant = DELETED_ACCOUNT_DATA),
    })
    User mapDataToDelete(User userToDelete);

    @AfterMapping
    default void mapUserToUserDTO(@MappingTarget UserWithAddressesDTO userWithAddressesDTO, User user) {
        userWithAddressesDTO.setUser(mapToUserDTO(user));
    }
    @AfterMapping
    default void mapUserToUserDTO(@MappingTarget UserWithRoleDTO userWithRoleDTO, User user) {
        userWithRoleDTO.setUser(mapToUserDTO(user));
    }

}
