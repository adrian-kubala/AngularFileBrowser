package com.adriankubala.repository;

import com.adriankubala.domain.Plik;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Plik entity.
 */
@SuppressWarnings("unused")
public interface PlikRepository extends JpaRepository<Plik,Long> {

}
