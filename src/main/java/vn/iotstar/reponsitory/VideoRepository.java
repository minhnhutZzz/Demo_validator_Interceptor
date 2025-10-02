package vn.iotstar.reponsitory;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
    List<Video> findByTitleContaining(String title);
    Page<Video> findByTitleContaining(String title, Pageable pageable);
    List<Video> findByCategory(Category category);  
    List<Video> findByUser(User user);
}