package dev.cxl.iam_service.domain.domainentity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserRepalcePass;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Boolean deleted;
    boolean isRoot;
    List<UserRoleDomain> userRoles=new ArrayList<>();

    public UserDomain(
            UserCreationRequest userCreationRequest,
            PasswordEncoder passwordEncoder,
            Supplier<String> userKCLSupplier,
            List<String> rolesExits) {
        this.userID = UUID.randomUUID().toString();
        this.userKCLID = userKCLSupplier.get();
        this.userName = userCreationRequest.getUserName();
        this.userMail = userCreationRequest.getUserMail();
        this.passWord = passwordEncoder.encode(userCreationRequest.getPassWord());
        this.firstName = userCreationRequest.getFirstName();
        this.lastName = userCreationRequest.getLastName();
        this.dateOfBirth = userCreationRequest.getDateOfBirth();
        this.avatar = userCreationRequest.getAvatar();
        this.enabled = false;
        this.deleted = false;
        this.isRoot = false;
        this.assignUserRoles(rolesExits);
    }

    public void assignUserRoles(List<String> rolesExits) {
        if (rolesExits != null && !rolesExits.isEmpty()) {
            rolesExits.forEach(roleId -> {
                UserRoleDomain userRole = new UserRoleDomain();
                userRole.setUserID(this.userID);
                userRole.setRoleID(roleId);
                this.userRoles.add(userRole);
            });
        }
    }

    public void update(UserUpdateRequest userUpdateRequest) {
        this.firstName = userUpdateRequest.getFirstName();
        this.lastName = userUpdateRequest.getLastName();
        this.dateOfBirth = userUpdateRequest.getDateOfBirth();
        this.enabled = userUpdateRequest.getEnabled();
        this.deleted = userUpdateRequest.getDeleted();
    }

    public void changePassword(UserRepalcePass userRepalcePass, PasswordEncoder passwordEncoder) {
        Boolean checkPass = passwordEncoder.matches(userRepalcePass.getOldPassword(), this.passWord);
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        Boolean aBoolean = userRepalcePass.getConfirmPassword().equals(userRepalcePass.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");
        this.setPassWord(passwordEncoder.encode(userRepalcePass.getNewPassword()));
    }

    public void deleteUserRoles(String id) {
        this.userRoles.forEach(userRoleDomain1 -> {
            if (userRoleDomain1.getRoleID().equals(id)) {
                this.userRoles.remove(userRoleDomain1);
            }
        });
    }

    public void addUserRole(UserRoleDomain userRoleDomain) {
        this.userRoles.add(userRoleDomain);
    }

    public void createProfile(String profile) {
        this.profile = profile;
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
