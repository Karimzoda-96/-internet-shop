package my.shop.azhar.repository;

import java.util.List;
import java.util.Optional;
import my.shop.azhar.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySlug(String slug);

    Optional<Product> findByIdAndIsActiveTrue(Long id);

    Optional<Product> findBySlugAndIsActiveTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<Product> findByIsActiveTrueAndIsFeaturedTrueOrderByCreatedAtDesc(Pageable pageable);

    List<Product> findByIsActiveTrueAndIsNewTrueOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    long countByIsActiveTrue();
}
