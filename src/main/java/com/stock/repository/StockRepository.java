package com.stock.repository;

import java.util.List;
import java.util.Optional;

import com.stock.request.StockResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stock.bean.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{

	public Stock findByName(String name);
	
	@Query("SELECT stk FROM Stock stk WHERE  LOWER(stk.name) LIKE LOWER(CONCAT('%', :namePattern, '%'))")
	public Optional<List<Stock>> findStockByName(@Param("namePattern") String namePattern);

    @Query("""
            select distinct stock from Stock stock
            where (:#{#stockResource.name} is null or lower(stock.name) like lower(concat('%', :#{#stockResource.name}, '%')))
              and (:#{#stockResource.brand} is null or lower(stock.brand) like lower(concat('%', :#{#stockResource.brand}, '%')))
           """)
    List<Stock> getStockDetails(@Param("stockResource") StockResource stockResource);
}
