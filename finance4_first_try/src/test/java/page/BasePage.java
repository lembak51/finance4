package page;

import com.google.common.base.Function;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import model.PageElement;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public  class BasePage {

    public WebDriver driver;
    protected Logger log;
    protected BasePage(WebDriver driver){
        this.driver = driver;
        PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/test/resources/log4j.properties");
        log = Logger.getLogger(this.getClass().getCanonicalName());
    }


    public String getText(PageElement pageElement){
        log.info("Getting text of element: " + pageElement.name);
        return this.find(pageElement).getText();
    }

    public void enterText(PageElement pageElement, String text){
        this.enterText(pageElement, text, true);
    }

    public void enterText(PageElement pageElement, String text, boolean clearField){
        log.info("Entering text \"" + text + "\" to element: " + pageElement.name);
        this.find(pageElement).click();
        if (clearField) {
            this.find(pageElement).clear();
        }
        this.find(pageElement).sendKeys(text);
    }

    public void click(PageElement pageElement){
        log.info("Clicking on element: " + pageElement.name);
        this.find(pageElement).click();
    }

    public WebElement find(By element){
        return this.driver.findElement(element);
    }

    public WebElement find(PageElement element){
        return this.find(element.getLocator());
    }

    public List<WebElement> findAll(By element){
        return this.driver.findElements(element);
    }

    public List<WebElement> findAll(PageElement element){
        return this.findAll(element.getLocator());
    }

    /**
     * Gets all elements on the page from the page object.
     *
     * @return a list of all the elements in this object.
     */
    public List<PageElement> getElements(){
        List<PageElement> elements = new ArrayList<PageElement>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().getSimpleName().equals("PageElement")) {
                try {
                    field.setAccessible(true);
                    elements.add((PageElement) field.get(PageElement.class));
                    field.setAccessible(false);
                } catch (IllegalAccessException ignored) {
                    System.out.println(ignored.toString());
                }
            }
        }
        return elements;
    }

    /**
     * Gets the required elements for this page.
     *
     * @return an array of the required elements on this page.
     */
    public List<PageElement> getRequiredElements(){
        ArrayList<PageElement> requiredElements = new ArrayList<PageElement>();
        for (PageElement ele : this.getElements()) {
            if (ele.required) {
                requiredElements.add(ele);
            }
        }
        return requiredElements;
    }

    /**
     * Returns an array of all the required elements that were not visible on the current page.
     *
     * @return all the elements in the array that were not visible.
     */
    public ArrayList<PageElement> getMissingRequiredElements(List<PageElement> requiredElements){
        ArrayList<PageElement> elements = new ArrayList<PageElement>(requiredElements);
        for (PageElement ele : requiredElements) {
            if (this.isElementPresent(ele)) {
                elements.remove(ele);
            } else {
                log.info("Missed required element: " + ele.name);
            }
        }
        return elements;
    }

    protected boolean allRequiredElementDisplayed(){
        log.info("Checking if all required elements present on page");
        return this.getMissingRequiredElements(this.getRequiredElements()).isEmpty();
    }

    public boolean isElementPresent(By element){
        boolean elementFound;
        try {
            this.find(element);
            elementFound = true;
        } catch (NoSuchElementException e) {
            elementFound = false;
        }
        return elementFound;
    }



    public boolean isElementPresent(PageElement element){
        return this.isElementPresent(element.getLocator());
    }

    /**
     * Waits for the specified timeout period for an element to be visible.
     *
     * @param element the By object representing the element to wait for.
     * @param timeout the length of time in seconds to wait, as an integer.
     */
    public void waitToBeVisible(final By element, int timeout){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoreAll(Arrays.asList(ElementNotVisibleException.class, NoSuchElementException.class, StaleElementReferenceException.class, WebDriverException.class));
        wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver input){
                return input.findElement(element);
            }
        });
    }



    public void waitToBeVisible(By element){
        this.waitToBeVisible(element, 30);
    }

    public void waitToBeVisible(PageElement element, int timeout){
        this.waitToBeVisible(element.getLocator(), timeout);
    }

    public void waitToBeVisible(PageElement element){
        this.waitToBeVisible(element.getLocator(), 30);
    }


    /**
     * Waits for the specified timeout period for an element to be invisible.
     *
     * @param element the By object representing the element to wait for.
     * @param timeout the length of time in seconds to wait, as an integer.
     */
    public void waitToBeInvisible(By element, int timeout){
        WebDriverWait wait = new WebDriverWait(this.driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    public void waitToBeInvisible(By element){
        this.waitToBeInvisible(element, 60);
    }

    public void waitToBeInvisible(PageElement element, int timeout){
        log.info("Wait to be invisible " + element.name + " ...");
        this.waitToBeInvisible(element.getLocator(), timeout);
    }

    public void waitToBeInvisible(PageElement element){
        this.waitToBeInvisible(element.getLocator(), 60);
    }

    /**
     * Waits for the specified timeout period for an element to be clickable.
     *
     * @param element the By object representing the element to wait for.
     * @param timeout the length of time in seconds to wait, as an integer.
     */
    public void waitToBeClickable(By element, int timeout){
        WebDriverWait wait = new WebDriverWait(this.driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitToBeClickable(By element){
        this.waitToBeClickable(element, 30);
    }

    public void waitToBeClickable(PageElement element, int timeout){
        this.waitToBeClickable(element.getLocator(), timeout);
    }

    public void waitToBeClickable(PageElement element){
        this.waitToBeClickable(element.getLocator(), 30);
    }

    /**
     * Waits for the specified timeout period for an element to be present.
     *
     * @param element the By object representing the element to wait for.
     * @param timeout the length of time in seconds to wait, as an integer.
     */
    public void waitToBePresent(By element, int timeout){
        WebDriverWait wait = new WebDriverWait(this.driver, timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    public void waitToBePresent(By element){
        this.waitToBePresent(element, 30);
    }

    public void waitToBePresent(PageElement element, int timeout){
        this.waitToBePresent(element.getLocator(), timeout);
    }

    public void waitToBePresent(PageElement element){
        this.waitToBePresent(element.getLocator(), 30);
    }

    /**
     * Select specific item from drop down
     *
     * @param selectBox the Select object representing the drop down.
     * @param value     the String object that find in the drop down
     */
    public void selectFromSelectBox(PageElement selectBox, String value){
        log.info("Selecting \"" + value + "\" from: " + selectBox.name);
        Select dropdown = new Select(find(selectBox));
        dropdown.selectByVisibleText(value);
    }



    /**
     * Waits default(1 sec) timeout period for an alert to be present.
     */
    public void waitToBeAlertPresent(){
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (NoAlertPresentException ex) {
            log.info("No Alert Present");
        }
    }

    /**
     * Waits for the specified timeout period for an alert to be present.
     *
     * @param timeout the Int object in the second that need to wait
     */
    public void waitToBeAlertPresent(int timeout){
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (NoAlertPresentException ex) {
            log.info("No Alert Present");
        }
    }

    /**
     * Get text from alert
     *
     * @return Alert Text the String object
     */
    protected String getTextFromAlert(){
        waitToBeAlertPresent();
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        log.info("Alert Text " + alertText);
        return alertText;
    }

    /**
     * Accept alert
     */
    public void acceptAlert(){
        waitToBeAlertPresent();
        driver.switchTo().alert().accept();
    }

    /**
     * Waits for the specified timeout period for an element to be present.
     *
     * @param element   the By object representing the element to wait for.
     * @param attribute the String object representing the value of an attribute of the element
     * @return Attribute value the String object
     */
    protected String getAttribute(PageElement element, String attribute){
        log.info("Get attribute " + attribute + " from element " + element.name);
        return driver.findElement(element.locator).getAttribute(attribute);
    }

    /**
     * Waits default(1 sec) timeout period
     */
    protected void waitUntilPageLoad(){
        //TODO implementation of JS to that
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits default(1 sec) timeout period
     *
     * @param timeout the Int object representing the value in second that need to wait
     */
    protected void waitUntilPageLoad(int timeout){
        //TODO implementation of JS to that
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Switch between tab in browser
     *
     * @param numberTab the Integer object representing the tab
     */
    public void switchToTab(int numberTab){
        waitUntilPageLoad();
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(numberTab));
        } catch (NegativeArraySizeException e) {
            log.info("New tab not open");
        }
    }

    /**
     * Upload Photo from the specified path
     *
     * @param pathToPhoto object representing the path to the file
     */
    public void uploadNewPhoto(String pathToPhoto){
        WebElement frame = driver.switchTo().activeElement();
        frame.sendKeys(pathToPhoto);
    }

    /**
     * Compare expected and actual text of alert
     *
     * @param expectedText the String object representing of the expected alert text
     */
    public boolean isAlertTextAsExpected(String expectedText){
        waitToBeAlertPresent(1);
        String actualText = getTextFromAlert();
        log.info("Actual Alert Text: " + actualText + ".Expected: " + expectedText);
        return actualText.equals(expectedText);
    }

}