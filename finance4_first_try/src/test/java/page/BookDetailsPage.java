package page;

import model.PageElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookDetailsPage extends BasePage{
    private static  final PageElement authorInscription = new PageElement(
            "Author inscription ",
            By.xpath("(//a[@itemprop=\"author\"])[2]"));


    public BookDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getAuthorName()  {
        waitToBePresent(authorInscription,10);
        return getText(authorInscription);
    }
}
