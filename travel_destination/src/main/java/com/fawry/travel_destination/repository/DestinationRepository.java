package com.fawry.travel_destination.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fawry.travel_destination.model.entities.Destination;

public interface DestinationRepository extends JpaRepository<Destination , Long > 
{
    
    @Query("""
        SELECT d FROM Destination d
        WHERE LOWER(d.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.capital) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.region) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.currency) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Destination> search(@Param("keyword") String keyword, Pageable pageable);
    @Query("""
    SELECT d FROM Destination d
    WHERE d.id IN :ids
      AND (:keyword IS NULL OR LOWER(d.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.capital) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.region) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.currency) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
Page<Destination> findAllByIdInWithKeyword(@Param("ids") List<Long> ids,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);
}
