package com.rsavto.categories.web.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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


    public OrdersPage openOrders() {
        ordersLink.click();
        return PageFactory.initElements(driver, OrdersPage.class);
    }

    public AdditionalPage openAdditional() {
        additionalsLink.click();
        return PageFactory.initElements(driver, AdditionalPage.class);
    }

    public ExtraPricesPage openExtraPricesPage() {
        extraPriceLink.click();
        return PageFactory.initElements(driver, ExtraPricesPage.class);
    }


    private static String getSupplierTextValue(final WebElement supplierRow) {
        return supplierRow.findElement(By.xpath("./td[1]/em/b")).getText();
    }

    private static String getSupplierExtraPrice(final WebElement supplierRow) {
        return supplierRow.findElement(By.xpath("./td[2]/span")).getText();
    }

    private static String getProcessedPartsInfo(final WebElement supplierRow) {
        try {
            return supplierRow.findElement(By.xpath("./td[6]/nobr/a")).getText();
        } catch (final WebDriverException exc) {
            return null;
        }
    }
}
