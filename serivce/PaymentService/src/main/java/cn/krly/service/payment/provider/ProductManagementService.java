package cn.krly.service.payment.provider;

import cn.krly.platform.api.payment.IProductManagement;
import cn.krly.platform.api.payment.pojo.Product;
import cn.krly.service.payment.mapper.ProductMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(provider = "ProductServiceProviderConfig")
public class ProductManagementService implements IProductManagement {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product createProduct(int projectId, String name, int price, String description) {
        return null;
    }

    @Override
    public Product getProduct(int productId) {
        return null;
    }

    @Override
    public int deleteProduct(int productId) {
        return 0;
    }

    @Override
    public int updateProduct(Product product) {
        return 0;
    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getProductsOfProject(int projectId) {
        return null;
    }
}
