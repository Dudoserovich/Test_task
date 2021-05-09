package ru.Drom.reviews;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class ValidFields {
    public ChromeDriver driver;
    final String SITE = "https://www.drom.ru/reviews/";
    final String INVALID_RU_EMAIL = "nmmn@ггг.ру";
    final String INVALID_DOMAIN_EMAIL = "nmmn@google";
    final String INVALID_FIRST_DOMAIN_EMAIL = "77-m_m@h.h";
    final String ERR_FIRST_DOMAIN_EMAIL = "Был задан домен первого уровня с одним символом" +
            "после точки и не было встречено " +
            "сообщения об ошибке: " + INVALID_DOMAIN_EMAIL;
    final double NO_ERR_EMAIL = 442.6666564941406;
    final String ERR_RU_EMAIL = "Были встречены символы из кирилицы в доменной " +
            "части и не было встречено сообщения об ошибке: " + INVALID_RU_EMAIL;
    final String ERR_DOMAIN_EMAIL = "Был задан домен нулевого уровня и не было встречено " +
            "сообщения об ошибке: " + INVALID_DOMAIN_EMAIL;

    @BeforeEach
    public void setUp() {
        // прописываем путь драйвера
        System.setProperty("webdriver.chrome.driver", "/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // открываем ссылку в хроме
        driver.get(SITE);
    }

    @AfterEach
    public void close() {
        driver.quit();
    }

    // переход в раздел добавления отзыва
    private void clickAddReview() {
        driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[1]/" +
                "div/div[4]/div[2]/div[2]/div/div[1]/a")).click();
    }

    // общая часть проверки почты
    private void basicEmailActions(String email, String errMessage) {
        clickAddReview();
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(email);
        driver.findElement(By.xpath("//*[@id=\"submitFormButton\"]")).click();

        // с помощью местоположения прокрутки проверяем вылезла ли ошибка у почты.
        // используем именно этот метод, потому что непонятно чем явлется это самое сообщение
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        double location_scrollY = (double) executor.executeScript("return window.pageYOffset;");
        //System.out.println(location_scrollY);
        if (location_scrollY == NO_ERR_EMAIL) {
            System.out.println("Test with email failed!");
            Assertions.fail(errMessage);
        }
        //js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    @Test // проверка на русские символы в доменной части
    public void emailValidRu() {
        basicEmailActions(INVALID_RU_EMAIL, ERR_RU_EMAIL);
        System.out.println("Test emailValidRu passed successfully!");
    }

    @Test // проверка на домен нулевого уровня
    public void emailValidDomen() {
        basicEmailActions(INVALID_DOMAIN_EMAIL, ERR_DOMAIN_EMAIL);
        System.out.println("Test emailValidDomen passed successfully!");
    }

    @Test // один символ после точки
    public void emailValidFirstDomen() {
        basicEmailActions(INVALID_FIRST_DOMAIN_EMAIL, ERR_FIRST_DOMAIN_EMAIL);
        System.out.println("Test emailValidDomen passed successfully!");
    }

    // проверка одного списка маркеров
    private void basicListsActions(String thisXpath, int count) {
        int[] xLocation = new int[count + 1];
        Point location = driver.findElement(By.xpath(thisXpath + "/div")).getLocation();
        xLocation[0] = location.x;
        for (int i = 1; i < count + 1; ++i) {
            location = driver.findElement(By.xpath(thisXpath + "/label[" + i + "]")).getLocation();
            xLocation[i] = location.x;
        }

        // используем самое простое сравнение, потому что количество элементов всегда не больше 3х
        int i = 0;
        while (i != xLocation.length) {
            if ((i + 1) != xLocation.length && xLocation[i] != xLocation[i + 1]) {
                System.out.println("Test lists failed!");
                Assertions.fail("В секции " + driver.findElement(By.xpath(thisXpath + "/div")).getText() +
                        " обнаружено неправильное отображение блока, разные параметры x");
            }
            ++i;
        }
    }

    @Test // производится проверка всех секций с маркером
    public void lists() {
        clickAddReview();
        driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div[1]/a[1]")).click();

        basicListsActions("/html/body/div[4]/div[1]/div[1]/div/div[2]/form/div[2]/div[3]/div[1]/div", 2);
        for (int i = 0; i < 3; ++i) {
            basicListsActions("/html/body/div[4]/div[1]/div[1]/div/div[2]/form/div[2]/div[3]/div[" + (i + 2) + "]/div", 3);
        }
        System.out.println("Test lists passed successfully!");
    }

}
