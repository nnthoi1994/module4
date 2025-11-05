package com.example.demo.repository;

import com.example.demo.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByTitleContaining(String keyword);
    List<Blog> findByCategoryId(Integer categoryId);

    @Query("SELECT b FROM Blog b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:categoryId IS NULL OR b.category.id = :categoryId)")
    Page<Blog> searchByTitleAndCategory(@Param("title") String title,
                                        @Param("categoryId") Integer categoryId,
                                        Pageable pageable);
}
