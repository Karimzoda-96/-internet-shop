package my.shop.azhar.repository;

import java.util.List;
import my.shop.azhar.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByIsActiveTrueOrderBySortOrderAsc();
}
