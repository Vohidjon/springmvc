package com.springmvc.serviceImpl;

import com.springmvc.components.SimplePager;
import com.springmvc.core.Product;
import com.springmvc.dao.ProductDAO;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.HibernateException;
import com.springmvc.components.EntityManager;

import java.util.List;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
@Repository
public class ProductDaoImpl implements ProductDAO {
    @Override
    public void create(Product p) {
        Session session = EntityManager.getSessionFactory().openSession();
        try {
            session.save(p);
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Product get(Long id) {
        Product product = null;
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "from Product as t1 where t1.id = :id and t1.deleted = false and t1.category.deleted = false";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        try {
            product = (Product) query.uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return product;
    }

    @Override
    public List<Product> list() {
        return this.list(null);
    }

    public List<Product> list(SimplePager pager) {
        List products = null;
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "from Product as t1 where t1.deleted = false and t1.category.deleted = false";
        Query query = session.createQuery(hql);
        if (pager != null) {
            if (pager.getPage() != null && pager.getRows() != null) {
                query.setMaxResults(pager.getRows());
                query.setFirstResult((pager.getPage() - 1) * pager.getRows());
            }
        }
        try {
            products = query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return products;
    }

    @Override
    public Long count() {
        return count(null);
    }

    public Long count(Long categoryId) {
        Session session = EntityManager.getSessionFactory().openSession();
        String hql = "select count(t1.id) from Product as t1 where t1.deleted = false and t1.category.deleted = false";
        if (categoryId != null) {
            hql += " and t1.category.id = :categoryId";
        }
        Query query = session.createQuery(hql);
        query.setParameter("categoryId", categoryId);
        return (Long) session.createQuery(hql).uniqueResult();
    }

    @Override
    public List<Product> listByCategory(Long categoryId) {
        return this.listByCategory(categoryId, null);
    }

    public List<Product> listByCategory(Long categoryId, SimplePager pager) {
        if (categoryId != null) {
            List<Product> products = null;
            Session session = EntityManager.getSessionFactory().openSession();
            String hql = "from Product as t1 where t1.deleted = false and t1.category.deleted = false and t1.category.id = :categoryId";
            Query query = session.createQuery(hql);
            query.setParameter("categoryId", categoryId);
            if (pager != null) {
                if (pager.getPage() != null && pager.getRows() != null) {
                    query.setMaxResults(pager.getPage());
                    query.setFirstResult((pager.getPage() - 1) * pager.getRows());
                }
            }
            try {
                products = query.list();
            } catch (HibernateException ex) {
                ex.printStackTrace();
            } finally {
                session.close();
            }
            return products;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        Session session = EntityManager.getSessionFactory().openSession();
        try {
            Product product = (Product)session.get(Product.class, id);
            product.setDeleted(true);
            session.update(product);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Product p) {
        Session session = EntityManager.getSessionFactory().openSession();
        session.update(p);
        session.close();
    }
}