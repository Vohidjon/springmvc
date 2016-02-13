package com.springmvc.dao;

import com.springmvc.core.Product;

import java.util.List;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
public interface ProductDAO  extends BaseDAO<Product>{
    public List<Product> listByCategory(Long categoryId);
}
