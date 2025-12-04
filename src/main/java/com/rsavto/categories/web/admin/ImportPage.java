package com.rsavto.categories.web.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


/**
 * @author Mykola Fedechko
 */
public class ImportPage {

    @FindBy(name = "importfile")
    private WebElement importFileField;

    @FindBy(name = "np")
    private WebElement selectSupplier;

    @FindBy(id = "import")
    private WebElement importButton;

    private final WebDriver driver;

    public ImportPage(final WebDriver driver) {
        this.driver = driver;
    }

    public ImportResultPage submitPrice(final String filePath, final String supplierId) {
        importFileField.sendKeys(filePath);
        final var selectBox = new Select(selectSupplier);
        selectBox.selectByValue(supplierId);
        importButton.click();
        return PageFactory.initElements(driver, ImportResultPage.class);
    }
}
