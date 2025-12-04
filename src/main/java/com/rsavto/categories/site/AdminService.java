package com.rsavto.categories.site;

import com.rsavto.categories.core.data.Category;
import com.rsavto.categories.exception.MagazunException;
import com.rsavto.categories.site.admin.AdminHomePage;
import com.rsavto.categories.site.admin.LoginPage;
import com.rsavto.categories.util.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mykola Fedechko
 */
public class AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);

    private final static String RSAVTO_LOGIN_URL = "https://rsavto.com.ua/adm";
    private final static String RSAVTO_IMPORT_CATEGORIES_URL = "https://rsavto.com.ua/adm/?m=shop&type=catalog&vb=import";

    private final static String FORTUNA_LOGIN_URL = "https://fortunaavto.com.ua/adm";
    private final static String FORTUNA_IMPORT_URL = "https://fortunaavto.com.ua/adm/?m=shop&type=data";
    private final String categoriesFolder;

    public AdminService(final String categoriesFolder, final String webdriverFolderFolder) {
        this.categoriesFolder = categoriesFolder;
        System.setProperty("webdriver.chrome.driver", FileUtils.buildChromeDriverPath(webdriverFolderFolder));
    }

    public void uploadCategories(final List<Category> categories) {
        final var driver = getHeadlessChrome();
        final var homePage = login(driver);
        final var catalogPage = homePage.openCatalog();
        final var cataloguesPage = catalogPage.openCataloguesPage();
        final var importCategoriesPage = cataloguesPage.openImport();
        for (final var category : categories) {

            final var categoryFile = new File(FileUtils.buildCategoryFilePath(categoriesFolder, category).toString());
            if (!categoryFile.exists()) {
                LOG.warn("No file for " + category);
                continue;
            }

            if (category == Category.UNKNOWN && categoryFile.exists()) {
                LOG.warn("Unknown category has been found");
                continue;
            }

            LOG.info("Start uploading " + category.getFileName());
            importCategoriesPage.submitCategory(categoryFile.getPath(), String.valueOf(category.getId()));
            wait(5000);
            LOG.info("Category " + category.getFileName() + " has been successfully uploaded.");
            driver.get(RSAVTO_IMPORT_CATEGORIES_URL);
        }
        LOG.info("All categories have been uploaded.");
        driver.quit();
    }

    public double getRate() {
        final var driver = getHeadlessChrome();
        final var adminHomePage = login(driver);
        final var catalogPage = adminHomePage.openCatalog();
        final var additionalPage = catalogPage.openAdditional();
        return additionalPage.getEurRate();
    }

    private static WebDriver getHeadlessChrome() {
        final var options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("disable-gpu");
        final WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    private static AdminHomePage login(final WebDriver driver) {
        driver.get(RSAVTO_LOGIN_URL);
        final var loginPage = PageFactory.initElements(driver, LoginPage.class);
        final var homePage = loginPage.loginToRsAvto();
        wait(2000);
        return homePage;
    }

    private static void wait(final int mills) {
        try {
            Thread.sleep(mills);
        } catch (final InterruptedException exc) {
            throw new MagazunException("While waiting", exc);
        }
    }

}
