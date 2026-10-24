package lab.shift.crmsystemforshift.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sellers")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE sellers SET deletion_date = now() WHERE id=?")
@SQLRestriction("deletion_date is null")
public class SellerEntity {
    @Id
    @SequenceGenerator(name = "seller_seq", sequenceName = "seller_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @CreatedDate
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TransactionEntity> transactions = new ArrayList<>();
}