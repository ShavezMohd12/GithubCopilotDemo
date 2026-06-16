package com.ProfitMint.login.repository;

import com.ProfitMint.login.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UserSession entity operations.
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    /**
     * Find a session by token.
     *
     * @param token the session token
     * @return Optional containing the session if found
     */
    Optional<UserSession> findByToken(String token);

    /**
     * Delete a session by token.
     *
     * @param token the session token
     */
    void deleteByToken(String token);

    /**
     * Delete all sessions for a user.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * Delete all expired sessions.
     *
     * @param now current timestamp
     */
    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.expiresAt < :now")
    void deleteExpiredSessions(@Param("now") LocalDateTime now);

    /**
     * Find all sessions for a user.
     *
     * @param userId the user ID
     * @return list of sessions for the user
     */
    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId ORDER BY s.createdAt DESC")
    List<UserSession> findAllByUserId(@Param("userId") Long userId);
}
