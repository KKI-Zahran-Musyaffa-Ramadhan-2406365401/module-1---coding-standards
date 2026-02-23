package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void testCreateProductAndVerifyInList(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");

        String pageTitle = driver.getTitle();
        assertEquals("Create New Product", pageTitle);

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        String testProductName = "Coba tes apaya";
        String testProductQuantity = "100";

        nameInput.sendKeys(testProductName);
        quantityInput.sendKeys(testProductQuantity);

        submitButton.click();

        Thread.sleep(500);

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/list"),
                "Should redirect to product list page");
        String listPageTitle = driver.getTitle();
        assertEquals("Product List", listPageTitle);

        WebElement productTable = driver.findElement(By.cssSelector("table.table"));
        List<WebElement> tableRows = productTable.findElements(By.cssSelector("tbody tr"));

        assertTrue(tableRows.size() > 0, "Product list should contain at least one product");
        boolean productFound = false;
        for (WebElement row : tableRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                String productName = cells.get(0).getText();
                String productQuantity = cells.get(1).getText();

                if (productName.equals(testProductName) &&
                        productQuantity.equals(testProductQuantity)) {
                    productFound = true;
                    break;
                }
            }
        }

        assertTrue(productFound,
                String.format("Product '%s' with quantity '%s' should be in the product list",
                        testProductName, testProductQuantity));
    }

    @Test
    void testCreateProductFormHasCorrectElements(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");

        assertEquals("Create New Product", driver.getTitle());

        WebElement heading = driver.findElement(By.tagName("h3"));
        assertEquals("Create New Product", heading.getText());

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertNotNull(nameInput, "Name input field should exist");
        assertNotNull(quantityInput, "Quantity input field should exist");
        assertNotNull(submitButton, "Submit button should exist");
        assertEquals("text", nameInput.getAttribute("type"));
        assertEquals("text", quantityInput.getAttribute("type"));
        assertEquals("Submit", submitButton.getText());
    }

    @Test
    void testCreateProductWithEmptyFields(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        Thread.sleep(500);

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/list"),
                "Should be redirected to product list page even with empty fields");
    }
}
