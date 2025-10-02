package vn.iotstar.model;


import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

// lớp này giúp tách biệt giữa Entity và Controller để bảo mật dữ liệu
@Data
public class CategoryModel {
    private Long categoryId;
    @NotBlank(message = "Category code is required")
    @Size(max = 50, message = "Category code must not exceed 50 characters")
    private String categorycode;

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String categoryname;

    private String images;
    private boolean status;
    private boolean isEdit; 
    private Long userId;   
}