package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId("test-id-123");
        testProduct.setProductName("Test Product");
        testProduct.setProductQuantity(10);
    }

    @Test
    void testUpdateProductSuccess() {

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Product Name");
        updatedProduct.setProductQuantity(20);

        Product expectedProduct = new Product();
        expectedProduct.setProductId("test-id-123");
        expectedProduct.setProductName("Updated Product Name");
        expectedProduct.setProductQuantity(20);

        when(productRepository.update(eq("test-id-123"), any(Product.class)))
                .thenReturn(expectedProduct);

        Product result = productService.update("test-id-123", updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals("test-id-123", result.getProductId());
        assertEquals("Updated Product Name", result.getProductName());
        assertEquals(20, result.getProductQuantity());
        verify(productRepository, times(1)).update(eq("test-id-123"), any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Non-existent Product");
        updatedProduct.setProductQuantity(5);

        when(productRepository.update(eq("non-existent-id"), any(Product.class)))
                .thenReturn(null);

        Product result = productService.update("non-existent-id", updatedProduct);

        assertNull(result);
        verify(productRepository, times(1)).update(eq("non-existent-id"), any(Product.class));
    }

    @Test
    void testUpdateProductWithInvalidQuantity() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Product with Invalid Quantity");
        updatedProduct.setProductQuantity(-10);

        Product expectedProduct = new Product();
        expectedProduct.setProductId("test-id-123");
        expectedProduct.setProductName("Product with Invalid Quantity");
        expectedProduct.setProductQuantity(-10);

        when(productRepository.update(eq("test-id-123"), any(Product.class)))
                .thenReturn(expectedProduct);

        Product result = productService.update("test-id-123", updatedProduct);

        assertNotNull(result);
        assertTrue(result.getProductQuantity() < 0, "Quantity should be negative to demonstrate invalid input");
        verify(productRepository, times(1)).update(eq("test-id-123"), any(Product.class));
    }

    @Test
    void testUpdateProductWithEmptyName() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("");
        updatedProduct.setProductQuantity(20);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.update("test-id-123", updatedProduct);
        });

        assertEquals("Product name cannot be empty", exception.getMessage());
    }

    @Test
    void testCreateProductWithEmptyName() {
        Product newProduct = new Product();
        newProduct.setProductName("");
        newProduct.setProductQuantity(15);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.create(newProduct);
        });

        assertEquals("Product name cannot be empty", exception.getMessage());
    }

    @Test
    void testDeleteProductSuccess() {
        doNothing().when(productRepository).delete("test-id-123");
        productService.delete("test-id-123");

        verify(productRepository, times(1)).delete("test-id-123");
    }

    @Test
    void testDeleteProductNotFound() {
        String nonExistentId = "non-existent-id";
        doNothing().when(productRepository).delete(nonExistentId);

        assertDoesNotThrow(() -> productService.delete(nonExistentId));
        verify(productRepository, times(1)).delete(nonExistentId);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setProductName("New Product");
        newProduct.setProductQuantity(15);

        when(productRepository.create(any(Product.class))).thenReturn(newProduct);

        Product result = productService.create(newProduct);

        assertNotNull(result);
        assertEquals("New Product", result.getProductName());
        assertEquals(15, result.getProductQuantity());
        verify(productRepository, times(1)).create(any(Product.class));
    }

    @Test
    void testFindAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(testProduct);

        Product product2 = new Product();
        product2.setProductId("test-id-456");
        product2.setProductName("Another Product");
        product2.setProductQuantity(25);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList.iterator());

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
        assertEquals("Another Product", result.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById("test-id-123")).thenReturn(testProduct);

        Product result = productService.findById("test-id-123");

        assertNotNull(result);
        assertEquals("test-id-123", result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(10, result.getProductQuantity());
        verify(productRepository, times(1)).findById("test-id-123");
    }
}
