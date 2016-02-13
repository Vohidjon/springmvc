package com.springmvc.dao;

import java.util.List;

/**
 * Created by vohidjon-linux on 1/10/16.
 */
public interface BaseDAO<E> {
    /**
     * This is the method to be used to create
     * a record in the E table.
     */
    public void create(E e);
    /**
     * This is the method to be used to list down
     * a record from the E table corresponding
     * to a passed E id.
     */
    public E get(Long id);
    /**
     * This is the method to be used to list down
     * all the records from the E table.
     */
    public List<E> list();
    /**
     * This is the method to be used to get
     * count of all E records
     */
    public Long count();
    /**
     * This is the method to be used to delete
     * a record from the E table corresponding
     * to a passed E id.
     */
    public void delete(Long id);
    /**
     * This is the method to be used to update
     * a record into the E table.
     */
    public void update(E e);
}
