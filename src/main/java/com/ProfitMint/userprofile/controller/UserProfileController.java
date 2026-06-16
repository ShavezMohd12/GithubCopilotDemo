package com.ProfitMint.userprofile.controller;

import com.ProfitMint.login.exception.InvalidSessionException;
import com.ProfitMint.login.util.CookieUtil;
import com.ProfitMint.userprofile.dto.LoginDetailResponse;
import com.ProfitMint.userprofile.dto.UpdateUserRequest;
import com.ProfitMint.userprofile.dto.UpdateUserResponse;
import com.ProfitMint.userprofile.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user profile endpoints.
 */
@RestController
@RequestMapping
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserProfileController {

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final UserProfileService userProfileService;
    private final CookieUtil cookieUtil;

    public UserProfileController(UserProfileService userProfileService, CookieUtil cookieUtil) {
        this.userProfileService = userProfileService;
        this.cookieUtil = cookieUtil;
    }

    /**
     * Update user profile endpoint.
     * Accepts email as a query parameter to identify the user.
     *
     * @param email   the email of the user to update
     * @param request the update request body
     * @return the updated user profile response
     */
    @PutMapping("/updateUser")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @RequestParam("email") String email,
            @Valid @RequestBody UpdateUserRequest request) {

        log.info("Received update user request for email: {}", email);
        UpdateUserResponse response = userProfileService.updateUser(email, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get login/session detail endpoint.
     * Uses the session cookie to identify the current user and return their login details.
     *
     * @param servletRequest the HTTP servlet request
     * @return the login detail response with user and session info
     */
    @GetMapping("/loginDetail")
    public ResponseEntity<List<LoginDetailResponse>> getLoginDetail(@RequestParam("email") String email) {

        log.info("Received login detail request");



        List<LoginDetailResponse> response = userProfileService.getLoginDetail(email);
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

