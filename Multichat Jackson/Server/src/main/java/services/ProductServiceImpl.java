package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.Component;
import models.ShopList;
import protocol.Request;
import repositories.ProductRepository;
import repositories.ProductRepositoryImpl;
import models.Payload;
import models.Product;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ProductServiceImpl implements ProductService, Component {
    private ProductRepository productRepository;

    public ShopList getProducts() {
        LinkedList<Product> products = productRepository.findAll();
        ShopList shopList = new ShopList();
        shopList.setProducts(products);
        return shopList;
    }

    public void addProduct(Request request) {
            Product product = new Product();
            product.setName(request.getParameter("name"));
            product.setPrice(Integer.parseInt(request.getParameter("price")));
            productRepository.save(product);

    }

    @Override
    public String getComponentName() {
        return null;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }
}
