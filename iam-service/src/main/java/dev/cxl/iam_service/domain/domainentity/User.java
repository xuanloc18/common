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
    boolean root;
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
        this.root = false;
        this.assignUserRoles(rolesExits);
    }

    private void assignUserRoles(List<String> rolesExits) {
        if (rolesExits != null && !rolesExits.isEmpty()) {
            rolesExits.forEach(roleId -> {
                UserRole userRole = new UserRole(this.userID, roleId);
                this.userRoles.add(userRole);
            });
        }
    }

    public void update(UserUpdateCommand cmd) {
        if (cmd == null) {
            return;
        }
        if (cmd.getFirstName() != null) {
            this.firstName = cmd.getFirstName();
        }
        if (cmd.getLastName() != null) {
            this.lastName = cmd.getLastName();
        }
        if (cmd.getDateOfBirth() != null) {
            this.dateOfBirth = cmd.getDateOfBirth();
        }
        if (cmd.getEnabled() != null) {
            this.enabled = cmd.getEnabled();
        }
        if (cmd.getDeleted() != null) {
            this.deleted = cmd.getDeleted();
        }
    }

    public void changePassword(UserReplacePassCommand userReplacePassCommand) {
        if (userReplacePassCommand == null) {
            return;
        }
        boolean checkPass = userReplacePassCommand.getOldPassword().equals(this.passWord);
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        boolean aBoolean = userReplacePassCommand.getConfirmPassword().equals(userReplacePassCommand.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");
        this.passWord = userReplacePassCommand.getNewPassword();
    }

    public void replacePassword(String passWord) {
        this.passWord = passWord;
    }

    public void addUserRole(List<String> roleIds) {
        // Lấy danh sách các roleID hiện tại
        List<String> existingRoleIds =
                this.userRoles.stream().map(UserRole::getRoleID).toList();

        // Tìm các roleId cần cập nhật trạng thái delete = false
        this.getUserRoles().stream()
                .filter(userRole -> roleIds.contains(userRole.getRoleID()) && userRole.getDeleted())
                .forEach(UserRole::setDeleted);

        // Tìm các roleId chưa tồn tại và tạo UserRole mới
        List<UserRole> userRolesNew = roleIds.stream()
                .filter(roleId -> !existingRoleIds.contains(roleId)) // Lọc các roleId mới
                .map(roleId -> new UserRole(this.userID, roleId)) // Tạo đối tượng UserRole mới
                .toList();

        // Thêm các UserRole mới vào danh sách hiện tại
        this.userRoles.addAll(userRolesNew);
    }

    public void deleteUserRole(List<String> roleIds) {
        this.userRoles.forEach(userRoles -> {
            if (roleIds.contains(userRoles.getRoleID())) {
                userRoles.delete();
            }
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
