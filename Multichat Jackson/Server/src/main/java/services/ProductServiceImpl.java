package services;

import context.Component;
import models.Product;
import models.ShopList;
import protocol.Request;
import repositories.ProductRepository;

import java.util.List;

public class ProductServiceImpl implements ProductService, Component {
    private ProductRepository productRepository;

    public ShopList getProducts() {
        List<Product> products = productRepository.findAll();
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
        return "productServiceImpl";
    }

}
