package dev.cxl.iam_service.domain.domainentity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.command.UserCreationCommand;
import dev.cxl.iam_service.domain.command.UserReplacePassCommand;
import dev.cxl.iam_service.domain.command.UserUpdateCommand;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
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

    public User(UserCreationCommand userCreationCommand, Supplier<String> userKCLSupplier, List<String> rolesExits) {
        this.userID = UUID.randomUUID().toString();
        this.userKCLID = userKCLSupplier.get();
        this.userName = userCreationCommand.getUserName();
        this.userMail = userCreationCommand.getUserMail();
        this.passWord = userCreationCommand.getPassWord();
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
                UserRole userRole = new UserRole(this.userID, roleId);
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

    public void changePassword(UserReplacePassCommand userReplacePassCommand) {
        boolean checkPass = userReplacePassCommand.getOldPassword().equals(this.passWord);
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        boolean aBoolean = userReplacePassCommand.getConfirmPassword().equals(userReplacePassCommand.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");
        this.passWord = userReplacePassCommand.getNewPassword();
    }

    public void addUserRole(List<String> roleIds) {
        roleIds.forEach(roleId -> {
            UserRole userRole = new UserRole(this.userID, roleId);
            this.userRoles.add(userRole);
        });
    }

    public void deleteUserRole(List<String> roleIds) {
        roleIds.forEach(roleId -> {
            userRoles.removeIf(userRole -> userRole.getRoleID().equals(roleId));
        });
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

    public void setuserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
