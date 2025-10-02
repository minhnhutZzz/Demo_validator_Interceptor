package vn.iotstar.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPasswd(String email, String passwd);
}