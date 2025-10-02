package vn.iotstar.service;

import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public interface VideoService {

	List<Video> findByCategory(Category category);

	Page<Video> findByTitleContaining(String title, Pageable pageable);

	List<Video> findByTitleContaining(String title);

	void deleteById(String videoId);

	void save(Video video);

	Optional<Video> findById(String videoId);

	List<Video> findAll();

	Page<Video> findAll(Pageable pageable);

	long count();

	List<Video> findByUser(User user);

}
