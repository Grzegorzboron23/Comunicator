package com.Comunicator.respository;


import com.Comunicator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getById(Long id);

    <S extends User> List<S> saveAll(Iterable<S> users);

    List<User> findAll();

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);

    User findByName(String name);

    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%")
    List<User> findByUsernameContaining(String filter);



}
