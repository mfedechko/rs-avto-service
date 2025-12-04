package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @author Mykola Fedechko
 */
public class CatalogPage {

    @FindBy(xpath = "//a[@href='?m=shop&type=data']")
    private WebElement importLink;

    @FindBy(xpath = "//a[@href='?m=shop&type=catalog']")
    private WebElement cataloguesLink;

    @FindBy(xpath = "//a[@href='?m=shop&type=dop']")
    private WebElement additionalsLink;

    @FindBy(xpath = "//a[@href='/adm/?m=shop&type=pos&nacenka']")
    private WebElement extraPriceLink;

    @FindBy(xpath = "//div[@id='postav_box_all']/table[2]/tbody/tr")
    private List<WebElement> supplierRows;

    @FindBy(xpath = "//a[@href='?m=shop&type=zakaz']")
    private WebElement ordersLink;

    private final WebDriver driver;

    public CatalogPage(final WebDriver driver) {
        this.driver = driver;
    }

    public ImportPage openImport() {
        importLink.click();
        return PageFactory.initElements(driver, ImportPage.class);
    }

    public AdditionalPage openAdditional() {
        additionalsLink.click();
        return PageFactory.initElements(driver, AdditionalPage.class);
    }

    public CataloguesPage openCataloguesPage() {
        cataloguesLink.click();
        return PageFactory.initElements(driver, CataloguesPage.class);
    }


}
