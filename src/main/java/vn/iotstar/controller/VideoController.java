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
import vn.iotstar.model.VideoModel;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.VideoService;

@Controller
@RequestMapping("admin/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String list(ModelMap model) {	
        List<Video> list = videoService.findAll();
        model.addAttribute("videos", list);
        return "admin/videos/list_Video";  // View: admin/videos/list.html
    }

    @GetMapping("add")
    public String add(ModelMap model) {
        VideoModel video = new VideoModel();
        video.setIsEdit(false);
        model.addAttribute("video", video);
        model.addAttribute("categories", categoryService.findAll());  // Để chọn category trong form
        return "admin/videos/addOrEdit_Video";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(
            ModelMap model,
            @Valid @ModelAttribute("video") VideoModel videoModel,
            BindingResult result,
            HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return new ModelAndView("admin/videos/addOrEdit_Video");
        }

        Video entity = new Video();
        BeanUtils.copyProperties(videoModel, entity);

        // Gán User từ session (chỉ admin)
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            entity.setUser(currentUser);
        } else {
            model.addAttribute("message", "Unauthorized access");
            return new ModelAndView("redirect:/admin/videos");
        }

        // Gán Category nếu có
        if (videoModel.getCategoryId() != null) {
            Optional<Category> optCategory = categoryService.findById(videoModel.getCategoryId());
            optCategory.ifPresent(entity::setCategory);
        }

        videoService.save(entity);
        String message = videoModel.getIsEdit() ? "Video đã được cập nhật" : "Video đã được thêm thành công";
        model.addAttribute("message", message);
        return new ModelAndView("forward:/admin/videos", model);
    }

    @GetMapping("edit/{videoId}")
    public ModelAndView edit(ModelMap model, @PathVariable("videoId") String videoId) {
        Optional<Video> opt = videoService.findById(videoId);
        VideoModel videoModel = new VideoModel();
        if (opt.isPresent()) {
            Video entity = opt.get();
            BeanUtils.copyProperties(entity, videoModel);
            videoModel.setIsEdit(true);
            model.addAttribute("video", videoModel);
            model.addAttribute("categories", categoryService.findAll());
            return new ModelAndView("admin/videos/addOrEdit_Video");
        }
        model.addAttribute("message", "Video không tồn tại");
        return new ModelAndView("redirect:/admin/videos");
    }

    @GetMapping("delete/{videoId}")
    public ModelAndView delete(ModelMap model, @PathVariable("videoId") String videoId) {
        videoService.deleteById(videoId);
        model.addAttribute("message", "Video đã xóa thành công");
        return new ModelAndView("redirect:/admin/videos");
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "title", required = false) String title) {
        List<Video> list = null;
        if (StringUtils.hasText(title)) {
            list = videoService.findByTitleContaining(title);
        } else {
            list = videoService.findAll();
        }
        model.addAttribute("videos", list);
        return "admin/videos/search_Video";
    }

    @RequestMapping("searchpagenated")
    public String searchPagenated(ModelMap model, @RequestParam(name = "title", required = false) String title,
            @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int count = (int) videoService.count();  // Giả sử VideoService có count()
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("videoId"));
        Page<Video> resultPage = null;

        if (StringUtils.hasText(title)) {
            resultPage = videoService.findByTitleContaining(title, pageable);
            model.addAttribute("title", title);
        } else {
            resultPage = videoService.findAll(pageable);  // Giả sử có findAll(Pageable)
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("videoPage", resultPage);
        return "admin/videos/searchpagenated_Video";
    }
}