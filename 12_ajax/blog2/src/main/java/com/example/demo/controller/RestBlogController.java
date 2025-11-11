package com.example.demo.controller;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Category;
import com.example.demo.service.IBlogService;
import com.example.demo.service.ICategoryService;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/blog")

public class RestBlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private IBlogService iBlogService;
    @Autowired
    private ICategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<Blog>> findAllBlog() {
        List<Blog> blogList = blogService.findAll();
        if (blogList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blogList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getById(@PathVariable("id") int id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 ( thành công không trả về dữ lieu;
        }
        return new ResponseEntity<>(blog, HttpStatus.OK); // 200
    }

    // thêm mới
    @PostMapping
    public ResponseEntity<Blog> saveBlog(@RequestBody Blog blog) {
        blogService.save(blog);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Blog> deleteById(@PathVariable("id") int id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 ( thành công không trả về dữ lieu;
        }
        // gọi method xoá để xoá
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Blog> update(@PathVariable("id")int id,
                                       @RequestBody Blog blog) {
        Blog blogs = blogService.findById(id);
        if (blogs ==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        blogService.save(blog);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
