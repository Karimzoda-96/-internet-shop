package my.shop.azhar.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.PageResponse;
import my.shop.azhar.dto.ProductImageRequest;
import my.shop.azhar.dto.ProductImageResponse;
import my.shop.azhar.dto.ProductListItemResponse;
import my.shop.azhar.dto.ProductRequest;
import my.shop.azhar.dto.ProductResponse;
import my.shop.azhar.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public PageResponse<ProductListItemResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam(required = false) Boolean discounted) {
        return productService.getProducts(page, size, search, categoryId, minPrice, maxPrice, sort, featured, isNew, discounted);
    }

    @GetMapping("/api/products/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getActiveById(id);
    }

    @GetMapping("/api/products/slug/{slug}")
    public ProductResponse getProductBySlug(@PathVariable String slug) {
        return productService.getActiveBySlug(slug);
    }

    @GetMapping("/api/products/featured")
    public List<ProductListItemResponse> getFeatured() {
        return productService.getFeatured();
    }

    @GetMapping("/api/products/new")
    public List<ProductListItemResponse> getNewProducts() {
        return productService.getNewProducts();
    }

    @GetMapping("/api/products/discounted")
    public List<ProductListItemResponse> getDiscounted() {
        return productService.getDiscounted();
    }

    @PostMapping("/api/admin/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/api/admin/products/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/api/admin/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/api/admin/products/{id}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageResponse addImage(@PathVariable Long id, @Valid @RequestBody ProductImageRequest request) {
        return productService.addImage(id, request);
    }
}
