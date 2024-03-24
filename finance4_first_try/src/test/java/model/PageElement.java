package model;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class PageElement {

    public final String name;
    public final By locator;
    public final boolean required;

    public PageElement(String name, By locator, boolean required){
        this.name = name;
        this.locator = locator;
        this.required = required;
    }

    public PageElement(String name, By locator){
        this(name, locator, false);
    }

    /**
     * Gets the appropriate locator.
     *
     * @return the appropriate By locator of this element.
     */
    public By getLocator(){
        return this.locator;
    }

    public void sendKeys(Keys aReturn) {
    }
}
