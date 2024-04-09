package com.stibodx.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Table(name = "users")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "dni")
    private String dni;
    @Column(name = "street")
    private String street;
    @Column(name = "sign_up_date")
    private LocalDate signUpDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities", inverseJoinColumns = @JoinColumn(name = "authorities_id"), joinColumns = @JoinColumn(name = "user_id"))
    private List<Authorities> authorities =  new ArrayList<>();

}
