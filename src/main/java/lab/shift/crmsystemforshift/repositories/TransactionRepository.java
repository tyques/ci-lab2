package lab.shift.crmsystemforshift.repositories;

import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllBySellerId(Long sellerId);

    /* TODO Надо разобраться с тем, как реализовать такую же логику без нативного SQL
        Почитать про @FilterDef и @Filter, а также AOP Aspect
    */

    @Query(value = "SELECT * FROM transactions", nativeQuery = true)
    List<TransactionEntity> findAllWithDeleted();
}