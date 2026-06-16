package com.ProfitMint.userprofile.service;

import com.ProfitMint.login.entity.User;
import com.ProfitMint.login.entity.UserSession;
import com.ProfitMint.login.repository.UserRepository;
import com.ProfitMint.login.repository.UserSessionRepository;
import com.ProfitMint.userprofile.dto.LoginDetailResponse;
import com.ProfitMint.userprofile.dto.UpdateUserRequest;
import com.ProfitMint.userprofile.dto.UpdateUserResponse;
import com.ProfitMint.userprofile.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for user profile operations.
 */
@Service
@Transactional
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository,
                              UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    /**
     * Update a user's profile by email.
     *
     * @param email   the email of the user to update
     * @param request the update request containing fields to update
     * @return the update response with updated user details
     */
    public UpdateUserResponse updateUser(String email, UpdateUserRequest request) {
        log.info("Processing update request for user with email: {}", email);

        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new UserNotFoundException(email));

        // Update only non-null fields
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone().trim());
        }


        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return UpdateUserResponse.builder()
                .message("User updated successfully")
                .name(updatedUser.getName())
                .phone(updatedUser.getPhone())
                .build();
    }

    /**
     * Get login/session details for a user by session token.
     *
     * @param token the session token
     * @return the login detail response with user and session info
     */
    @Transactional(readOnly = true)
    public List<LoginDetailResponse> getLoginDetail(String email) {
        log.info("Fetching login detail for session token");

      User user=userRepository.findByEmail(email.toLowerCase().trim()).orElseThrow(() -> new UserNotFoundException(email));
    System.out.println(user);
      List<UserSession> sessions=userSessionRepository.findAllByUserId(user.getId());
        System.out.println(sessions);
      List<LoginDetailResponse> responses=new ArrayList<>();
      for(UserSession session:sessions){

        LoginDetailResponse resp= LoginDetailResponse.builder()
                  .ipAddress(session.getIpAddress())
                  .userAgent(session.getUserAgent())
                  .sessionCreatedAt(session.getCreatedAt().toString())
                  .build();
            responses.add(resp);
          }



        return responses;
    }
}

