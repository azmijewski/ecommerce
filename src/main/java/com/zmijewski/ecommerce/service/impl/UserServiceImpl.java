package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.*;
import com.zmijewski.ecommerce.exception.EmailAlreadyExistException;
import com.zmijewski.ecommerce.exception.RoleNotFoundException;
import com.zmijewski.ecommerce.exception.UserNotFoundException;
import com.zmijewski.ecommerce.mapper.UserMapper;
import com.zmijewski.ecommerce.model.entity.Role;
import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.model.enums.UserSortType;
import com.zmijewski.ecommerce.repository.RoleRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.repository.UserSearchRepository;
import com.zmijewski.ecommerce.service.UserService;
import com.zmijewski.ecommerce.util.EmailTemplateCreator;
import com.zmijewski.ecommerce.util.PasswordGenerator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:subject.properties")
public class UserServiceImpl implements UserService {

    private static final String USER_ROLE = "USER";
    private static final String EMAIL_QUEUE = "emailQueue";

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EmailTemplateCreator emailTemplateCreator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final String registrationSubject;
    private final String resetPasswordSubject;

    public UserServiceImpl(UserRepository userRepository,
                           UserSearchRepository userSearchRepository,
                           RabbitTemplate rabbitTemplate,
                           EmailTemplateCreator emailTemplateCreator,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           @Value("${register-subject}") String registrationSubject,
                           @Value("${reset-password-subject}") String resetPasswordSubject) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.emailTemplateCreator = emailTemplateCreator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.registrationSubject = registrationSubject;
        this.resetPasswordSubject = resetPasswordSubject;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::mapToUserDTO)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }

    @Override
    public Page<UserWithRoleDTO> findUsersWithRoles(int page, int size, UserSortType userSortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(userSortType.getSortType()), userSortType.getField());
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable)
                .map(userMapper::mapToUserWithRoleDTO);
    }

    @Override
    public Page<UserWithRoleDTO> searchUsersWithRoles(int page, int size, UserSortType userSortType, String searchWords) {
        return userSearchRepository.searchForUsers(searchWords, page, size, userSortType)
                .map(userMapper::mapToUserWithRoleDTO);
    }


    @Override
    public UserWithAddressesDTO findUserWithAddresses(String email) {
        return userRepository.findWithAddressesByEmail(email)
                .map(userMapper::mapToUserWithAddressesDTO)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }

    @Override
    public void registerUser(RegistrationDTO registration) {
        User userToRegister = userMapper.mapRegistrationToUser(registration);
        if (userRepository.existsByEmail(registration.getEmail())) {
            throw new EmailAlreadyExistException("User with email: " + registration.getEmail() + " already exist");
        }
        userToRegister.setPassword(passwordEncoder.encode(registration.getPassword()));
        String token = UUID.randomUUID().toString();
        userToRegister.setToken(token);
        userToRegister.setTokenCreatedAt(new Date());
        userToRegister.setIsActive(false);
        Role userRole = roleRepository.getByName(USER_ROLE);
        userToRegister.setRole(userRole);
        userRepository.save(userToRegister);
        String emailContent = emailTemplateCreator.getRegistrationTemplate(token);
        addMailSendToQueue(registration.getEmail(), registrationSubject, emailContent);
    }

    @Override
    public Long addUser(UserWithRoleDTO userWithRole) {
        User userToSave = userMapper.mapToUser(userWithRole.getUser());
        if (userRepository.existsByEmail(userToSave.getEmail())) {
            throw new EmailAlreadyExistException("User with email: " + userToSave.getEmail() + " already exist");
        }
        Role userRole = roleRepository.findById(userWithRole.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Could not find role with id: " + userWithRole.getRoleId()));
        userToSave.setRole(userRole);
        String password = PasswordGenerator.generatePassword();
        userToSave.setPassword(passwordEncoder.encode(password));
        userToSave.setIsActive(true);
        Long result = userRepository.save(userToSave).getId();
        String emailContent = emailTemplateCreator.getUserAddedTemplate(password);
        addMailSendToQueue(userToSave.getEmail(), registrationSubject, emailContent);
        return result;
    }

    @Override
    public void modifyUser(String email, UserDTO user) {
        User userToUpdate = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        if (userRepository.existsByEmailAndOtherId(user.getEmail(), userToUpdate.getId())) {
            throw new EmailAlreadyExistException("User with email: " + user.getEmail() + " already exist");
        }
        userMapper.mapDataToUpdate(userToUpdate, user);
        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteAccount(String email) {
        User userToDelete = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        User withMappedData = userMapper.mapDataToDelete(userToDelete);
        withMappedData.setIsActive(false);
        userRepository.save(withMappedData);
    }

    @Override
    public void confirmAccount(String token) {
        User userToConfirm = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with token:" + token));
        userToConfirm.setIsActive(true);
        userToConfirm.setToken(null);
        userRepository.save(userToConfirm);
    }


    @Override
    public void resetPassword(String token, String newPassword) {
        User userToResetPassword = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + token));
        String password = PasswordGenerator.generatePassword();
        userToResetPassword.setPassword(passwordEncoder.encode(password));
       userRepository.save(userToResetPassword);

    }

    @Override
    public void changePassword(String email, String newPassword) {
        User userToChangePassword = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        userToChangePassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToChangePassword);
    }

    @Override
    public void sendNewRegistrationToken(String email) {
        User userToSetNewToken = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        String token = UUID.randomUUID().toString();
        userToSetNewToken.setToken(token);
        userToSetNewToken.setIsActive(false);
        userToSetNewToken.setTokenCreatedAt(new Date());
        userRepository.save(userToSetNewToken);
        String emailContent = emailTemplateCreator.getRegistrationTemplate(token);
        addMailSendToQueue(userToSetNewToken.getEmail(), registrationSubject, emailContent);
    }

    @Override
    public void sendResetPasswordToken(String email) {
        User userToResetPassword = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        String token = UUID.randomUUID().toString();
        userToResetPassword.setToken(token);
        userToResetPassword.setTokenCreatedAt(new Date());
        userRepository.save(userToResetPassword);
        String emailContent = emailTemplateCreator .getResetPasswordTemplate(token);
        addMailSendToQueue(userToResetPassword.getEmail(), resetPasswordSubject, emailContent);
    }

    private void addMailSendToQueue(String sendTo, String subject, String content) {
        EmailDTO email = new EmailDTO();
        email.setContent(content);
        email.setSendTo(sendTo);
        email.setSubject(subject);
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, email);
    }
}
