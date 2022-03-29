package com.infosys.service;

import com.infosys.model.User;
import com.infosys.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericModelService<User, Integer, UserRepository> {

}
