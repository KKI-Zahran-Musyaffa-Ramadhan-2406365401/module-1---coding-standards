package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private List<Product> products;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        this.products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        this.products.add(product);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Zahran Mulya");
    }

    @Test
    void testSaveCreate() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("p1", "Voucher Code", paymentData, order);

        Payment result = paymentRepository.save(payment);

        assertEquals(payment, result);
        Payment saved = paymentRepository.findById("p1");
        assertNotNull(saved);
        assertEquals(payment.getId(), saved.getId());
        assertEquals("Voucher Code", saved.getMethod());
    }

    @Test
    void testSaveUpdate() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment1 = new Payment("p1", "Voucher Code", paymentData, order);
        paymentRepository.save(payment1);

        Payment payment2 = new Payment("p1", "Cash on Delivery", paymentData, order);
        paymentRepository.save(payment2);

        Payment saved = paymentRepository.findById("p1");
        assertEquals("Cash on Delivery", saved.getMethod());
        assertEquals(1, paymentRepository.findAll().size());
    }

    @Test
    void testFindByIdNotFound() {
        Payment saved = paymentRepository.findById("non-existent");
        assertNull(saved);
    }

    @Test
    void testFindAll() {
        Map<String, String> paymentData = new HashMap<>();
        paymentRepository.save(new Payment("p1", "Voucher Code", paymentData, order));
        paymentRepository.save(new Payment("p2", "Cash on Delivery", paymentData, order));

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
    }
}
