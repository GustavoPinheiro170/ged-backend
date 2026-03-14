package br.com.ged.domains;

import br.com.ged.domains.enums.Profiles;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private List<Profiles> profile;


    public User() {
    }
    public User(String username, String password, Profiles profile) {
        this.username = username;
        this.password = password;
        this.profile = Collections.singletonList(profile);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Profiles> getProfile() {
        return profile;
    }

    public void setProfile(List<Profiles> profile) {
        this.profile = profile;
    }
}
