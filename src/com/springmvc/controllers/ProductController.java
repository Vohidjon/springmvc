package com.springmvc.controllers;

import com.springmvc.components.SimplePager;
import com.springmvc.components.Util;
import com.springmvc.core.Product;
import com.springmvc.core.ProductCategory;
import com.springmvc.serviceImpl.ProductDaoImpl;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDaoImpl productDao;

    @Autowired
    private Util util;

    /**
     * Takes the user to entry page of product management
     * @return String
     */

    @RequestMapping(value = {"/entry"}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String entry() {
        return "/products/entry";
    }

    // CRUD operations on product entity

    /**
     * Creates a new record in product table
     * @return String
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody String create(HttpServletRequest request, HttpServletResponse response) {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        ProductCategory category = new ProductCategory();
        category.setId(Long.parseLong(request.getParameter("category")));
        product.setCategory(category);
        product.setDescription(request.getParameter("description"));
        productDao.create(product);
        return "success";
    }

    /**
     * Returns single product specified by id.
     * @param id Long
     * @return String
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String read(@PathVariable Long id) {
        return util.toGson(productDao.get(id));
    }

    /**
     * Returns the list of active products
     * @return String
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String read(HttpServletRequest request, HttpServletResponse response) {
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer rows = Integer.parseInt(request.getParameter("rows"));
        util.getPager().setPage(page);
        util.getPager().setRows(rows);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("total", productDao.count());
        // test
        // getting session factory
        Configuration configuration = new Configuration();
        SessionFactory factory = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex){
            System.out.println("Failed to create sessionFactory object: " + ex);
            throw new ExceptionInInitializerError();
        }
        // getting a particular session
        /*Session session = factory.openSession();
        Transaction tx = null;
        List products = null;
        try {
            tx = session.beginTransaction();
            products = session.createQuery("from Product").list();
            tx.commit();
        } catch (HibernateException e){
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }*/
        // test
        map.put("rows", productDao.list(util.getPager()));
        return util.toGson(map);
    }

    /**
     * Returns the list of active products by given category
     * @return String
     */
    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String readByCategory(@PathVariable Long categoryId) {
        return util.toGson(productDao.listByCategory(categoryId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody String update(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        Product product = productDao.get(id);
        if(product == null){
            return "error";
        }
        product.setName(request.getParameter("name"));
        ProductCategory category = new ProductCategory();
        category.setId(Long.parseLong(request.getParameter("category")));
        product.setCategory(category);
        product.setDescription(request.getParameter("description"));
        return "success";
    }
    /**
     * Sets the deleted field of the product (specified by id) to true
     * @param id Long
     * @return String
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Long id){
        if (productDao.get(id) == null){
            return "error";
        }
        productDao.delete(id);
        return "success";
    }
}