package ru.Drom.auto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

// Задания 2.1 и 2.2
public class TestsFilterAndAuthorization {
    final int COUNT_LIST = 2;
    final String TEST_FILTER_FAILED = "Filtering test failed!";
    private ChromeDriver driver;
    final String SITE = "https://auto.drom.ru/";
    final String EMAIL = "mynametesterovshik@gmail.com";
    final String PASSWORD = "wdxfk47w";

    // объявляем общий код запускаемый для каждого теста по отдельности
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

    // выбор из списка
    private void select(String click, String set) {
        driver.findElement(By.xpath(click)).click();
        driver.findElement(By.xpath(set)).click();
    }

    private void fillingForm() {
        System.out.println("Program started selected");
        // Выбор марки машины
        select("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[1]/div[1]/button",
                "/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[1]/div[2]/div[3]");

        // Выбор модели машины
        select("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[2]/div[1]/button",
                "/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[2]/div[2]/div[89]");

        // Выбор гибрида
        select("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[2]/div[3]/div[2]/div[1]/button",
                "/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[2]/div[3]/div[2]/div[2]/div[6]");

        // Непроданные
        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/" +
                "div[3]/div[3]/div[1]/label")).click();

        // Расширенный поиск
        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/" +
                "form/div[4]/div[2]/span")).click();

        // пробег более 1км, введём 1001
        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[4]/" +
                "div[3]/div[3]/div[1]/div/div[1]/div/div/div[1]/input")).sendKeys("1001");

        // год от 2007
        select("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/" +
                "div[2]/div[2]/div[1]/div[1]/button", "/html/body/div[2]/" +
                "div[5]/div[1]/div[1]/div[3]/form/div[2]/div[2]/div[1]/div[2]/div[17]");

        // жмём показать
        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]" +
                "/div[3]/form/div[5]/div[3]")).click();

        System.out.println("Program finished selected");
    }

    @Test // тест фильтрации (2.1)
    public void filter() {

        fillingForm();

        // объявляем массивы строк, в которых будем хранить данные об объявлениях
        String listHead[] = new String[20];
        String listGo[] = new String[20];

        System.out.println("Program started parsing");
        // парсинг страниц
        int num;
        for (int k = 0; k < COUNT_LIST; ++k) {
            String xpathHead, xpathGo;
            String baseXpath = "/html/body/div[2]/div[4]/div[1]/div[1]/div[4]/div/div[2]/";
            // ищем проданные машины
            try {
                driver.findElement(By.xpath(baseXpath)).
                        findElements(By.className("css-yhqutu e1vivdbi0")).forEach(it -> {
                    System.out.println(TEST_FILTER_FAILED);
                    Assertions.fail("Была найдена проданная машина: " + it.getText() + '\n');
                });
            } catch (Throwable thrown) {
                Assertions.assertNotEquals("", thrown.getMessage());
            }

            for (int i = 0; i < listHead.length; ++i) {
                xpathHead = baseXpath + "a[" + String.valueOf(i + 1) + "]" + "/div[2]/div[1]/div/span";
                // заголовки с годами
                listHead[i] = driver.findElement(By.xpath(xpathHead)).getText();

                // текст с пробегом
                xpathGo = baseXpath + "a[" + String.valueOf(i + 1) + "]" + "/div[2]/div[2]/span[5]";
                listGo[i] = driver.findElement(By.xpath(xpathGo)).getText();
            }

            int index;
            String year;
            for (int i = 0; i < listHead.length; ++i) {
                // записываем все года
                index = listHead[i].indexOf(',') + 2;
                year = "";
                for (int j = index; j < listHead[i].length(); ++j) {
                    year += listHead[i].charAt(j);
                }

                // проверка года
                if (Integer.parseInt(year) < 2007) {
                    System.out.println(TEST_FILTER_FAILED);
                    Assertions.fail("Встречен год меньше 2007 в объявлении: " + listHead[i] + '\n');
                }

                // проверяем пробег
                if (listGo[i] == null) {
                    System.out.println(TEST_FILTER_FAILED);
                    Assertions.fail("Нет пробега в объявлении: " + listHead[i] + '\n');
                }
            }
            if (k == 1)
                num = k + 2;
            else
                num = k + 1;
            driver.findElement(By.xpath("/html/body/div[2]/div[4]/div[1]/" +
                    "div[1]/div[4]/div/div[3]/div/div/div[" + num + "]")).click();
        }

        System.out.println("Program finished parsing");
        System.out.println("Filtering test passed successfully!");
    }

    // активируем поле и заполняем его
    private void writeText(String click, String where_write, String key) {
        driver.findElement(By.xpath(click)).click();
        driver.findElement(By.xpath(where_write)).sendKeys(key);
    }

    @Test // тест авторизации (2.2)
    public void authorization() {
        try {
            // переходим в раздел авторизации
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[3]")).click();

            // вводим логин
            writeText("//*[@id=\"signForm\"]/form/div[2]/div[2]", "//*[@id=\"sign\"]", EMAIL);

            // вводим пароль
            writeText("//*[@id=\"signForm\"]/form/div[3]/div/div[2]", "//*[@id=\"password\"]", PASSWORD);

            driver.findElement(By.id("signbutton")).click();

            // добавляем случайную объявление в избранное
            int rand = (int) (Math.random() * (20 - 1) + 1);
            driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[10]/div/div[2]/a[" + rand + "]" + "/div[3]/div[3]/div")).click();

        } catch (Throwable thrown) {
            Assertions.fail("Authorization test failed: " + thrown.getMessage());
        }
        System.out.println("Authorization test passed successfully!");
    }

}
