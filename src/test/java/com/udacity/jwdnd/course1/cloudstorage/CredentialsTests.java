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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsTests {

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
    public void create_edit_delete_credentials_flow() {
        String username = "av12";
        String password = "av12";
        signupAndLoginWithUserName(username, password);
        WebDriverWait wait1 = new WebDriverWait(driver, 2);

        WebElement nav = wait1.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
        nav.click();

        WebElement button;
        try{
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("addCredButton")));
            button.click();
        } catch (TimeoutException ex) {
            nav.click();
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("addCredButton")));
            button.click();
        }

        WebElement url = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
        WebElement uname = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
        WebElement pwd = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
        WebElement submit = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credSubmit")));

        String credUsername = "AtulVerma";
        String actualPwd = "1212";
        url.sendKeys("udacity");
        uname.sendKeys(credUsername);
        pwd.sendKeys(actualPwd);
        submit.click();

        Assertions.assertEquals(driver.getTitle(), "Result");

        driver.get("http://localhost:" + this.port + "/api/user/home");
        nav = wait1.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
        nav.click();

        int index = 2;
        WebElement baseTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        WebElement tr = tableRows.get(index);
        List<WebElement> thList = tr.findElements(By.tagName("td"));
        Boolean aBoolean = wait1.until(ExpectedConditions.textToBePresentInElement(thList.get(1), credUsername));
        Assertions.assertTrue(aBoolean);
        String insertedPwd = thList.get(2).getText();

        // verifies that the passwd is encrypted
        Assertions.assertNotEquals(insertedPwd, actualPwd);

        /*
        Editting password test cases
         */

        String edittedUsername = "Atul";
        WebElement editButton = wait1.until(ExpectedConditions.elementToBeClickable(By.id("editCredButton-" + credUsername)));
        editButton.click();
        uname = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
        submit = wait1.until(ExpectedConditions.elementToBeClickable(By.id("credSubmit")));

        uname.clear();
        uname.sendKeys(edittedUsername);
        submit.click();

        // Editted credentials saved successfully
        Assertions.assertEquals(driver.getTitle(), "Result");

        //verifying editted username
        driver.get("http://localhost:" + this.port + "/api/user/home");
        nav = wait1.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
        nav.click();

        baseTable = driver.findElement(By.id("credentialTable"));
        tableRows = baseTable.findElements(By.tagName("tr"));
        tr = tableRows.get(index);
        thList = tr.findElements(By.tagName("td"));
        aBoolean = wait1.until(ExpectedConditions.textToBePresentInElement(thList.get(1), edittedUsername));
        Assertions.assertTrue(aBoolean);
        String insertedUsername = thList.get(1).getText();
        Assertions.assertEquals(insertedUsername, edittedUsername);

        /*
        Deleting the credentials and verifying the result
         */
        button = driver.findElement(By.id("deleteCredButton-" + edittedUsername));
        button.click();
        baseTable = driver.findElement(By.id("credentialTable"));
        tableRows = baseTable.findElements(By.tagName("tr"));
        Assertions.assertEquals(tableRows.size(), 2);

    }

    private void signupAndLoginWithUserName(String username, String passwd) {
        driver.get("http://localhost:" + this.port + "/api/user/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        WebElement fname = driver.findElement(By.id("inputFirstName"));
        WebElement lname = driver.findElement(By.id("inputLastName"));
        WebElement uname = driver.findElement(By.id("inputUsername"));
        WebElement pwd = driver.findElement(By.id("inputPassword"));
        WebElement signup =driver.findElement(By.xpath("//button[text()='Sign Up']"));

        fname.sendKeys("av");
        lname.sendKeys("av");
        uname.sendKeys(username);
        pwd.sendKeys(passwd);
        signup.click();

        String actualUrl = "http://localhost:" + this.port + "/api/user/signup";
        Assertions.assertEquals(driver.getCurrentUrl(), actualUrl);

        driver.get("http://localhost:" + this.port + "/api/user/login");
        Assertions.assertEquals(driver.getTitle(), "Login");

        uname = driver.findElement(By.id("inputUsername"));
        pwd = driver.findElement(By.id("inputPassword"));
        WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));

        uname.sendKeys(username);
        pwd.sendKeys(passwd);
        login.click();
        Assertions.assertEquals(driver.getTitle(), "Home");
    }
}
