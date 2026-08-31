package com.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Selenium ile basit bir UI otomasyon testi.
 * Yerel bir HTML dosyasini headless Chrome'da acar, dogrular.
 * (Harici siteye bagimli degil => kararli, "flaky" degil.)
 */
class ExampleUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
// "headless" ortam degiskeni "false" degilse headless kos (CI icin guvenli varsayilan)
        if (!"false".equals(System.getenv("HEADLESS"))) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /** Sayfa basligini dogrular (pozitif senaryo). */
    @Test
    void pageTitleIsCorrect() {
        driver.get(pageUrl());

        assertEquals("CI Demo", driver.getTitle());
    }

    /** Butona tiklayinca dogru metnin ciktigini dogrular (etkilesim senaryosu). */
    @Test
    void clickingButtonShowsResult() throws InterruptedException{

        driver.get(pageUrl());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btn"))).click();
        driver.findElement(By.id("btn")).click();
        Thread.sleep(1500);
        String result = driver.findElement(By.id("result")).getText();
        wait.until(ExpectedConditions.textToBe(By.id("result"),"Clicked!"));
        assertEquals("Clicked!", result);
        Thread.sleep(1500);
    }

    private String pageUrl() {
        return Paths.get("src/test/resources/index.html").toUri().toString();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
