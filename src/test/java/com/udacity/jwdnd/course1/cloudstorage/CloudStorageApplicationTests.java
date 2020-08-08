package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }


    /**
     * test that signs up a new user, logs in,
     * verifies that the home page is accessible,
     * logs out, and verifies that the home page is no longer accessible
     */
    @Test
    public void test_signup_login_logout_flow() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/api/user/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        WebElement fname = driver.findElement(By.id("inputFirstName"));
        WebElement lname = driver.findElement(By.id("inputLastName"));
        WebElement uname = driver.findElement(By.id("inputUsername"));
        WebElement pwd = driver.findElement(By.id("inputPassword"));
        WebElement signup = driver.findElement(By.xpath("//button[text()='Sign Up']"));

        fname.sendKeys("av");
        lname.sendKeys("av");
        uname.sendKeys("testUsername");
        pwd.sendKeys("test");
        signup.click();

        String actualUrl = "http://localhost:" + this.port + "/api/user/signup";
        Assertions.assertEquals(driver.getCurrentUrl(), actualUrl);

        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals(driver.getTitle(), "Login");

        WebDriverWait wait1 = new WebDriverWait(driver, 2);
        uname = driver.findElement(By.id("inputUsername"));
        pwd = driver.findElement(By.id("inputPassword"));
        WebElement login = wait1.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));

        uname.sendKeys("testUsername");
        pwd.sendKeys("test");
        login.click();
        wait1.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals(driver.getTitle(), "Home");


        WebElement logout;
        logout = wait1.until(ExpectedConditions.elementToBeClickable(By.id("logoutButton")));
        logout.click();

        try{
            Boolean aBoolean = wait1.until(ExpectedConditions.titleContains("Login"));
            Assertions.assertTrue(aBoolean);
        } catch (TimeoutException ex) {
            logout.click();
            Boolean aBoolean = wait1.until(ExpectedConditions.titleContains("Login"));
            Assertions.assertTrue(aBoolean);
        }

        Assertions.assertEquals("Login", driver.getTitle());
        WebElement logoutMessage = driver.findElement(By.id("logoutMessage"));
        Assertions.assertEquals(logoutMessage.getText(), "You have been logged out");

        driver.get("http://localhost:" + this.port + "/api/user/home");
        Assertions.assertEquals(driver.getTitle(), "Error/Access-Denied");

    }


    @Test
    public void test_invalid_credentials() {

        driver.get("http://localhost:" + this.port + "/api/user/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        WebElement fname = driver.findElement(By.id("inputFirstName"));
        WebElement lname = driver.findElement(By.id("inputLastName"));
        WebElement uname = driver.findElement(By.id("inputUsername"));
        WebElement pwd = driver.findElement(By.id("inputPassword"));
        WebElement signup = driver.findElement(By.xpath("//button[text()='Sign Up']"));

        fname.sendKeys("av");
        lname.sendKeys("av");
        uname.sendKeys("testUsername11");
        pwd.sendKeys("test11");
        signup.click();

        String actualUrl = "http://localhost:" + this.port + "/api/user/signup";
        Assertions.assertEquals(driver.getCurrentUrl(), actualUrl);

        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals(driver.getTitle(), "Login");

        uname = driver.findElement(By.id("inputUsername"));
        pwd = driver.findElement(By.id("inputPassword"));
        WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));

        uname.sendKeys("InvalidUsername");
        pwd.sendKeys("InvalidPassword");
        login.click();
        Assertions.assertEquals(driver.getTitle(), "Login");
        WebElement invalidMessage = driver.findElement(By.id("invalidUsernameMessage"));
        Assertions.assertEquals(invalidMessage.getText(), "Invalid username or password");
    }

    /**
     * test that verifies that an unauthorized user
     * can only access the login and signup pages
     */
    @Test
    public void test_non_authenticated_flow() {
        driver.get("http://localhost:" + this.port + "/api/user/home");
        Assertions.assertEquals(driver.getTitle(), "Error/Access-Denied");

        driver.get("http://localhost:" + this.port + "/api/file/1");
        Assertions.assertEquals(driver.getTitle(), "Error/Access-Denied");

        driver.get("http://localhost:" + this.port + "/api/file/downloadFile/1");
        Assertions.assertEquals(driver.getTitle(), "Error/Access-Denied");

        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals(driver.getTitle(), "Login");

        driver.get("http://localhost:" + this.port + "/api/user/signup");
        Assertions.assertEquals(driver.getTitle(), "Sign Up");
    }

    @Test
    public void test_requestMethodNotSupportedException() {
        driver.get("http://localhost:" + this.port + "/api/user/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        WebElement fname = driver.findElement(By.id("inputFirstName"));
        WebElement lname = driver.findElement(By.id("inputLastName"));
        WebElement uname = driver.findElement(By.id("inputUsername"));
        WebElement pwd = driver.findElement(By.id("inputPassword"));
        WebElement signup = driver.findElement(By.xpath("//button[text()='Sign Up']"));

        fname.sendKeys("av");
        lname.sendKeys("av");
        uname.sendKeys("testUsername1234");
        pwd.sendKeys("test");
        signup.click();

        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals(driver.getTitle(), "Login");

        uname = driver.findElement(By.id("inputUsername"));
        pwd = driver.findElement(By.id("inputPassword"));
        WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
        uname.sendKeys("testUsername1234");
        pwd.sendKeys("test");
        login.click();

        driver.get("http://localhost:" + this.port + "/api/file/1");
        WebElement responseStatus = driver.findElement(By.id("responseStatus"));
        String actualStatus = "406 NOT_ACCEPTABLE";
        Assertions.assertEquals(responseStatus.getText(), actualStatus);
    }


}
