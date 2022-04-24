package com.infosys.repository;

import com.infosys.model.User;
import com.infosys.model.projection.LoginView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String username);

    LoginView findLoginViewByName(String username);
}
