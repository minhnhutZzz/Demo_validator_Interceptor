package vn.iotstar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.reponsitory.UserRepository;
import vn.iotstar.service.UserService;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    
    @Override
	public User findByEmailAndPasswd(String email, String passwd) {
        return userRepository.findByEmailAndPasswd(email, passwd);
    }
}