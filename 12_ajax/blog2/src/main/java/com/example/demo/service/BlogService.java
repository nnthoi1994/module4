package com.example.demo.service;

import com.example.demo.entity.Blog;
import com.example.demo.repository.IBlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements IBlogService {
    private final IBlogRepository blogRepository;

    public BlogService(IBlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    // Implement method phân trang
    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public void save(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public Blog findById(Integer id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> findByCategoryId(Integer categoryId) {
        return blogRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Blog> findByTitleContaining(String keyword) {
        return blogRepository.findByTitleContaining(keyword);
    }

    @Override
    public Page<Blog> searchByTitleAndCategory(String title, Integer categoryId, Pageable pageable) {
        return blogRepository.searchByTitleAndCategory(title, categoryId, pageable);
    }
}