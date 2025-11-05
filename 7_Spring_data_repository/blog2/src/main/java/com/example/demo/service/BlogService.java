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

    @Override
    public Blog findById(int id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Blog blog) {
         blogRepository.save(blog);
    }

    @Override
    public void deleteById(int id) {
            blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> findByTitleContaining(String keyword) {
        return blogRepository.findByTitleContaining(keyword);
    }

    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> findAllByNameContaining(String searchName, Pageable pageable) {
        return blogRepository.findAllByNameContaining(searchName, pageable);
    }
}
