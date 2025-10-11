package lab.shift.crmsystemforshift.repositories;

import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllBySellerId(Long sellerId);
}
