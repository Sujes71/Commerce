package es.sujes71.test.integration;

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

  // ========== SUCCESS CASES ==========

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

  // ========== ERROR CASES - MISSING PARAMETERS ==========

  @Test
  void testError_missingBrandId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_missingProductId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_missingApplicationDate_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_allParametersMissing_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices"))
        .andExpect(status().isBadRequest());
  }

  // ========== ERROR CASES - INVALID PARAMETERS ==========

  @Test
  void testError_invalidBrandId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "invalid")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_invalidProductId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "invalid")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_invalidDateFormat_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "invalid-date"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_emptyBrandId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_emptyProductId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_emptyApplicationDate_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", ""))
        .andExpect(status().isBadRequest());
  }

  // ========== ERROR CASES - DATA NOT FOUND ==========

  @Test
  void testError_nonExistentBrand_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "999")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testError_nonExistentProduct_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "99999")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testError_dateOutOfRange_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2025-12-31T23:59:59"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testError_pastDate_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2019-01-01T00:00:00"))
        .andExpect(status().isNotFound());
  }

  // ========== EDGE CASES - BOUNDARY VALUES ==========

  @Test
  void testEdge_negativeIds_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "-1")
            .param("productId", "-35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_zeroIds_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "0")
            .param("productId", "0")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_veryLargeIds_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "2147483647")
            .param("productId", "2147483647")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isNotFound());
  }

  // ========== ADDITIONAL TEST CASES ==========

  @Test
  void testError_whitespaceOnlyBrandId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "   ")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_whitespaceOnlyProductId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "   ")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_whitespaceOnlyApplicationDate_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "   "))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_floatingPointIds_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1.5")
            .param("productId", "35455.7")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_dateWithoutTime_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_dateWithWrongTimeFormat_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14 10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testError_mixedValidAndInvalidParams_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "invalid")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_negativeBrandIdOnly_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "-1")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_negativeProductIdOnly_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "-35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_zeroBrandIdOnly_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "0")
            .param("productId", "35455")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testEdge_zeroProductIdOnly_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", "1")
            .param("productId", "0")
            .param("applicationDate", "2020-06-14T10:00:00"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testValidation_leadingAndTrailingSpaces_shouldWork() throws Exception {
    mockMvc.perform(get("/commerce/prices")
            .param("brandId", " 1 ")
            .param("productId", " 35455 ")
            .param("applicationDate", " 2020-06-14T10:00:00 "))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brandId", is(1)))
        .andExpect(jsonPath("$.productId", is(35455)));
  }
}