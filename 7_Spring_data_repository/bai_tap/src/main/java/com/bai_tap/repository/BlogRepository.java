package com.bai_tap.repository;


import com.bai_tap.entity.Blog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogRepository implements IBlogRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Blog> findAll() {
        TypedQuery<Blog> typedQuery = entityManager.createQuery("from Blog ",Blog.class);
        return typedQuery.getResultList();
    }

    @Override
    public Blog findById(int id) {
        return entityManager.find(Blog.class,id);
    }

    @Transactional
    @Override
    public boolean save(Blog blog) {
        try {
            entityManager.merge(blog);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    @Transactional
    public void deleteById(int id) {
        Blog blog = entityManager.find(Blog.class, id);
        if (blog != null) {
            entityManager.remove(blog);
        }
    }

    @Override
    public List<Blog> searchByName(String keyword) {
        String sql = "SELECT * FROM blog WHERE LOWER(name) LIKE LOWER(:keyword)";
        TypedQuery<Blog> query = (TypedQuery<Blog>) entityManager
                .createNativeQuery(sql, Blog.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
}
