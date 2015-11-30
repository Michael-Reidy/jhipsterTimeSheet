package com.crowhill.employeerecord.repository;

import com.crowhill.employeerecord.domain.LineItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LineItem entity.
 */
public interface LineItemRepository extends JpaRepository<LineItem,Long> {

}
