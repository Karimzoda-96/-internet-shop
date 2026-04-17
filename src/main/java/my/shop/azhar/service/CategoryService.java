package my.shop.azhar.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.CategoryRequest;
import my.shop.azhar.dto.CategoryResponse;
import my.shop.azhar.entity.Category;
import my.shop.azhar.exception.ConflictException;
import my.shop.azhar.exception.NotFoundException;
import my.shop.azhar.mapper.CategoryMapper;
import my.shop.azhar.repository.CategoryRepository;
import my.shop.azhar.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findAllByIsActiveTrueOrderByNameAsc().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getActiveById(Long id) {
        return categoryMapper.toResponse(findActiveById(id));
    }

    @Transactional(readOnly = true)
    public CategoryResponse getActiveBySlug(String slug) {
        return categoryMapper.toResponse(categoryRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String slug = resolveSlug(request.slug(), request.name());
        if (categoryRepository.existsBySlug(slug)) {
            throw new ConflictException("Category slug already exists");
        }
        Category category = Category.builder()
                .name(request.name())
                .slug(slug)
                .description(request.description())
                .imageUrl(request.imageUrl())
                .isActive(request.isActive() == null || request.isActive())
                .build();
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        String slug = resolveSlug(request.slug(), request.name());
        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException("Category slug already exists");
        }
        category.setName(request.name());
        category.setSlug(slug);
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());
        category.setActive(request.isActive() == null || request.isActive());
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setActive(false);
    }

    public Category findActiveById(Long id) {
        return categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    private String resolveSlug(String slug, String name) {
        return SlugUtil.toSlug(slug == null || slug.isBlank() ? name : slug);
    }
}
