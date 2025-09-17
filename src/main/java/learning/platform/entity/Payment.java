package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.dto.PaymentRequest;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal amount;

    @CurrentTimestamp
    private LocalDateTime paidAt;

    private LocalDateTime expiresAt;

    public  Payment(long l, User user, BigDecimal bigDecimal, LocalDateTime now, LocalDateTime localDateTime){}

    public Payment(){}

    public Payment(PaymentRequest paymentRequest, User user) {
        this.user = user;
        this.amount = paymentRequest.amount();
        this.expiresAt = LocalDateTime.now().plusMonths(12);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void update() {
        this.paidAt = LocalDateTime.now();
        this.expiresAt = expiresAt.plusMonths(12);
    }

    public void setExpired() {
        this.expiresAt = LocalDateTime.now();
    }
}
