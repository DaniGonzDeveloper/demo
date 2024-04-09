package com.stibodx.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "authorities")
@Data
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role")
    private String role;

    @ManyToMany
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "authorities_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    //Throw an exception when doing user.toString due to lazy relationship between authorities and users
    @Override
    public String toString() {
        return "Authorities{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
