package com.rsavto.categories.web.supplier.asg;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Mykola Fedechko
 */
public class GeneratePricePage {

    @FindBy(xpath = "//form[@action='http://new-online.asg.ua/ua/formatting-price/store']/div/div[2]/div/button")
    private WebElement generatePriceButton;

    @FindBy(xpath = "//table[@id='dataTableBuilder']/tbody/tr[1]/td[1]/span")
    private WebElement generationDate;

    @FindBy(xpath = "//table[@id='dataTableBuilder']/tbody/tr[1]/td[6]/span")
    private WebElement statusColumn;

    @FindBy(xpath = "//table[@id='dataTableBuilder']/tbody/tr[1]/td[8]/span/a")
    private WebElement downloadButton;

    public boolean isPriceUpToDate() throws ParseException {
        final var dateStr = generationDate.getText();
        final var latestDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(dateStr);
        final var formatter = new SimpleDateFormat("dd/MM/yyyy");
        final var today = formatter.parse(formatter.format(new Date()));
        return today.before(latestDate);
    }

    public void generatePrice() {
        generatePriceButton.click();
    }

    public boolean isPriceReady() {
        return statusColumn.getText().equals("Готовий");
    }

    public void downloadPrice() {
        downloadButton.click();
    }


}
