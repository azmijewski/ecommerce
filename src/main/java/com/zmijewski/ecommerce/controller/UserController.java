package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.*;
import com.zmijewski.ecommerce.enums.UserSearchCriteria;
import com.zmijewski.ecommerce.enums.UserSortType;
import com.zmijewski.ecommerce.service.UserService;
import com.zmijewski.ecommerce.util.ResponseUriBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class UserController {

    private final UserService userService;
    private final ResponseUriBuilder uriBuilder;

    public UserController(UserService userService, ResponseUriBuilder uriBuilder) {
        this.userService = userService;
        this.uriBuilder = uriBuilder;
    }


    @GetMapping("users/userByEmail")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam(name = "email") String email) {
        log.info("Getting user with email: {}", email);
        UserDTO result = userService.findUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("usersWithRoles")
    @Secured({"ADMIN"})
    public ResponseEntity<Page<UserWithRoleDTO>> getUsersWithRoles(@RequestParam(name = "page") @Min(0) int page,
                                                                   @RequestParam(name = "size") @Min(1) int size,
                                                                   @RequestParam(name = "sort", defaultValue = "ID_ASC") UserSortType sortType) {
        log.info("Getting users with roles page: {}, size{}", page, size);
        Page<UserWithRoleDTO> result = userService.findUsersWithRoles(page, size, sortType);
        return ResponseEntity.ok(result);
    }

    @GetMapping("usersWithRolesByCriteria")
    @Secured({"ADMIN"})
    public ResponseEntity<Page<UserWithRoleDTO>> getUsersWithRolesByCriteria(@RequestParam(name = "page") @Min(0) int page,
                                                                             @RequestParam(name = "size") @Min(1) int size,
                                                                             @RequestParam(name = "sort", defaultValue = "ID_ASC") UserSortType sortType,
                                                                             @RequestParam Map<UserSearchCriteria, String> criteria) {
        log.info("Getting users with roles page: {}, size{} by criteria: {}", page, size, criteria);
        Page<UserWithRoleDTO> result = userService.findUsersWithRolesByCriteria(page, size, sortType, criteria);
        return ResponseEntity.ok(result);
    }
    @PostMapping("usersWithRoles")
    @Secured({"ADMIN"})
    public ResponseEntity<Page<UserWithRoleDTO>> searchUsersWithRoles(@RequestParam(name = "page") @Min(0) int page,
                                                                      @RequestParam(name = "size") @Min(1) int size,
                                                                      @RequestParam(name = "sort", defaultValue = "ID_ASC") UserSortType sortType,
                                                                      @RequestBody @Valid SearchDTO searchDTO) {
        log.info("Looking for users with roles page: {}, size{} by searchWords: {}", page, size, searchDTO);
        Page<UserWithRoleDTO> result = userService.searchUsersWithRoles(page, size, sortType, searchDTO.getSearchWords());
        return ResponseEntity.ok(result);
    }
    @GetMapping("users/logged")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<UserWithAddressesDTO> getLoggedUser(Principal principal) {
        log.info("GetTing logged user data with email: {}", principal.getName());
        UserWithAddressesDTO result = userService.findUserWithAddresses(principal.getName());
        return ResponseEntity.ok(result);
    }
    @PostMapping("users/registration")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationDTO registrationDTO) {
        log.info("Trying to register user with email: {}", registrationDTO.getEmail());
        userService.registerUser(registrationDTO);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("users")
    @Secured({"ADMIN"})
    public ResponseEntity<?> addNewUser(@RequestBody @Valid UserWithRoleDTO userWithRoleDTO) {
        log.info("Trying to add new user with email: {}", userWithRoleDTO.getUser().getEmail());
        userService.addUser(userWithRoleDTO);
        URI location =
                uriBuilder.buildUriWithAddedPathAndEmail("/userByEmail", userWithRoleDTO.getUser().getEmail());
        return ResponseEntity.created(location).build();
    }
    @PutMapping("users/logged")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<?> modifyLoggedUser(Principal principal,
                                               @RequestBody @Valid UserDTO userDTO) {
        log.info("Modifying user with email: {}", principal.getName());
        userService.modifyUser(principal.getName(), userDTO);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("users/logged")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<?> deleteLoggedUserAccount(Principal principal) {
        log.info("Deleting user with email: {}", principal.getName());
        userService.deleteAccount(principal.getName());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("users/userByEmail")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteUserAccount(@RequestParam(name = "email") String email) {
        log.info("Deleting user with email: {}", email);
        userService.deleteAccount(email);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("users/confirmation")
    public ResponseEntity<?> sendNewRegistrationToken (@RequestParam(name = "email") String email) {
        log.info("Trying to send new registration token for user: {}", email);
        userService.sendNewRegistrationToken(email);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("users/confirmation/{token}")
    public ResponseEntity<?> confirmAccount(@PathVariable(name = "token") @NotBlank String token) {
        log.info("Trying to confirm account with token: {}", token);
        userService.confirmAccount(token);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("users/resetting")
    public ResponseEntity<?> sendResetToken(@RequestParam(name = "email") String email) {
        log.info("Trying to send reset password token for user with id: {}" + email);
        userService.sendResetPasswordToken(email);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("users/resetting/{token}")
    public ResponseEntity<?> resetPassword (@PathVariable(name = "token") @NotBlank String token,
                                            @RequestBody @Valid CredentialDTO credentialDTO) {
        log.info("Trying to reset password with token: {}", token);
        userService.resetPassword(token, credentialDTO.getNewPassword());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("users/logged/credential")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<?> changePassword (Principal principal,
                                             @RequestBody @Valid CredentialDTO credentialDTO) {
        log.info("Trying to change password for user: {}", principal.getName());
        userService.changePassword(principal.getName(), credentialDTO.getNewPassword());
        return ResponseEntity.noContent().build();
    }


}
