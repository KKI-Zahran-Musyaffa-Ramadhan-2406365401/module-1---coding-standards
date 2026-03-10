package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.order = order;
        this.status = PaymentStatus.WAITING_PAYMENT.getValue();
    }

    public Payment(String id, String method, Map<String, String> paymentData, Order order, String status) {
        this(id, method, paymentData, order);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
