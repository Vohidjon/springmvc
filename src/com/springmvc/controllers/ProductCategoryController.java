package com.springmvc.controllers;

import com.springmvc.components.SimplePager;
import com.springmvc.components.Util;
import com.springmvc.core.Product;
import com.springmvc.core.ProductCategory;
import com.springmvc.serviceImpl.ProductCategoryDaoImpl;
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
import java.util.*;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
@Controller()
@RequestMapping("/product-categories")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryDaoImpl productCategoryDao;

    @Autowired
    private Util util;
    /**
     * Takes the user to entry page of product category management
     * @return String
     */
    @RequestMapping(value = {"/entry"}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String entry() {
        return "/productCategories/entry";
    }

    // CRUD operations on product entity

    /**
     * Creates a new record in product category table
     * @return String
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody
    String create(HttpServletRequest request, HttpServletResponse response) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(request.getParameter("name"));
        productCategory.setDescription(request.getParameter("description"));
        productCategoryDao.create(productCategory);
        return "success";
    }

    /**
     * Returns single product category specified by id.
     * @param id Long
     * @return String
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String read(@PathVariable Long id) {
        return util.toGson(productCategoryDao.get(id));
    }

    /**
     * Returns the list of active product categories
     * @return String
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String read(HttpServletRequest request, HttpServletResponse response) {
//        String pageStr = request.getParameter("page");
//        String rowsStr = request.getParameter("rows");
/*
        if (pageStr != null && rowsStr != null){
            util.getPager().setPage(Integer.parseInt(pageStr));
            util.getPager().setRows(Integer.parseInt(rowsStr));
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("total", productCategoryDao.count());
            map.put("rows", productCategoryDao.list(util.getPager()));
            return util.toGson(map);
        }
*/
        // test
        // getting session factory
        SessionFactory factory = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex){
            System.out.println("Failed to create sessionFactory object: " + ex);
            throw new ExceptionInInitializerError();
        }
        // getting a particular session
        Session session = factory.openSession();
        String result = null;
        Transaction tx = null;
        List categories = null;
        try {
            tx = session.beginTransaction();
            categories = session.createQuery("from ProductCategory").list();
            result = util.toGson(categories);
            tx.commit();
        } catch (HibernateException e){
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        // test
        return result;
    }

    /**
     * Updates single product category specified by id
     * @return String
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody String update(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        ProductCategory productCategory = productCategoryDao.get(id);
        if(productCategory == null){
            return "error";
        }
        productCategory.setName(request.getParameter("name"));
        productCategory.setDescription(request.getParameter("description"));
//        productCategoryDao.update(id, productCategory);
        return "success";
    }
    /**
     * Sets the deleted field of the product category (specified by id) to true
     * @param id Long
     * @return String
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Long id){
        productCategoryDao.delete(id);
        return "success";
    }
}