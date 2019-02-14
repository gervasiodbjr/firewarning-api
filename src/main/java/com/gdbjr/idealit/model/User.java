package com.gdbjr.idealit.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "MAIL", nullable = false, unique = true, length = 100)
    private String mail;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "KEY", nullable = false, length = 255)
    private String key;

    @Column(name = "ENABLED", nullable = false, length = 1)
    private String enabled = "S";

    @ManyToOne
    @JoinColumn(name = "EMPRESA")
    private Empresa empresa;

    @ManyToMany( fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST} )
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn( name = "USER_ID", referencedColumnName = "ID" ),
            inverseJoinColumns = @JoinColumn( name = "ROLE_ID", referencedColumnName = "ID") )
    private Collection<Role> roles;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String token) {
        this.key = token;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
