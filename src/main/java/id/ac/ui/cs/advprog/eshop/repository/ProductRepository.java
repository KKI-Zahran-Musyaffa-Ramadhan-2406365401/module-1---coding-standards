package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository

public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(final Product product) {
        product.setProductId(UUID.randomUUID().toString());
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(final String productId) {
        Product foundProduct = null;
        for (final Product product : productData) {
            if (product.getProductId().equals(productId)) {
                foundProduct = product;
                break;
            }
        }
        return foundProduct;
    }

    public Product update(final String productId, final Product updatedProduct) {
        Product result = null;
        for (int i = 0; i < productData.size(); i++) {
            final Product product = productData.get(i);
            if (product.getProductId().equals(productId)) {
                updatedProduct.setProductId(productId);
                productData.set(i, updatedProduct);
                result = updatedProduct;
                break;
            }
        }
        return result;
    }

    public void delete(final String productId) {
        productData.removeIf(product -> product.getProductId().equals(productId));
    }

}
