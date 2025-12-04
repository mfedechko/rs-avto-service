package com.rsavto.categories.web.supplier.asg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class MainPage {

    private final WebDriver driver;

    @FindBy(xpath = "//header/div[1]/a[1]")
    private WebElement generatePriceLink;

    public MainPage(final WebDriver driver) {
        this.driver = driver;
    }

    public GeneratePricePage openGeneratePricePage() {
        generatePriceLink.click();
        return PageFactory.initElements(driver, GeneratePricePage.class);
    }


}
