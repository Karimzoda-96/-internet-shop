package my.shop.azhar.service;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.PageResponse;
import my.shop.azhar.dto.ProductImageRequest;
import my.shop.azhar.dto.ProductImageResponse;
import my.shop.azhar.dto.ProductListItemResponse;
import my.shop.azhar.dto.ProductRequest;
import my.shop.azhar.dto.ProductResponse;
import my.shop.azhar.entity.Category;
import my.shop.azhar.entity.Product;
import my.shop.azhar.entity.ProductImage;
import my.shop.azhar.exception.BadRequestException;
import my.shop.azhar.exception.ConflictException;
import my.shop.azhar.exception.NotFoundException;
import my.shop.azhar.mapper.ProductMapper;
import my.shop.azhar.repository.ProductImageRepository;
import my.shop.azhar.repository.ProductRepository;
import my.shop.azhar.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public PageResponse<ProductListItemResponse> getProducts(int page, int size, String search, Long categoryId,
                                                             BigDecimal minPrice, BigDecimal maxPrice, String sort,
                                                             Boolean featured, Boolean isNew, Boolean discounted) {
        Pageable pageable = PageRequest.of(normalizePage(page), normalizeSize(size), parseSort(sort));
        Page<ProductListItemResponse> products = productRepository.findAll(
                        publicSpecification(search, categoryId, minPrice, maxPrice, featured, isNew, discounted),
                        pageable)
                .map(productMapper::toListItem);
        return PageResponse.from(products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getActiveById(Long id) {
        return productMapper.toResponse(findActiveById(id));
    }

    @Transactional(readOnly = true)
    public ProductResponse getActiveBySlug(String slug) {
        return productMapper.toResponse(productRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new NotFoundException("Product not found")));
    }

    @Transactional(readOnly = true)
    public List<ProductListItemResponse> getFeatured() {
        return productRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByCreatedAtDesc(PageRequest.of(0, 12)).stream()
                .map(productMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductListItemResponse> getNewProducts() {
        return productRepository.findByIsActiveTrueAndIsNewTrueOrderByCreatedAtDesc(PageRequest.of(0, 12)).stream()
                .map(productMapper::toListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductListItemResponse> getDiscounted() {
        return productRepository.findAll(publicSpecification(null, null, null, null, null, null, true),
                        PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "createdAt"))).stream()
                .map(productMapper::toListItem)
                .toList();
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        String slug = resolveSlug(request.slug(), request.name());
        if (productRepository.existsBySlug(slug)) {
            throw new ConflictException("Product slug already exists");
        }
        Category category = categoryService.findActiveById(request.categoryId());
        Product product = Product.builder()
                .category(category)
                .name(request.name())
                .slug(slug)
                .description(request.description())
                .price(request.price())
                .oldPrice(request.oldPrice())
                .sku(request.sku())
                .stockQuantity(request.stockQuantity())
                .barcode(request.barcode())
                .mainImageUrl(request.mainImageUrl())
                .isActive(request.isActive() == null || request.isActive())
                .isFeatured(Boolean.TRUE.equals(request.isFeatured()))
                .isNew(Boolean.TRUE.equals(request.isNew()))
                .build();
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        String slug = resolveSlug(request.slug(), request.name());
        if (productRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException("Product slug already exists");
        }
        product.setCategory(categoryService.findActiveById(request.categoryId()));
        product.setName(request.name());
        product.setSlug(slug);
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setOldPrice(request.oldPrice());
        product.setSku(request.sku());
        product.setStockQuantity(request.stockQuantity());
        product.setBarcode(request.barcode());
        product.setMainImageUrl(request.mainImageUrl());
        product.setActive(request.isActive() == null || request.isActive());
        product.setFeatured(Boolean.TRUE.equals(request.isFeatured()));
        product.setNew(Boolean.TRUE.equals(request.isNew()));
        return productMapper.toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setActive(false);
    }

    @Transactional
    public ProductImageResponse addImage(Long productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.imageUrl())
                .sortOrder(request.sortOrder() == null ? 0 : request.sortOrder())
                .build();
        return productMapper.toImageResponse(productImageRepository.save(image));
    }

    public Product findActiveById(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    private Specification<Product> publicSpecification(String search, Long categoryId, BigDecimal minPrice,
                                                       BigDecimal maxPrice, Boolean featured, Boolean isNew,
                                                       Boolean discounted) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("isActive")));
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("sku")), pattern)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (featured != null) {
                predicates.add(featured ? cb.isTrue(root.get("isFeatured")) : cb.isFalse(root.get("isFeatured")));
            }
            if (isNew != null) {
                predicates.add(isNew ? cb.isTrue(root.get("isNew")) : cb.isFalse(root.get("isNew")));
            }
            if (Boolean.TRUE.equals(discounted)) {
                predicates.add(cb.isNotNull(root.get("oldPrice")));
                predicates.add(cb.greaterThan(root.get("oldPrice"), root.get("price")));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private int normalizePage(int page) {
        return Math.max(page, 0);
    }

    private int normalizeSize(int size) {
        return Math.min(Math.max(size, 1), 100);
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        if (!List.of("name", "price", "createdAt", "stockQuantity").contains(property)) {
            throw new BadRequestException("Unsupported sort field");
        }
        Sort.Direction direction = parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(direction, property);
    }

    private String resolveSlug(String slug, String name) {
        return SlugUtil.toSlug(slug == null || slug.isBlank() ? name : slug);
    }
}
