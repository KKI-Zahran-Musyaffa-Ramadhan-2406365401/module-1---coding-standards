package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, method, paymentData, order);
        
        String status = validatePayment(method, paymentData);
        return setStatus(payment, status);
    }

    private String validatePayment(String method, Map<String, String> paymentData) {
        if (method.equals("Voucher Code")) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && 
                voucherCode.length() == 16 && 
                voucherCode.startsWith("ESHOP") && 
                countNumerics(voucherCode) == 8) {
                return PaymentStatus.SUCCESS.getValue();
            }
        } else if (method.equals("Cash on Delivery")) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address != null && !address.trim().isEmpty() && 
                deliveryFee != null && !deliveryFee.trim().isEmpty()) {
                return PaymentStatus.SUCCESS.getValue();
            }
        }
        return PaymentStatus.REJECTED.getValue();
    }

    private int countNumerics(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
