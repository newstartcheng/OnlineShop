package com.itheima.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Orders;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.utils.DataSourceUtils;

public class ProductService {
    
    //  获取热门商品列表
    public List<Product> findHotProductList() {
        ProductDao dao = new ProductDao();
        List<Product> hotProductList = null;
        try {
            hotProductList = dao.findHotProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotProductList;
    }
    //  获取最新商品列表
    public List<Product> findNewProductList() {
        ProductDao dao = new ProductDao();
        List<Product> newProductList = null;
        try {
            newProductList = dao.findNewProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProductList;
    }
    
    //  按cid查询每页商品
    public PageBean<Product> findPoductListByCid(String cid, int currentPage, int currentCount) {
        
        
        //  封装pageBean
        PageBean<Product> pageBean = new PageBean<Product>();
        //  1.封装当前页
        pageBean.setCurrentPage(currentPage);
        //  2.封装每页显示的条目数
        pageBean.setCurrentCount(currentCount);
        //  3.封装总条数
        ProductDao dao = new ProductDao();
        int totalCount = 0;
        try {
            totalCount = dao.getCount(cid);
            pageBean.setTotalCount(totalCount);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        //  4.封装总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
        pageBean.setTotalPage(totalPage);
        //  5.封装当前页显示的数据
        //  当前页的index
        int index = (currentPage - 1) * currentCount;
        List<Product> list = null;
        try {
            list = dao.findPoductListByCid(cid, index, currentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setList(list);
        return pageBean;
    }

    //  根据pid获取商品信息
    public Product getProductInfoByPid(String pid) {
        ProductDao dao = new ProductDao();
        Product product = null;
        try {
            product = dao.getProductInfoByPid(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    
    //  提交订单，将订单和订单项的数据存储到数据库
    public void submitOrders(Orders orders) throws SQLException {
        ProductDao dao = new ProductDao();
        System.out.println("in database front");
        //  开启事物
        DataSourceUtils.startTransaction();

        System.out.println("orders;" + orders.toString());
        dao.submitOrders(orders);
        dao.submitOrderItem(orders);
        DataSourceUtils.commitAndRelease();

        System.out.println("in database back");
    }

}
