package my.shop.azhar.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import my.shop.azhar.dto.CategoryRequest;
import my.shop.azhar.dto.LoginRequest;
import my.shop.azhar.dto.OrderCreateRequest;
import my.shop.azhar.dto.OrderItemCreateRequest;
import my.shop.azhar.dto.OrderStatusUpdateRequest;
import my.shop.azhar.dto.ProductRequest;
import my.shop.azhar.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ShopApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authLoginReturnsJwt() throws Exception {
        loginAndGetToken();
    }

    @Test
    void adminCanCreateCategory() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest(
                                "Test Category A",
                                "test-category-a",
                                "Test category",
                                "/images/test-a.jpg",
                                true))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("test-category-a"));
    }

    @Test
    void adminCanCreateProduct() throws Exception {
        String token = loginAndGetToken();
        long categoryId = createCategory(token, "test-product-category");

        mockMvc.perform(post("/api/admin/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest(
                                categoryId,
                                "Test Product A",
                                "test-product-a",
                                "Product description",
                                new BigDecimal("25.50"),
                                null,
                                "TEST-SKU-A",
                                10,
                                "TEST-BAR-A",
                                "/images/test-product-a.jpg",
                                true,
                                true,
                                false))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("test-product-a"))
                .andExpect(jsonPath("$.price").value(25.50));
    }

    @Test
    void publicCanCreateOrderAndAdminCanUpdateStatus() throws Exception {
        String token = loginAndGetToken();
        long categoryId = createCategory(token, "test-order-category");
        long productId = createProduct(token, categoryId, "test-order-product", "TEST-ORDER-SKU");

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderCreateRequest(
                                "Customer",
                                "+992900000000",
                                "customer@example.com",
                                "Dushanbe",
                                "Call before delivery",
                                "courier",
                                "cash",
                                List.of(new OrderItemCreateRequest(productId, 2))))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.totalAmount").value(51.00))
                .andReturn();

        long orderId = objectMapper.readTree(orderResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/api/admin/orders/{id}/status", orderId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderStatusUpdateRequest(OrderStatus.CONFIRMED))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    private String loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("admin@example.com", "Admin123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").isString())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private long createCategory(String token, String slug) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest(
                                "Category " + slug,
                                slug,
                                "Test category",
                                "/images/" + slug + ".jpg",
                                true))))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    private long createProduct(String token, long categoryId, String slug, String sku) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/admin/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest(
                                categoryId,
                                "Product " + slug,
                                slug,
                                "Product description",
                                new BigDecimal("25.50"),
                                null,
                                sku,
                                10,
                                "BAR-" + sku,
                                "/images/" + slug + ".jpg",
                                true,
                                false,
                                true))))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }
}
