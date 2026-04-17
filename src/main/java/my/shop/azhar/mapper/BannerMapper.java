package my.shop.azhar.mapper;

import my.shop.azhar.dto.BannerResponse;
import my.shop.azhar.entity.Banner;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper {
    public BannerResponse toResponse(Banner banner) {
        return new BannerResponse(
                banner.getId(),
                banner.getTitle(),
                banner.getImageUrl(),
                banner.getLink(),
                banner.isActive(),
                banner.getSortOrder(),
                banner.getCreatedAt(),
                banner.getUpdatedAt()
        );
    }
}
