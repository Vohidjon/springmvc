package com.springmvc.serviceImpl;

import com.springmvc.components.SimplePager;
import com.springmvc.components.EntityManager;
import com.springmvc.core.ProductCategory;
import com.springmvc.dao.ProductCategoryDAO;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
@Repository
public class ProductCategoryDaoImpl implements ProductCategoryDAO {
    @Override
    public void create(ProductCategory pc) {
        Session session = EntityManager.getSessionFactory().openSession();
        try {
            session.save(pc);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public ProductCategory get(Long id) {
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "from ProductCategory where id = :id and deleted = false";
        ProductCategory productCategory = null;
        try {
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            productCategory = (ProductCategory) query.uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return productCategory;
    }

    @Override
    public List<ProductCategory> list() {
        return this.list(null);
    }

    public List<ProductCategory> list(SimplePager pager) {
        List<ProductCategory> productCategories = null;
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "from ProductCategory where deleted = false";
        Query query = session.createQuery(hql);
        if (pager != null) {
            if (pager.getPage() != null && pager.getRows() != null) {
                query.setMaxResults(pager.getRows());
                query.setFirstResult((pager.getPage() - 1) * pager.getRows());
            }
        }
        try {
            productCategories = query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return productCategories;
    }

    @Override
    public Long count() {
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "select count(id) from ProductCategory where deleted = false";
        return (Long)session.createQuery(hql).uniqueResult();
    }

    @Override
    public void delete(Long id) {
        Session session = EntityManager.getSessionFactory().openSession();
        ProductCategory productCategory = (ProductCategory) session.get(ProductCategory.class, id);
        productCategory.setDeleted(true);
        try {
            session.update(productCategory);
        } catch (HibernateException ex){
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void update(ProductCategory pc) {
        Session session = EntityManager.getSessionFactory().openSession();
        session.update(pc);
        session.close();
    }
}
