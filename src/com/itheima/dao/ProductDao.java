package com.itheima.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.domain.OrderItem;
import com.itheima.domain.Orders;
import com.itheima.domain.Product;
import com.itheima.utils.DataSourceUtils;

public class ProductDao {
    //  查询热门商品
    public List<Product> findHotProductList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where is_hot=? limit ?,?";
        List<Product> hotProductList = runner.query(sql, new BeanListHandler<>(Product.class), 1, 0, 9);
        return hotProductList;
    }
    //  查询最新商品
    public List<Product> findNewProductList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product order by pdate desc limit ?,?";
        List<Product> newProductList = runner.query(sql, new BeanListHandler<>(Product.class), 0, 9);
        return newProductList;
    }

    //  按cid查询每页商品
    public List<Product> findPoductListByCid(String cid, int index, int currentCount) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where cid=? limit ?,?";
        List<Product> productList = runner.query(sql, new BeanListHandler<Product>(Product.class), cid, index, currentCount);
        return productList;
    }
    //  根据cid查询总商品总数
    public int getCount(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from product where cid=?";
        Long count = (Long) runner.query(sql, new ScalarHandler(), cid);
        return count.intValue();
    }
    
    //  根据pid获取商品信息
    public Product getProductInfoByPid(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where pid=?";
        Product product = runner.query(sql, new BeanHandler<Product>(Product.class), pid);
        return product;
    }
    
    //  向orders表插入数据
    public void submitOrders(Orders orders) throws SQLException {

        QueryRunner runner = new QueryRunner();
        String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
        Connection conn = DataSourceUtils.getConnection();
        runner.update(conn, sql, orders.getOid(), orders.getOrdertime(), orders.getTotal(),
                orders.getState(), orders.getAddress(), orders.getName(), orders.getTelephone(),
                orders.getUser().getUid());
        
    }
    
    //  向ordersItem表中插入数据
    public void submitOrderItem(Orders orders) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orders values(?,?,?,?,?)";
        Connection conn = DataSourceUtils.getConnection();
        List<OrderItem> orderItems = orders.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            runner.update(sql, conn, orderItem.getItemid(), orderItem.getCount(), orderItem.getSubtotal(),
                    orderItem.getProduct().getPid(), orderItem.getOrders().getOid());
        }
        runner.update(conn, sql, orders.getOid(), orders.getOrdertime(), orders.getTotal()
                ,orders.getState(), orders.getAddress(), orders.getName());
        
    }

}
