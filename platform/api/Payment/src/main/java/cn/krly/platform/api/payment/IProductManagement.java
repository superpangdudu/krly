package cn.krly.platform.api.payment;

import cn.krly.platform.api.payment.pojo.Product;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */
public interface IProductManagement {
    Product createProduct(int projectId, String name, int price, String description);
    Product getProduct(int productId);
    int deleteProduct(int productId);
    int updateProduct(Product product);

    List<Product> getAll();
    List<Product> getProductsOfProject(int projectId);
}
