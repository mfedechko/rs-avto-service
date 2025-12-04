package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class AdminHomePage {

    @FindBy(xpath = "//a[@href='?m=shop']")
    private WebElement catalogLink;

    private final WebDriver driver;

    public AdminHomePage(final WebDriver driver) {
        this.driver = driver;
    }

    public CatalogPage openCatalog() {
        catalogLink.click();
        return PageFactory.initElements(driver, CatalogPage.class);
    }
}
