package page;

import com.google.inject.Inject;
import model.PageElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage{

    private static  final PageElement languageDropDownList = new PageElement(
            "Language drop down",
            By.xpath("//div[@class='language-component header-dropdown']"));

    private static  final PageElement enDropDownOpt = new PageElement(
            "English drop down option",
            By.xpath("//a[@data-lang-id='en']//parent::li"));

    private static  final PageElement searchField = new PageElement(
            "Search field ",
            By.xpath("//input[@aria-label='Search']"));

    private static  final PageElement searchButton = new PageElement(
            "Search button ",
            By.xpath("//input[@class='search-bar-submit']"));

    public HomePage(WebDriver driver) {
        super(driver);
    }


    public void selectEnglishLanguage(){
        click(languageDropDownList);
        click(enDropDownOpt);
    }

    public void searchBook(String text){
       waitToBeClickable(searchField);
       enterText(searchField,text);
       click(searchButton);
    }
}
