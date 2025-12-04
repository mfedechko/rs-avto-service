package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


/**
 * @author Mykola Fedechko
 */
public class ImportCategoriesPage {

    private static final String VARIANT_ID = "2";
    private static final String CURRENCY_ID = "5";
    private static final String EXTRA_ID = "45";
    private static final String DESC_REFRESH_ID = "1";

    @FindBy(name = "importfile")
    private WebElement importFileField;

    @FindBy(name = "cat_import")
    private WebElement selectCategoryBox;

    @FindBy(name = "cat_vid")
    private WebElement selectVariantBox;

    @FindBy(name = "valuta")
    private WebElement currencySelectBox;

    @FindBy(name = "shab")
    private WebElement extraPriceSelectBox;

    @FindBy(name = "cat_des")
    private WebElement descSelectBox;

    @FindBy(xpath = "//td[@id='import_button']/input")
    private WebElement importButton;

    private final WebDriver driver;

    public ImportCategoriesPage(final WebDriver driver) {
        this.driver = driver;
    }

    public ImportResultPage submitCategory(final String filePath, final String categoryId) {
        importFileField.sendKeys(filePath);
        final var selectCategory = new Select(selectCategoryBox);
        selectCategory.selectByValue(categoryId);
        final var selectVariant = new Select(selectVariantBox);
        selectVariant.selectByValue(VARIANT_ID);
        final var currencySelect = new Select(currencySelectBox);
        currencySelect.selectByValue(CURRENCY_ID);
        final var selectExtraPrice = new Select(extraPriceSelectBox);
        selectExtraPrice.selectByValue(EXTRA_ID);
        final var descSelect = new Select(descSelectBox);
        descSelect.selectByValue(DESC_REFRESH_ID);
        importButton.click();
        return PageFactory.initElements(driver, ImportResultPage.class);
    }
}
