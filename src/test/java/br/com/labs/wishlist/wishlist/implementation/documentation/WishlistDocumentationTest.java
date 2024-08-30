package br.com.labs.wishlist.wishlist.implementation.documentation;

import br.com.labs.wishlist.wishlist.MongoDBIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class WishlistDocumentationTest implements MongoDBIntegrationTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void add() throws Exception {
        final String userId = "1";
        final String productId = "1";
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/wishlist/products?userId=" + userId + "&productId=" + productId)
                                .accept(MediaType.APPLICATION_JSON.getMediaType())
                )
                .andExpect(status().isOk())
                .andDo(document("add"));
    }

    @Test
    protected void remove() throws Exception {
        final String userId = "1";
        final String productId = "1";
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/v1/wishlist/products?userId=" + userId + "&productId=" + productId)
                                .accept(MediaType.APPLICATION_JSON.getMediaType())
                )
                .andExpect(status().isOk())
                .andDo(document("remove"));
    }

    @Test
    protected void getWishlist() throws Exception {
        final String userId = "1";
        final String productId = "1";
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/wishlist?userId=1")
                                .accept(MediaType.APPLICATION_JSON.getMediaType())
                )
                .andExpect(status().isOk())
                .andDo(document("get"));

    }

    @Test
    protected void contains() throws Exception {
        final String userId = "1";
        final String productId = "1";
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/wishlist/products?userId=" + userId + "&productId=" + productId)
                                .accept(MediaType.APPLICATION_JSON.getMediaType())
                )
                .andExpect(status().isOk())
                .andDo(document("contains"));
    }
}