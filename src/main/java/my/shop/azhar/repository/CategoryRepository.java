package my.shop.azhar.repository;

import java.util.List;
import java.util.Optional;
import my.shop.azhar.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsActiveTrueOrderByNameAsc();

    Optional<Category> findBySlug(String slug);

    Optional<Category> findByIdAndIsActiveTrue(Long id);

    Optional<Category> findBySlugAndIsActiveTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    long countByIsActiveTrue();
}
