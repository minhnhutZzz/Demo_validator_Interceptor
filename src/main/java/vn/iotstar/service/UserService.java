package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface UserService {

	User findByEmailAndPasswd(String email, String passwd);

}
