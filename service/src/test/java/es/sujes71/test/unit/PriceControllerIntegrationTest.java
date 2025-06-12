package es.sujes71.test.unit;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.sujes71.configuration.TestJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestJdbcConfig.class)
class PriceControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void test1_getPriceAt10AM_Day14_Product35455_Brand1_ZARA() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)))
        .andExpect(jsonPath("$.priceList", is(1)))
        .andExpect(jsonPath("$.price", is(35.50)))
        .andExpect(jsonPath("$.currency", is("EUR")));
  }

  @Test
  void test2_getPriceAt4PM_Day14_Product35455_Brand1_ZARA() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T16:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)))
        .andExpect(jsonPath("$.priceList", is(2)))
        .andExpect(jsonPath("$.price", is(25.45)))
        .andExpect(jsonPath("$.currency", is("EUR")));
  }

  @Test
  void test3_getPriceAt9PM_Day14_Product35455_Brand1_ZARA() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T21:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)))
        .andExpect(jsonPath("$.priceList", is(1)))
        .andExpect(jsonPath("$.price", is(35.50)))
        .andExpect(jsonPath("$.currency", is("EUR")));
  }

  @Test
  void test4_getPriceAt10AM_Day15_Product35455_Brand1_ZARA() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-15T10:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)))
        .andExpect(jsonPath("$.priceList", is(3)))
        .andExpect(jsonPath("$.price", is(30.50)))
        .andExpect(jsonPath("$.currency", is("EUR")));
  }

  @Test
  void test5_getPriceAt9PM_Day16_Product35455_Brand1_ZARA() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-16T21:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)))
        .andExpect(jsonPath("$.priceList", is(4)))
        .andExpect(jsonPath("$.price", is(38.95)))
        .andExpect(jsonPath("$.currency", is("EUR")));
  }
}