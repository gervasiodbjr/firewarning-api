package com.gdbjr.idealit.repository;

import com.gdbjr.idealit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByMailAndEnabled(String email, String enabled);

    public User findByIdAndEnabled(Long id, String enabled);

    public User findByMailAndPassword(String mail, String password);

    public List<User> findAllByEnabled(String enabled);

}
