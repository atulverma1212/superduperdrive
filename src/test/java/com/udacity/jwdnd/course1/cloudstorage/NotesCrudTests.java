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
public class NotesCrudTests {

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

    /**
     *  creates a note, and verifies it is displayed
     *  edits the same note and verifies the changes
     *  deletes the same note and verifies that it is not displayed
     */
    @Test
    public void create_update_delete_note_flow() {
        String username = "av11";
        String password = "av11";
        signupAndLoginWithUserName(username, password);

        WebElement nav = driver.findElement(By.id("nav-notes-tab"));
        nav.click();

        WebDriverWait wait1 = new WebDriverWait(driver, 2);
        WebElement button;
        try {
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("createNoteButton")));
            button.click();
        } catch (TimeoutException ex) {
            nav.click();
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("createNoteButton")));
            button.click();
        }

        String actualTitle = "testTitle";
        WebElement title = wait1.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement desc = driver.findElement(By.id("note-description"));
        title.sendKeys(actualTitle);
        desc.sendKeys("testDescription");
        button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1")));
        button.click();

        driver.get("http://localhost:" + this.port + "/api/user/home");
        nav = driver.findElement(By.id("nav-notes-tab"));
        nav.click();

        int index = 1;
        WebElement baseTable = driver.findElement(By.id("notesTable"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        WebElement tr = tableRows.get(index);
        List<WebElement> thList = tr.findElements(By.tagName("th"));
        Boolean aBoolean = wait1.until(ExpectedConditions.textToBePresentInElement(thList.get(0), actualTitle));
        Assertions.assertTrue(aBoolean);
        String insertedTitle = thList.get(0).getText();
        Assertions.assertEquals(insertedTitle, actualTitle);

        /*
        Editting the same note
         */

        WebElement editNoteButton = driver.findElement(By.id("editNoteButton"));
        editNoteButton.click();

        String edittedText = "editted";
        title = wait1.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        desc = driver.findElement(By.id("note-description"));
        title.clear();
        title.sendKeys(edittedText);
        button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1")));
        button.click();

        driver.get("http://localhost:" + this.port + "/api/user/home");
        nav = driver.findElement(By.id("nav-notes-tab"));
        nav.click();

        baseTable = driver.findElement(By.id("notesTable"));
        tableRows = baseTable.findElements(By.tagName("tr"));
        tr = tableRows.get(index);
        thList = tr.findElements(By.tagName("th"));
        aBoolean = wait1.until(ExpectedConditions.textToBePresentInElement(thList.get(0), edittedText));
        Assertions.assertTrue(aBoolean);
        insertedTitle = thList.get(0).getText();
        Assertions.assertEquals(insertedTitle, edittedText);

        /*
        deleting the same note and verifying that it is not displayed
         */
        driver.get("http://localhost:" + this.port + "/api/user/home");
        nav = driver.findElement(By.id("nav-notes-tab"));
        nav.click();

        try {
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("deleteNoteButton")));
            button.click();
        } catch (TimeoutException ex) {
            nav.click();
            button = wait1.until(ExpectedConditions.elementToBeClickable(By.id("deleteNoteButton")));
            button.click();
        }

        baseTable = driver.findElement(By.id("notesTable"));
        tableRows = baseTable.findElements(By.tagName("tr"));
        Assertions.assertEquals(tableRows.size(), 1);
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
