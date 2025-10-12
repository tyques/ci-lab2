package lab.shift.crmsystemforshift.repositories;

import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    /* TODO Надо разобраться с тем, как реализовать такую же логику без нативного SQL
        Почитать про @FilterDef и @Filter, а также AOP Aspect
     */

    @Query("SELECT t.seller FROM TransactionEntity t WHERE t.transactionDate BETWEEN :startDate AND :endDate GROUP BY t.seller ORDER BY SUM(t.amount) DESC LIMIT 1")
    Optional<SellerEntity> findMostProductiveSeller(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM SellerEntity s WHERE (SELECT SUM(t.amount) FROM TransactionEntity t WHERE t.seller = s AND t.transactionDate BETWEEN :startDate AND :endDate) < :maxAmount")
    List<SellerEntity> findSellersWithTotalAmountLessThan(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("maxAmount") double maxAmount);

    @Query(value = "SELECT * FROM sellers", nativeQuery = true)
    List<SellerEntity> findAllWithDeleted();
}