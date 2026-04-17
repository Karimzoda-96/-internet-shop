package my.shop.azhar.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.BannerRequest;
import my.shop.azhar.dto.BannerResponse;
import my.shop.azhar.entity.Banner;
import my.shop.azhar.exception.NotFoundException;
import my.shop.azhar.mapper.BannerMapper;
import my.shop.azhar.repository.BannerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Transactional(readOnly = true)
    public List<BannerResponse> getActiveBanners() {
        return bannerRepository.findAllByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Transactional
    public BannerResponse create(BannerRequest request) {
        Banner banner = Banner.builder()
                .title(request.title())
                .imageUrl(request.imageUrl())
                .link(request.link())
                .isActive(request.isActive() == null || request.isActive())
                .sortOrder(request.sortOrder())
                .build();
        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Transactional
    public BannerResponse update(Long id, BannerRequest request) {
        Banner banner = findById(id);
        banner.setTitle(request.title());
        banner.setImageUrl(request.imageUrl());
        banner.setLink(request.link());
        banner.setActive(request.isActive() == null || request.isActive());
        banner.setSortOrder(request.sortOrder());
        return bannerMapper.toResponse(banner);
    }

    @Transactional
    public void delete(Long id) {
        Banner banner = findById(id);
        banner.setActive(false);
    }

    private Banner findById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner not found"));
    }
}
