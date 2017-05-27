package com.adriankubala.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Plik.
 */
@Entity
@Table(name = "plik")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @ManyToOne
    private Katalog katalog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Plik nazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Katalog getKatalog() {
        return katalog;
    }

    public Plik katalog(Katalog katalog) {
        this.katalog = katalog;
        return this;
    }

    public void setKatalog(Katalog katalog) {
        this.katalog = katalog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plik plik = (Plik) o;
        if (plik.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, plik.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Plik{" +
            "id=" + id +
            ", nazwa='" + nazwa + "'" +
            '}';
    }
}
