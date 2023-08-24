import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class YandexTestUI {

    WebDriver driver;
    WebDriverWait wait;

    public static final String URL_MAIN = "https://360.yandex.ru/";
    public static final String URL_DISC = "https://disk.yandex.ru/client/disk";
    public static final String LOGIN = "popk3n";
    public static final String PASSWORD = "wAMVqoC";

    // Локаторы авторизации
    private static final By logBtn = By.xpath("//*[@id='header-login-button']"); // кнопка "Войти" на стартовой
    private static final By loginInput = By.xpath("//*[@id='passp-field-login']"); // поле ввода логина
    private static final By logSubmitBtn = By.xpath("//*[@id='passp:sign-in']"); // кнопка "Войти" после ввода логина/пароля
    private static final By passwordInput = By.xpath("//*[@id='passp-field-passwd']"); // поле ввода пароля

    // Локаторы работы с диском
    private static final By createBtn = By.xpath("(//button)[2]"); // кнопка "Создать"
    private static final By createFolder = By.xpath("//*[@aria-label='Папку']"); // создать папку
    private static final By inputNameFolder = By.xpath("(//input)[3]"); // инпут для ввода названия папки
    private static final By saveFolderBtn = By.xpath("//*[contains(@class, 'confirmation-dialog__button_submit')]"); // сохранить папку
    private static final By fileLocator = By.xpath("//*[@aria-label='Файл для копирования.docx']"); // локатор файла для копирования
    private static final By copyCommand = By.xpath("//*[@data-key='item-6']"); // команда "Копировать"
    private static final By folderToCopy = By.xpath("(//*[@title='Тестовая папка'])[2]"); // выбор тестовой папки для копирования
    private static final By folderToCopyBtn = By.xpath("(//*[@class='dialog__wrap']//button)[3]"); // кнопка "Копировать" в диалоговом окне
    private static final By folderLocator = By.xpath("(//*[@title='Тестовая папка'])[1]"); // тестовая папка в списке всех файлов
    private static final By fileIsExist = By.xpath("//*[@title='Файл для копирования.docx']"); // наличие файла в папке
    private static final By backBtn = By.xpath("//*[@id='/disk']"); // возврат в корневую папку
    private static final By folderToRemove = By.xpath("(//*[@title='Тестовая папка'])[1]"); // выбор тестовой папки для копирования
    private static final By removeFolder = By.xpath("//*[@data-key='item-6']"); // удаление тестовой папки
    private static final By dialogWindowNewFolder = By.xpath("//*[@class='dialog__wrap']"); // диалоговое окно создания новой папки

    // Локаторы разлогина
    private static final By userPic = By.xpath("(//*[contains(@class, 'user-pic')])[1]"); // профиль
    private static final By logOut = By.xpath("//*[@aria-label='Выйти из аккаунта']"); // выход из аккаунта
    private static final By newLogTitle = By.xpath("//h1"); // заголовок формы авторизации
    private static final By alert = By.xpath("//*[contains(@class, 'notifications__item_trash')]"); // уведомление об удалении папки


    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws IOException {
        // Для снятия скриншота
        var sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(sourceFile, new File(
                "./screenshots/screenshot.png"));
        driver.quit();
    }

    @Test
    public void copyFile() throws InterruptedException {

        Actions actions = new Actions(driver);

        // Авторизация
        driver.navigate().to(URL_MAIN);
        driver.findElement(logBtn).click();
        driver.findElement(loginInput).sendKeys(LOGIN);
        driver.findElement(logSubmitBtn).click();
        Thread.sleep(1000);
        driver.findElement(passwordInput).sendKeys(PASSWORD);
        driver.findElement(logSubmitBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(logSubmitBtn));

        // Работа с файлом в ЯндексДиске
        driver.navigate().to(URL_DISC);
        driver.findElement(createBtn).click();
        driver.findElement(createFolder).click();
        Thread.sleep(1000);
        driver.findElement(inputNameFolder).sendKeys("Тестовая папка");
        driver.findElement(saveFolderBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogWindowNewFolder));
        WebElement file = driver.findElement(fileLocator);
        var expectedName = file.getText();
        actions.contextClick(file).perform();
        driver.findElement(copyCommand).click();
        driver.findElement(folderToCopy).click();
        driver.findElement(folderToCopyBtn).click();

        // Обнаружение элемента в папке
        WebElement folder = driver.findElement(folderLocator);
        actions.doubleClick(folder).perform();
        WebElement fileInFolder = driver.findElement(fileIsExist);
        var actualName = fileInFolder.getText();
        Assert.assertEquals("Название файла не соответствует ожидаемому", expectedName,
                actualName);

        // Удаление тестовой папки
        driver.findElement(backBtn).click();
        WebElement folderInCatalog = driver.findElement(folderToRemove);
        actions.contextClick(folderInCatalog).perform();
        driver.findElement(removeFolder).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(alert));

        // Выход из аккаунта
        driver.findElement(userPic).click();
        driver.findElement(logOut).click();

        // Проверка разлогина
        var actualTitle = driver.findElement(newLogTitle).getText();
        Assert.assertEquals("Разлогин не произошел", "Войдите, чтобы продолжить", actualTitle);
    }
}