package vn.iotstar.model;


import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoModel {
    private String videoId;

    @NotNull(message = "Active status is required")
    private boolean active;

    @NotBlank(message = "Description is required")
    @Size(max = 4000, message = "Description must not exceed 4000 characters")
    private String description;

    @NotNull(message = "Image file is required")
    private MultipartFile imageFile;

    private String poster;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotNull(message = "Views must be provided")
    private int views;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Boolean isEdit = false;

    // Thêm userId để liên kết với User (admin hoặc user tạo video)
    @NotNull(message = "User ID is required")
    private Long userId;
}