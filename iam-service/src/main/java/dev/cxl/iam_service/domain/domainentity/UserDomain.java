package dev.cxl.iam_service.domain.domainentity;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserRepalcePass;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDomain extends AuditableEntityDomain {

    String userID;
    String userKCLID;
    String profile;
    String userName;
    String userMail;
    String passWord;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String avatar;
    Boolean enabled;
    Boolean deleted = false;
    private boolean isRoot = false;


    public UserDomain create(UserCreationRequest userCreationRequest) {
        return UserDomain.builder()
                .userID(UUID.randomUUID().toString())
                .userName(userCreationRequest.getUserName())
                .userMail(userCreationRequest.getUserMail())
                .passWord(userCreationRequest.getPassWord())
                .firstName(userCreationRequest.getFirstName())
                .lastName(userCreationRequest.getLastName())
                .dateOfBirth(userCreationRequest.getDateOfBirth())
                .avatar(userCreationRequest.getAvatar())
                .enabled(true)
                .deleted(false)
                .isRoot(false)
                .build();
    }
    public  void update(UserUpdateRequest userUpdateRequest) {
        this.firstName = userUpdateRequest.getFirstName();
        this.lastName = userUpdateRequest.getLastName();
        this.dateOfBirth = userUpdateRequest.getDateOfBirth();
        this.enabled = userUpdateRequest.getEnabled();
        this.deleted = userUpdateRequest.getDeleted();
    }
    public void changePassword(UserRepalcePass userRepalcePass ,PasswordEncoder passwordEncoder) {
        Boolean checkPass = passwordEncoder.matches(userRepalcePass.getOldPassword(), this.passWord);
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        Boolean aBoolean = userRepalcePass.getConfirmPassword().equals(userRepalcePass.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");
        this.setPassWord(passwordEncoder.encode(userRepalcePass.getNewPassword()));

    }
    public void enabled() {
        this.enabled = true;
    }
    public void disabled() {
        this.enabled = false;
    }
    public void deleted() {
        this.deleted = true;
    }

}
