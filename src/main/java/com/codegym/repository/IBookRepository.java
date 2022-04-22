package com.codegym.repository;

import com.codegym.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
    @Query(value = "select * from book where category_id = ?1", nativeQuery = true)
    Page<Book> findByCategory(Long id, Pageable pageable);

    Page<Book> findByNameContaining(String name, Pageable pageable);
}
