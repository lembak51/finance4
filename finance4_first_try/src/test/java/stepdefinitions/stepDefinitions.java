package stepdefinitions;



import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import page.BookDetailsPage;
import page.HomePage;
import page.SearchResultPage;



import java.util.concurrent.TimeUnit;

import static config.Config.baseUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static stepdefinitions.APICall.authorFromApi;

public class stepDefinitions  {
    //need separate  28 - 60 lines to another file for now i don't know how do it.
    // inherited can't use for it (cucumber blocked it)

    public WebDriver driver;
    public HomePage homePage;
    public SearchResultPage searchResultPage;
    public BookDetailsPage bookDetailsPage;

   @Before
    public void setupTestRun() {
        setUpDriver();
        initPages();
    }
   @After
    public void tearDown(){
        driver.close();
        driver.quit();


    }
    public void setUpDriver() {
        String chromeDriverPath = "src/test/driver/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    private void initPages(){
        homePage = new HomePage(driver);
        searchResultPage= new SearchResultPage(driver);
        bookDetailsPage = new BookDetailsPage(driver);
    }




    @Given("open home page")
    public void open_home_page(){
        driver.get(baseUrl);
    }

    @And("select english language")
    public void enSelected(){
        homePage.selectEnglishLanguage();
    }

    @When("user searches book by {string}")
    public void searchBookByName(String bookName) {
        homePage.searchBook(bookName);
    }

    @When("open book by {string}")
    public void clickOnBookByYear(String year){
      searchResultPage.openBookByPublishYear(year);
    }

    @Then("Compare author from api and ui")
    public void compareNameOfAuthor()  {
        assertThat(authorFromApi, equalTo(bookDetailsPage.getAuthorName()));


    }
}

