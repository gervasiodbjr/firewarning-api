package com.gdbjr.idealit.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EMPRESA")
public class Empresa {

    @Id
    @Column(name = "CNPJ", nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "FANTASIA", nullable = false, length = 200)
    private String fantasia;

    @Column(name = "CONTATO", length = 20)
    private String contato;

    @Column(name = "ENABLED", length = 1)
    private String enabled = "S";

    @ManyToMany( cascade = { CascadeType.MERGE, CascadeType.PERSIST} )
    @JoinTable(
            name = "EMPRESA_INCIDENTE",
            joinColumns = @JoinColumn( name = "CNPJ", referencedColumnName = "CNPJ" ),
            inverseJoinColumns = @JoinColumn( name = "INCIDENTE_ID", referencedColumnName = "ID") )
    private Collection<Incidente> incidentes;

    @OneToMany
    private List<User> users;

    public Empresa() {
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Collection<Incidente> getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(Collection<Incidente> incidentes) {
        this.incidentes = incidentes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
