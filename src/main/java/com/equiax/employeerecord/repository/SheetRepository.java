package com.equiax.employeerecord.repository;

import com.equiax.employeerecord.domain.Sheet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sheet entity.
 */
public interface SheetRepository extends JpaRepository<Sheet,Long> {

}
