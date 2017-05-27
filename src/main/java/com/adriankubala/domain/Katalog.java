package com.adriankubala.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Katalog.
 */
@Entity
@Table(name = "katalog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Katalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @Column(name = "nadrzedny")
    private String nadrzedny;

    @OneToMany(mappedBy = "katalog")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Plik> pliks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Katalog nazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNadrzedny() {
        return nadrzedny;
    }

    public Katalog nadrzedny(String nadrzedny) {
        this.nadrzedny = nadrzedny;
        return this;
    }

    public void setNadrzedny(String nadrzedny) {
        this.nadrzedny = nadrzedny;
    }

    public Set<Plik> getPliks() {
        return pliks;
    }

    public Katalog pliks(Set<Plik> pliks) {
        this.pliks = pliks;
        return this;
    }

    public Katalog addPlik(Plik plik) {
        this.pliks.add(plik);
        plik.setKatalog(this);
        return this;
    }

    public Katalog removePlik(Plik plik) {
        this.pliks.remove(plik);
        plik.setKatalog(null);
        return this;
    }

    public void setPliks(Set<Plik> pliks) {
        this.pliks = pliks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Katalog katalog = (Katalog) o;
        if (katalog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, katalog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Katalog{" +
            "id=" + id +
            ", nazwa='" + nazwa + "'" +
            ", nadrzedny='" + nadrzedny + "'" +
            '}';
    }
}
