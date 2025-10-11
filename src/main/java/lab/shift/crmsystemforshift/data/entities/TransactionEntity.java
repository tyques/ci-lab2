package lab.shift.crmsystemforshift.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
@SQLDelete(sql = "UPDATE transactions SET deletion_date = now() WHERE id=?")
@Where(clause = "deletion_date is null")
public class TransactionEntity {
    @Id
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerEntity seller;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;
}