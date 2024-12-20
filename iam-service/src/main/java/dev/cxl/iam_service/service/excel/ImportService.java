package dev.cxl.iam_service.service.excel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.evo.common.client.storage.StorageClient;

import dev.cxl.iam_service.configuration.SecurityUtils;
import dev.cxl.iam_service.entity.UserInformation;
import dev.cxl.iam_service.enums.UserAction;
import dev.cxl.iam_service.respository.UserInformationRepository;
import dev.cxl.iam_service.service.ActivityService;
import dev.cxl.iam_service.service.UtilUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final UtilUserService utilUserService;
    private final UserInformationRepository userInformationRepository;
    private final StorageClient client;
    private final ActivityService activityService;

    public List<String> importUsers(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        List<UserInformation> users = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            String idFile = client.importFiles(file, SecurityUtils.getAuthenticatedUserID());

            // Đọc dữ liệu từ hàng 2 (bỏ qua tiêu đề)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                UserInformation user = parseRow(row, i + 1, idFile, errors);
                if (user != null) {
                    users.add(user);
                }
            }
        }

        // Chỉ lưu vào database nếu không có lỗi
        if (errors.isEmpty()) {
            userInformationRepository.saveAll(users);
            activityService.createHistoryActivity(SecurityUtils.getAuthenticatedUserID(), UserAction.EXPORT_EXCEL);
        }

        return errors;
    }

    private UserInformation parseRow(Row row, int rowIndex, String fileId, List<String> errors) {
        try {
            String username = getCellString(row, 0, "Username", rowIndex, errors);
            if (username == null || utilUserService.userExists(username)) {
                errors.add("Dòng " + rowIndex + ": Username '" + username + "' đã tồn tại hoặc không hợp lệ.");
                return null;
            }

            UserInformation user = new UserInformation();
            user.setUsername(username);
            user.setFullName(getCellString(row, 1, "Họ tên", rowIndex, errors));
            user.setDateOfBirth(getCellDate(row, 2, "Ngày sinh", rowIndex, errors));
            user.setStreetName(getCellString(row, 3, "Tên đường", rowIndex, errors));
            user.setWard(getCellString(row, 4, "Phường/xã", rowIndex, errors));
            user.setDistrict(getCellString(row, 5, "Quận/huyện", rowIndex, errors));
            user.setProvince(getCellString(row, 6, "Tỉnh/thành", rowIndex, errors));
            user.setYearsOfExperience(getCellInteger(row, 7, "Số năm kinh nghiệm", rowIndex, errors));
            user.setDeleted(false);
            user.setFileID(fileId);

            validateUser(user, errors, rowIndex);

            return user;
        } catch (Exception e) {
            errors.add("Dòng " + rowIndex + " bị lỗi không xác định: " + e.getMessage());
            return null;
        }
    }

    private String getCellString(Row row, int index, String fieldName, int rowIndex, List<String> errors) {
        try {
            if (row.getCell(index) != null && row.getCell(index).getCellType() == CellType.STRING) {
                return row.getCell(index).getStringCellValue().trim();
            }
        } catch (Exception e) {
            errors.add("Dòng " + rowIndex + ": " + fieldName + " không hợp lệ.");
        }
        errors.add("Dòng " + rowIndex + ": " + fieldName + " bị thiếu hoặc sai định dạng.");
        return null;
    }

    private LocalDate getCellDate(Row row, int index, String fieldName, int rowIndex, List<String> errors) {
        try {
            if (row.getCell(index) != null && row.getCell(index).getCellType() == CellType.NUMERIC) {
                return row.getCell(index).getLocalDateTimeCellValue().toLocalDate();
            }
        } catch (Exception e) {
            errors.add("Dòng " + rowIndex + ": " + fieldName + " không hợp lệ.");
        }
        errors.add("Dòng " + rowIndex + ": " + fieldName + " bị thiếu hoặc sai định dạng.");
        return null;
    }

    private Integer getCellInteger(Row row, int index, String fieldName, int rowIndex, List<String> errors) {
        try {
            if (row.getCell(index) != null && row.getCell(index).getCellType() == CellType.NUMERIC) {
                double value = row.getCell(index).getNumericCellValue();
                return (int) value >= 0 ? (int) value : null;
            }
        } catch (Exception e) {
            errors.add("Dòng " + rowIndex + ": " + fieldName + " không hợp lệ.");
        }
        errors.add("Dòng " + rowIndex + ": " + fieldName + " phải là số >= 0.");
        return null;
    }

    private void validateUser(UserInformation user, List<String> errors, int rowIndex) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Username bị trống.");
        }
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Họ tên bị trống.");
        }
        if (user.getDateOfBirth() == null || user.getDateOfBirth().isAfter(LocalDate.now())) {
            errors.add("Dòng " + rowIndex + ": Ngày sinh không hợp lệ.");
        }
        if (user.getStreetName() == null || user.getStreetName().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Tên đường bị trống.");
        }
        if (user.getWard() == null || user.getWard().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Phường/xã bị trống.");
        }
        if (user.getDistrict() == null || user.getDistrict().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Quận/huyện bị trống.");
        }
        if (user.getProvince() == null || user.getProvince().isEmpty()) {
            errors.add("Dòng " + rowIndex + ": Tỉnh/thành bị trống.");
        }
        if (user.getYearsOfExperience() == null || user.getYearsOfExperience() < 0) {
            errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm không hợp lệ.");
        }
    }
}
