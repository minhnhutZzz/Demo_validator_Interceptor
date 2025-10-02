package vn.iotstar.controller;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.VideoService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    
    @Autowired
    VideoService videoService;
    
    

    @GetMapping("")
    public String list(ModelMap model) {
        List<Category> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";
    }

    @GetMapping("add")
    public String add(ModelMap model) {
        CategoryModel cate = new CategoryModel();
        cate.setEdit(false);
        model.addAttribute("category", cate);
        return "admin/categories/addOrEdit";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(
            ModelMap model,// đối tượng để thêm thuộc tính truyền sang view
            @Valid @ModelAttribute("category") CategoryModel cate,// thêm thuộc tính từ form vào Model
            BindingResult result,//Lưu kết quả validation và hiện lỗi
            HttpSession session) {// lưu thông tin vào session 
        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }

        // copy dữ liệu từ Model và Entity
        Category entity = new Category();
        BeanUtils.copyProperties(cate, entity);

        // Gán User từ session (chỉ admin được phép)
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            entity.setUser(currentUser);
        } else {
            model.addAttribute("message", "Unauthorized access");
            return new ModelAndView("redirect:/admin/categories");
        }

        //lưu entity vào database 	
        categoryService.save(entity);
        String message = cate.isEdit() ? "Category đã được cập nhật" : "Category đã được thêm thành công";
        model.addAttribute("message", message);
        return new ModelAndView("forward:/admin/categories", model);
    }

    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<Category> opt = categoryService.findById(categoryId);
        CategoryModel cate = new CategoryModel();
        if (opt.isPresent()) {
            Category entity = opt.get();
            BeanUtils.copyProperties(entity, cate);
            cate.setEdit(true);
            model.addAttribute("category", cate);
            return new ModelAndView("admin/categories/addOrEdit", model);
        }
        model.addAttribute("message", "Category không tồn tại");
        return new ModelAndView("redirect:/admin/categories", model);
    }

    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category đã xóa thành công");
        return new ModelAndView("redirect:/admin/categories", model);
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<Category> list = null;
        if (StringUtils.hasText(name)) {
            list = categoryService.findByCategorynameContaining(name);
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("categories", list);
        return "admin/categories/search";
    }

    @RequestMapping("searchpagenated")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryId"));
        Page<Category> resultPage = null;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategorynameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > count) {
                if (end == totalPages) {
                    start = end - count;
                }
            } else if (start == 1) {
                end = start + count;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("categoryPage", resultPage);
        return "admin/categories/searchpagenated";
    }
    
    @GetMapping("/{categoryId}/videos")
    public String viewVideos(@PathVariable("categoryId") Long categoryId, ModelMap model) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            List<Video> videos = videoService.findByCategory(category);  // Giả sử VideoService có phương thức này
            model.addAttribute("category", category);
            model.addAttribute("videos", videos);
            return "admin/videos/list_Video"; 
        }
        model.addAttribute("message", "Category không tồn tại");
        return "redirect:/admin/categories";
    }
}