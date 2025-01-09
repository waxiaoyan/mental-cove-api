package com.mental.cove.repository;

import com.mental.cove.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String name);
    User findByOpenId(String openId);
}
