package com.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Selenium ile basit bir UI otomasyon testi.
 * Yerel bir HTML dosyasini headless Chrome'da acar, dogrular.
 * (Harici siteye bagimli degil => kararli, "flaky" degil.)
 */
class ExampleUiTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        // CI runner'inda ekran yok => headless. --no-sandbox / dev-shm Linux runner icin gerekli.
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    /** Sayfa basligini dogrular (pozitif senaryo). */
    @Test
    void pageTitleIsCorrect() {
        driver.get(pageUrl());
        assertEquals("CI Demo", driver.getTitle());
    }

    /** Butona tiklayinca dogru metnin ciktigini dogrular (etkilesim senaryosu). */
    @Test
    void clickingButtonShowsResult() {
        driver.get(pageUrl());
        driver.findElement(By.id("btn")).click();
        String result = driver.findElement(By.id("result")).getText();
        assertEquals("Clicked!", result);
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
