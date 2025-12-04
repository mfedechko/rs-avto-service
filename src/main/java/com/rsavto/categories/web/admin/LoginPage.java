package com.rsavto.categories.web.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class LoginPage {

    private static final String RSAVTO_LOGIN = "anton";
    private static final String RSAVTO_PASSWORD = "14151617a";

    private static final String FORTUNA_LOGIN = "fortuna";
    private static final String FORTUNA_PASSWORD = "fortuna2402442";

    @FindBy(id = "email")
    private WebElement loginField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "send")
    private WebElement loginButton;

    private final WebDriver driver;

    public LoginPage(final WebDriver driver) {
        this.driver = driver;
    }

    public HomePage loginToRsAvto() {
        loginField.sendKeys(RSAVTO_LOGIN);
        passwordField.sendKeys(RSAVTO_PASSWORD);
        loginButton.click();
        return PageFactory.initElements(driver, HomePage.class);
    }

    public HomePage loginToFortuna() {
        loginField.sendKeys(FORTUNA_LOGIN);
        passwordField.sendKeys(FORTUNA_PASSWORD);
        loginButton.click();
        return PageFactory.initElements(driver, HomePage.class);
    }

}
