package com.adriankubala.repository;

import com.adriankubala.domain.Katalog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Katalog entity.
 */
@SuppressWarnings("unused")
public interface KatalogRepository extends JpaRepository<Katalog,Long> {

}
