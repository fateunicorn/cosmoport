package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long>, PagingAndSortingRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {
    @Query(value = "SELECT * FROM ship;", nativeQuery = true)
    List<Ship> getAll();
}
