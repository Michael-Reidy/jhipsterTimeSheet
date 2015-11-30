package com.crowhill.employeerecord.repository;

import com.crowhill.employeerecord.domain.Sheet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sheet entity.
 */
public interface SheetRepository extends JpaRepository<Sheet,Long> {

}
