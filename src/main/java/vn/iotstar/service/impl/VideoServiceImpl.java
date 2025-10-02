package vn.iotstar.service.impl;



import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.reponsitory.VideoRepository;
import vn.iotstar.service.VideoService;


@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

   
    @Override
	public List<Video> findAll() {
        return videoRepository.findAll();
    }

 
    @Override
	public Optional<Video> findById(String videoId) {
        return videoRepository.findById(videoId);
    }

   
    @Override
	public void save(Video video) {
        videoRepository.save(video);
    }

   
    @Override
	public void deleteById(String videoId) {
        videoRepository.deleteById(videoId);
    }

  
    @Override
	public List<Video> findByTitleContaining(String title) {
        return videoRepository.findByTitleContaining(title);
    }


    @Override
	public Page<Video> findByTitleContaining(String title, Pageable pageable) {
        return videoRepository.findByTitleContaining(title, pageable);
    }

  
    @Override
	public List<Video> findByCategory(Category category) {
        return videoRepository.findByCategory(category);
    }
    
 // Thêm hai phương thức này
   
    @Override
	public long count() {
        return videoRepository.count();  // Gọi count() từ JpaRepository
    }

   
    @Override
	public Page<Video> findAll(Pageable pageable) {
        return videoRepository.findAll(pageable);  // Gọi findAll(Pageable) từ JpaRepository
    }
    
 
   
    @Override
	public List<Video> findByUser(User user) {
        return videoRepository.findByUser(user);  // Giả sử repository có phương thức này
    }
}