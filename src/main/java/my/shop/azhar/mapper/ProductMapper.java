package my.shop.azhar.mapper;

import java.util.Comparator;
import my.shop.azhar.dto.ProductImageResponse;
import my.shop.azhar.dto.ProductListItemResponse;
import my.shop.azhar.dto.ProductResponse;
import my.shop.azhar.entity.Product;
import my.shop.azhar.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ProductListItemResponse toListItem(Product product) {
        return new ProductListItemResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getOldPrice(),
                product.isDiscounted(),
                product.getSku(),
                product.getStockQuantity(),
                product.getMainImageUrl(),
                product.isFeatured(),
                product.isNew()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                categoryMapper.toResponse(product.getCategory()),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getOldPrice(),
                product.isDiscounted(),
                product.getSku(),
                product.getStockQuantity(),
                product.getBarcode(),
                product.getMainImageUrl(),
                product.isActive(),
                product.isFeatured(),
                product.isNew(),
                product.getImages().stream()
                        .sorted(Comparator.comparing(ProductImage::getSortOrder))
                        .map(this::toImageResponse)
                        .toList(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public ProductImageResponse toImageResponse(ProductImage image) {
        return new ProductImageResponse(image.getId(), image.getImageUrl(), image.getSortOrder(), image.getCreatedAt());
    }
}
