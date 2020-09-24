package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.*;
import com.zmijewski.ecommerce.enums.UserSearchCriteria;
import com.zmijewski.ecommerce.enums.UserSortType;
import com.zmijewski.ecommerce.exception.EmailAlreadyExistException;
import com.zmijewski.ecommerce.exception.RoleNotFoundException;
import com.zmijewski.ecommerce.exception.UserNotFoundException;
import com.zmijewski.ecommerce.mapper.UserMapper;
import com.zmijewski.ecommerce.model.Role;
import com.zmijewski.ecommerce.model.User;
import com.zmijewski.ecommerce.properties.GuiProperties;
import com.zmijewski.ecommerce.repository.RoleRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.repository.UserSearchRepository;
import com.zmijewski.ecommerce.service.impl.UserServiceImpl;
import com.zmijewski.ecommerce.util.EmailTemplateCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserSearchRepository userSearchRepository;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    EmailTemplateCreator emailTemplateCreator;
    @Mock
    GuiProperties guiProperties;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;

    @Test
    void shouldFindUserByEmailIfExist() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.mapToUserDTO(any())).thenReturn(new UserDTO());
        //when
        UserDTO result = userService.findUserByEmail("test@test");
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotFindUserByEmailIfNotExist() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail("test@test"));
    }
    @Test
    void shouldFindUsersWithRoles() {
        //given
        int page = 0;
        int size = 10;
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new User()), PageRequest.of(page, size), 1));
        when(userMapper.mapToUserWithRoleDTO(any())).thenReturn(new UserWithRoleDTO());
        //when
        Page<UserWithRoleDTO> result = userService.findUsersWithRoles(page, size, UserSortType.ID_ASC);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindUsersWithRolesByCriteria() {
        //given
        int page = 0;
        int size = 10;
        Map<UserSearchCriteria, String> criteria = new HashMap<>();
        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new User()), PageRequest.of(page, size), 1));
        when(userMapper.mapToUserWithRoleDTO(any())).thenReturn(new UserWithRoleDTO());
        //when
        Page<UserWithRoleDTO> result = userService.findUsersWithRolesByCriteria(page, size, UserSortType.ID_ASC, criteria);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldSearchForUsersWithRoles() {
        //given
        int page = 0;
        int size = 10;
        when(userSearchRepository.searchForUsers(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(new User()), PageRequest.of(page, size), 1));
        when(userMapper.mapToUserWithRoleDTO(any())).thenReturn(new UserWithRoleDTO());
        //when
        Page<UserWithRoleDTO> result = userService.searchUsersWithRoles( page, size, UserSortType.ID_ASC, "test");
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindUserWithAddressesByEmailIfExist() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.mapToUserWithAddressesDTO(any())).thenReturn(new UserWithAddressesDTO());
        //when
        UserWithAddressesDTO result = userService.findUserWithAddresses("test@test");
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotFindUserWithAddressesByEmailIfNotExist() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.findUserWithAddresses("test@test"));
    }
    @Test
    void shouldRegisterUser() {
        //given
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("test@test")
                .password("test")
                .build();
        when(userMapper.mapRegistrationToUser(any())).thenReturn(new User());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.getByName(anyString())).thenReturn(new Role());
        when(userRepository.save(any())).thenReturn(new User());
        when(emailTemplateCreator.getRegistrationTemplate(anyString())).thenReturn("test");
        when(guiProperties.getRegistrationUrl()).thenReturn("http:localhost:4200/registration");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        userService.registerUser(registrationDTO);
        //then
        verify(userRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));

    }
    @Test
    void shouldNotRegisterIfEmailAlreadyExist() {
        //given
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("test@test")
                .password("test")
                .build();
        when(userMapper.mapRegistrationToUser(any())).thenReturn(new User());
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //when && then
        assertThrows(EmailAlreadyExistException.class, () -> userService.registerUser(registrationDTO));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rabbitTemplate);
    }
    @Test
    void shouldAddUser() {
        //given
        UserWithRoleDTO userWithRoleDTO = UserWithRoleDTO.builder()
                .roleId(1L)
                .build();
        User userToSave = User.builder()
                .email("test@test")
                .build();
        when(userMapper.mapToUser(any())).thenReturn(userToSave);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(new Role()));
        when(userRepository.save(any())).thenReturn(new User());
        when(emailTemplateCreator.getUserAddedTemplate(anyString())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        userService.addUser(userWithRoleDTO);
        //then
        verify(userRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
    }
    @Test
    void shouldNotAddUserIfEmailAlreadyExist() {
        //given
        User userToSave = User.builder()
                .email("test@test")
                .build();
        when(userMapper.mapToUser(any())).thenReturn(userToSave);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //when && then
        assertThrows(EmailAlreadyExistException.class, () -> userService.addUser(new UserWithRoleDTO()));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rabbitTemplate);
    }
    @Test
    void shouldNotAddUserIfRoleNotFound() {
        //given
        UserWithRoleDTO userWithRoleDTO = UserWithRoleDTO.builder()
                .roleId(1L)
                .build();
        User userToSave = User.builder()
                .email("test@test")
                .build();
        when(userMapper.mapToUser(any())).thenReturn(userToSave);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(RoleNotFoundException.class, () -> userService.addUser(userWithRoleDTO));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rabbitTemplate);
    }
    @Test
    void shouldModifyUser() {
        //given
        User userToUpdate = User.builder()
                .id(1L)
                .build();
        UserDTO inputUser = UserDTO.builder()
                .email("test@test")
                .build();
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.existsByEmailAndOtherId(anyString(), anyLong())).thenReturn(false);
        doNothing().when(userMapper).mapDataToUpdate(any(), any());
        when(userRepository.save(any())).thenReturn(new User());
        //when
        userService.modifyUser("test@test", inputUser);
        //then
        verify(userRepository).save(any());
    }
    @Test
    void  shouldNotModifyUserIfNotFound() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.modifyUser("test@test", new UserDTO()));
    }
    @Test
    void shouldNotModifyUserIfNewEmailAlreadyExist() {
        //given
        User userToUpdate = User.builder()
                .id(1L)
                .build();
        UserDTO inputUser = UserDTO.builder()
                .email("test@test")
                .build();
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.existsByEmailAndOtherId(anyString(), anyLong())).thenReturn(true);
        //when && then
        assertThrows(EmailAlreadyExistException.class, () -> userService.modifyUser("test@test", inputUser));
    }
    @Test
    void shouldDeleteAccount() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.mapDataToDelete(any())).thenReturn(new User());
        when(userRepository.save(any())).thenReturn(new User());
        //when
        userService.deleteAccount("test@test");
        //then
        verify(userRepository).save(any());
    }
    @Test
    void shouldNotDeleteAccountIfNotFound() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.deleteAccount("test@test"));
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void shouldConfirmAccount() {
        //given
        when(userRepository.findByToken(anyString())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        //when
        userService.confirmAccount("test");
        //then
        verify(userRepository).save(any());
    }
    @Test
    void shouldNotConfirmAccountIfNotFound() {
        //given
        when(userRepository.findByToken(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.confirmAccount("test"));
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void shouldResetPassword() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(new User());
        when(emailTemplateCreator.getResetPasswordTemplate(anyString())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        userService.resetPassword("test@test");
        //then
        verify(userRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
    }
    @Test
    void shouldNotResetPasswordIfUserNotFound() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.resetPassword("test@test"));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rabbitTemplate);
    }
    @Test
    void shouldChangePassword() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(new User());
        //when
        userService.changePassword("test@test", "test");
        //then
        verify(userRepository).save(any());
    }
    @Test
    void shouldNotChangePasswordIfUserNotFound() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.changePassword("test@test", "test"));
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void shouldSendNewToken() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        when(emailTemplateCreator.getRegistrationTemplate(anyString())).thenReturn("test");
        when(guiProperties.getRegistrationUrl()).thenReturn("http:localhost:4200/registration");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        userService.sendNewToken("test@test");
        //then
        verify(userRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
    }
    @Test
    void shouldNotSendNewTokenIfUserNotFound() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class, () -> userService.sendNewToken("test@test"));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rabbitTemplate);
    }

}