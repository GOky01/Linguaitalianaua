package com.project.linguaitalianaua;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeControllerGUITest {

    private WebDriver driver;

    private void login() {
        // Open the login page
        driver.get("http://localhost:8080/login");

        // Find the username and password fields, and enter the credentials
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.sendKeys("test");
        passwordField.sendKeys("2001a2001a");

        // Submit the login form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\andrii.silich\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver(options);
        login();
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testLoginPage() {

        driver.get("http://localhost:8080/login");

        assertEquals("Вхід", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("form")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("input[type='text']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("input[type='password']")).isDisplayed());
    }

    @Test
    public void testRegisterPage() {

        driver.get("http://localhost:8080/register");

        assertEquals("Реєстрація", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("form")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("input[type='text']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("input[type='email']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("input[type='password']")).isDisplayed());
    }

    @Test
    public void testHomePage() {
        driver.get("http://localhost:8080");

        assertEquals("Головна", driver.getTitle());
        assertTrue(driver.findElement(By.tagName("nav")).isDisplayed());
        assertTrue(driver.findElement(By.className("container")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("p")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("img")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("h5")).isDisplayed());
        assertTrue(driver.findElement(By.className("col-sm-4")).isDisplayed());
    }

    @Test
    public void testProfilePage() {
        driver.get("http://localhost:8080/profile");

        assertEquals("Профіль", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("h2")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("p")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("div")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("button")).isDisplayed());
    }

    @Test
    public void testAboutPage() {

        driver.get("http://localhost:8080/about");

        assertEquals("Про нас", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("section")).isDisplayed());
        assertTrue(driver.findElement(By.className("about-description")).isDisplayed());
        assertTrue(driver.findElement(By.className("profile-buttons")).isDisplayed());
    }

    @Test
    public void testContactPage() {

        driver.get("http://localhost:8080/contacts");

        assertEquals("Контакти", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("div")).isDisplayed());
        assertTrue(driver.findElement(By.className("contact-container")).isDisplayed());
        assertTrue(driver.findElement(By.className("contact-info")).isDisplayed());
        assertTrue(driver.findElement(By.className("profile-buttons")).isDisplayed());
    }

    @Test
    public void testCoursesPage() {

        driver.get("http://localhost:8080/courses");

        assertEquals("Курси", driver.getTitle());

        assertTrue(driver.findElement(By.tagName("div")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("h3")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("p")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("a")).isDisplayed());
    }

    @Test
    public void testFAQPage() {

        driver.get("http://localhost:8080/faq");

        assertEquals("FAQ", driver.getTitle());

        assertTrue(driver.findElement(By.className("faq-container")).isDisplayed());
        assertTrue(driver.findElement(By.tagName("button")).isDisplayed());
    }

}
