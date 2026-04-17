package my.shop.azhar.repository;

import java.util.List;
import java.util.Optional;
import my.shop.azhar.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySlug(String slug);

    @Query("select p from Product p where p.id = :id and p.isActive = true")
    Optional<Product> findByIdAndIsActiveTrue(Long id);

    @Query("select p from Product p where p.slug = :slug and p.isActive = true")
    Optional<Product> findBySlugAndIsActiveTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Query("select p from Product p where p.isActive = true and p.isFeatured = true order by p.createdAt desc")
    List<Product> findByIsActiveTrueAndIsFeaturedTrueOrderByCreatedAtDesc(Pageable pageable);

    @Query("select p from Product p where p.isActive = true and p.isNew = true order by p.createdAt desc")
    List<Product> findByIsActiveTrueAndIsNewTrueOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    @Query("select count(p) from Product p where p.isActive = true")
    long countByIsActiveTrue();
}
