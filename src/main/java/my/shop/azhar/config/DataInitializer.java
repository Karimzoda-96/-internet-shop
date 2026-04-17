package my.shop.azhar.config;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.entity.Banner;
import my.shop.azhar.entity.Category;
import my.shop.azhar.entity.Product;
import my.shop.azhar.entity.User;
import my.shop.azhar.enums.UserRole;
import my.shop.azhar.repository.BannerRepository;
import my.shop.azhar.repository.CategoryRepository;
import my.shop.azhar.repository.ProductRepository;
import my.shop.azhar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin-email}")
    private String adminEmail;

    @Value("${app.seed.admin-password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        seedAdmin();
        List<Category> categories = seedCategories();
        seedProducts(categories);
        seedBanners();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail(adminEmail)) {
            userRepository.save(User.builder()
                    .fullName("Store Administrator")
                    .email(adminEmail)
                    .phone("+10000000000")
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .build());
        }
    }

    private List<Category> seedCategories() {
        createCategory("Электроника", "electronics", "Смартфоны, гаджеты и аксессуары", "/images/categories/electronics.jpg");
        createCategory("Одежда", "clothing", "Повседневная одежда и базовые вещи", "/images/categories/clothing.jpg");
        createCategory("Дом", "home", "Товары для дома и кухни", "/images/categories/home.jpg");
        createCategory("Красота", "beauty", "Косметика и уход", "/images/categories/beauty.jpg");
        return categoryRepository.findAll();
    }

    private void createCategory(String name, String slug, String description, String imageUrl) {
        if (!categoryRepository.existsBySlug(slug)) {
            categoryRepository.save(Category.builder()
                    .name(name)
                    .slug(slug)
                    .description(description)
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .build());
        }
    }

    private void seedProducts(List<Category> categories) {
        if (productRepository.count() > 0 || categories.isEmpty()) {
            return;
        }
        Category electronics = findCategory(categories, "electronics");
        Category clothing = findCategory(categories, "clothing");
        Category home = findCategory(categories, "home");
        Category beauty = findCategory(categories, "beauty");

        saveProduct(electronics, "Смартфон Nova X", "smartphone-nova-x", "SKU-1001", "Современный смартфон с ярким экраном.", "699.00", "799.00", 35, true, true, "/images/products/nova-x.jpg");
        saveProduct(electronics, "Беспроводные наушники AirBeat", "wireless-headphones-airbeat", "SKU-1002", "Легкие наушники с шумоподавлением.", "129.00", "159.00", 60, true, false, "/images/products/airbeat.jpg");
        saveProduct(electronics, "Умные часы Pulse", "smart-watch-pulse", "SKU-1003", "Фитнес, уведомления и мониторинг сна.", "199.00", null, 25, false, true, "/images/products/pulse.jpg");
        saveProduct(electronics, "Power Bank 20000", "power-bank-20000", "SKU-1004", "Емкий внешний аккумулятор.", "49.00", null, 120, false, false, "/images/products/power-bank.jpg");
        saveProduct(clothing, "Худи Essential", "hoodie-essential", "SKU-2001", "Мягкое хлопковое худи.", "59.00", "79.00", 80, true, false, "/images/products/hoodie.jpg");
        saveProduct(clothing, "Футболка Classic", "tshirt-classic", "SKU-2002", "Базовая футболка на каждый день.", "19.00", null, 150, false, true, "/images/products/tshirt.jpg");
        saveProduct(clothing, "Куртка Urban", "jacket-urban", "SKU-2003", "Легкая городская куртка.", "149.00", "189.00", 30, true, false, "/images/products/jacket.jpg");
        saveProduct(home, "Кофеварка Morning", "coffee-maker-morning", "SKU-3001", "Компактная кофеварка для дома.", "89.00", null, 40, true, true, "/images/products/coffee-maker.jpg");
        saveProduct(home, "Набор посуды Terra", "cookware-terra", "SKU-3002", "Практичный набор для кухни.", "119.00", "139.00", 20, false, false, "/images/products/cookware.jpg");
        saveProduct(home, "Настольная лампа Halo", "desk-lamp-halo", "SKU-3003", "Регулируемая LED-лампа.", "39.00", null, 70, false, true, "/images/products/lamp.jpg");
        saveProduct(beauty, "Крем Daily Care", "cream-daily-care", "SKU-4001", "Увлажняющий крем для ежедневного ухода.", "24.00", null, 95, true, false, "/images/products/cream.jpg");
        saveProduct(beauty, "Шампунь Fresh", "shampoo-fresh", "SKU-4002", "Мягкий шампунь для частого использования.", "12.00", "16.00", 110, false, false, "/images/products/shampoo.jpg");
    }

    private Category findCategory(List<Category> categories, String slug) {
        return categories.stream()
                .filter(category -> slug.equals(category.getSlug()))
                .findFirst()
                .orElse(categories.getFirst());
    }

    private void saveProduct(Category category, String name, String slug, String sku, String description, String price,
                             String oldPrice, int stock, boolean featured, boolean isNew, String imageUrl) {
        productRepository.save(Product.builder()
                .category(category)
                .name(name)
                .slug(slug)
                .sku(sku)
                .description(description)
                .price(new BigDecimal(price))
                .oldPrice(oldPrice == null ? null : new BigDecimal(oldPrice))
                .stockQuantity(stock)
                .barcode("BAR-" + sku)
                .mainImageUrl(imageUrl)
                .isActive(true)
                .isFeatured(featured)
                .isNew(isNew)
                .build());
    }

    private void seedBanners() {
        if (bannerRepository.count() > 0) {
            return;
        }
        bannerRepository.save(Banner.builder()
                .title("Весенняя распродажа")
                .imageUrl("/images/banners/spring-sale.jpg")
                .link("/products?discounted=true")
                .isActive(true)
                .sortOrder(1)
                .build());
        bannerRepository.save(Banner.builder()
                .title("Новинки недели")
                .imageUrl("/images/banners/new-products.jpg")
                .link("/products?isNew=true")
                .isActive(true)
                .sortOrder(2)
                .build());
        bannerRepository.save(Banner.builder()
                .title("Выбор магазина")
                .imageUrl("/images/banners/featured.jpg")
                .link("/products?featured=true")
                .isActive(true)
                .sortOrder(3)
                .build());
    }
}
