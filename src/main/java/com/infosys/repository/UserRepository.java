package com.infosys.repository;

import com.infosys.model.User;
import com.infosys.model.projection.LoginView;
import com.infosys.model.projection.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String username);

    LoginView findLoginViewByName(String username);

//    @Query(value = "UPDATE test.users SET temp_auth_token = NULL WHERE id = ?1", nativeQuery = true)
//    void deleteTempAuthToken(Integer userid);
//
//    @Query(value = "UPDATE test.users SET temp_auth_token = ?2 WHERE id = ?1", nativeQuery = true)
//    void setTempAuthToken(Integer userid, String newTempAuthToken);
}
