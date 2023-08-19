package com.Comunicator.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Getter
@Setter
@NoArgsConstructor
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

    @Column(name = "password")
    private String password;

    public User(String name, String email, int number, String password) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
    }


    @PrePersist
    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }
}
