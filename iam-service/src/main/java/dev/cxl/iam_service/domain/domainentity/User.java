package dev.cxl.iam_service.domain.domainentity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.command.UserCreationCommand;
import dev.cxl.iam_service.domain.command.UserReplacePassCommand;
import dev.cxl.iam_service.domain.command.UserUpdateCommand;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends Auditable {

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
    List<UserRole> userRoles = new ArrayList<>();

    public User(
            UserCreationCommand userCreationCommand,
            PasswordEncoder passwordEncoder,
            Supplier<String> userKCLSupplier,
            List<String> rolesExits) {
        this.userID = UUID.randomUUID().toString();
        this.userKCLID = userKCLSupplier.get();
        this.userName = userCreationCommand.getUserName();
        this.userMail = userCreationCommand.getUserMail();
        this.passWord = passwordEncoder.encode(userCreationCommand.getPassWord());
        this.firstName = userCreationCommand.getFirstName();
        this.lastName = userCreationCommand.getLastName();
        this.dateOfBirth = userCreationCommand.getDateOfBirth();
        this.avatar = userCreationCommand.getAvatar();
        this.enabled = false;
        this.deleted = false;
        this.isRoot = false;
        this.assignUserRoles(rolesExits);
    }

    public void assignUserRoles(List<String> rolesExits) {
        if (rolesExits != null && !rolesExits.isEmpty()) {
            rolesExits.forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserID(this.userID);
                userRole.setRoleID(roleId);
                this.userRoles.add(userRole);
            });
        }
    }

    public void update(UserUpdateCommand userUpdateCommand) {
        this.firstName = userUpdateCommand.getFirstName();
        this.lastName = userUpdateCommand.getLastName();
        this.dateOfBirth = userUpdateCommand.getDateOfBirth();
        this.enabled = userUpdateCommand.getEnabled();
        this.deleted = userUpdateCommand.getDeleted();
    }

    public void changePassword(UserReplacePassCommand userReplacePassCommand, PasswordEncoder passwordEncoder) {
        Boolean checkPass = passwordEncoder.matches(userReplacePassCommand.getOldPassword(), this.passWord);
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        Boolean aBoolean = userReplacePassCommand.getConfirmPassword().equals(userReplacePassCommand.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");
        this.setPassWord(passwordEncoder.encode(userReplacePassCommand.getNewPassword()));
    }

    public void deleteUserRoles(String id) {
        this.userRoles.forEach(userRoleDomain1 -> {
            if (userRoleDomain1.getRoleID().equals(id)) {
                this.userRoles.remove(userRoleDomain1);
            }
        });
    }

    public void addUserRole(UserRole userRoleDomain) {
        this.userRoles.add(userRoleDomain);
    }

    public void createProfile(String profile) {
        this.profile = profile;
    }

    public void enabled() {
        this.enabled = true;
    }

    public void forgotPassword(String newPassword) {
        this.passWord = newPassword;
    }

    public void disabled() {
        this.enabled = false;
    }

    public void deleted() {
        this.deleted = true;
    }
}
