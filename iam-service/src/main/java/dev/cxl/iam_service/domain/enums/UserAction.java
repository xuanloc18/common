package dev.cxl.iam_service.domain.enums;

public enum UserAction {
    CREATE("Tài khoản được tạo"),
    LOGIN("Đăng nhập"),
    LOGOUT("Đăng xuất"),
    CHANGE_PASSWORD("Đổi mật khẩu"),
    UPDATE_PROFILE("Cập nhật thông tin người dùng"),
    RESET_PASSWORD("Khôi phục mật khẩu"),
    DELETE_ACCOUNT("Xóa tài khoản"),
    FAILED_LOGIN("Đăng nhập thất bại"),
    IMPORT_EXCEL("import excel"),
    EXPORT_EXCEL("export excel"),
    PASSWORD_EXPIRED("Mật khẩu hết hạn");

    private final String description;

    // Constructor
    UserAction(String description) {
        this.description = description;
    }

    // Getter
    public String getDescription() {
        return description;
    }
}
