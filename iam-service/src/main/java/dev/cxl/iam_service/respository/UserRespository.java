package dev.cxl.iam_service.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.entity.User;

@Repository
public interface UserRespository extends JpaRepository<User, String> {
    Optional<User> findByUserMail(String userMail);

    Optional<User> findByUserKCLID(String string);

    boolean existsByUserMail(String userMail);

    Page<User> findAll(Pageable pageable);

    //     CREATE EXTENSION IF NOT EXISTS unaccent;
    @Query(
            value = "SELECT * FROM user_accounts "
                    + "WHERE unaccent(CONCAT(user_name, ' ', user_mail, ' ', first_name, ' ', last_name)) "
                    + "ILIKE unaccent('%' || :key || '%')",
            nativeQuery = true)
    Page<User> findUsersByKey(@Param("key") String key, Pageable pageable);
}
