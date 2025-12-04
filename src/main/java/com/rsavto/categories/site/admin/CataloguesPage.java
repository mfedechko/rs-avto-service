package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class CataloguesPage {

    @FindBy(xpath = "//a[@href='?m=shop&type=catalog&vb=import']")
    private WebElement importCataloguesLink;


    private final WebDriver driver;

    public CataloguesPage(final WebDriver driver) {
        this.driver = driver;
    }

    public ImportCategoriesPage openImport() {
        importCataloguesLink.click();
        return PageFactory.initElements(driver, ImportCategoriesPage.class);
    }


}
