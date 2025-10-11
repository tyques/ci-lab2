package lab.shift.crmsystemforshift.data.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller")
public class Seller {
    @Id
    @SequenceGenerator(name = "seller_seq", sequenceName = "seller_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "seller_id")
    private long sellerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    // TODO Попробовать поубирать аннотации
    @Column(name = "registration_date")
    @CreatedDate
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactions = new ArrayList<>();
}
