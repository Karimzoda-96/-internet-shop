package my.shop.azhar.repository;

import java.util.List;
import java.util.Optional;
import my.shop.azhar.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.isActive = true order by c.name asc")
    List<Category> findAllByIsActiveTrueOrderByNameAsc();

    Optional<Category> findBySlug(String slug);

    @Query("select c from Category c where c.id = :id and c.isActive = true")
    Optional<Category> findByIdAndIsActiveTrue(Long id);

    @Query("select c from Category c where c.slug = :slug and c.isActive = true")
    Optional<Category> findBySlugAndIsActiveTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Query("select count(c) from Category c where c.isActive = true")
    long countByIsActiveTrue();
}
