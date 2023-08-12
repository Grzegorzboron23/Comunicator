package com.Comunicator.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "number", unique = true)
    private int number;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "password")
    private String password;

    public User(String name, String email, int number, String password) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.role =Role.User;
    }


    @PrePersist
    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }
}
