package page;

import com.google.inject.Inject;
import model.PageElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchResultPage extends BasePage {

    private static  final PageElement headerComponent = new PageElement(
            "Search button ",
            By.xpath("//div[@id='contentHead']"),
            true);

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }


    public void openBookByPublishYear(String year){
    PageElement bookByYear = new PageElement(
            "Text present",
            By.xpath("//span[@class='publishedYear' and contains(text(),'First published in " + year + "')]//ancestor::div[@class='details']/div"),
            false);
       click(bookByYear);
    }
}
