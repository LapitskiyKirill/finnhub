package com.gmail.kirilllapitsky.mailing.repository;

import com.gmail.kirilllapitsky.mailing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
