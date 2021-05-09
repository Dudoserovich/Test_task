package ru.Drom.travel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Tests {
    private ChromeDriver driver;
    final String SITE = "http://travel.drom.ru/";
    final int COUNT_LIST = 10; // количество проверяемых страниц

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
        //driver.quit();
    }

    // проверка отображения текущей странице в меню перехода между страницами
    private void checkLink(int num) {
        String inputMessage = Integer.toString(num);
        try {
            Assertions.assertTrue(driver.findElement(By.xpath("/html/body/div[4]/div[1]" +
                    "/div[1]/div/div/div[14]/div/span")).getText().contains(inputMessage));
        } catch (Throwable thrown) {
            System.out.println("Test nextList failed!");
            Assertions.fail(thrown + "\n" + "Встречена ссылка страницы самой на себя: " + inputMessage);
        }
    }

    @Test // проверка отображения текущих страниц
    public void nextList() {
        for (int i = 0; i < COUNT_LIST; ++i) {
            driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div/div[14]/div/a[" + (i + 2) + "]")).click();
            checkLink(i + 2);
        }
        System.out.println("Test nextList passed successfully!");
    }

    @Test // проверка налия поля с выбором регионов
    public void search() {
        int k = 0;
        try {
            int i = 0;
            while (i != COUNT_LIST) {
                if (i == 1)
                    k = i + 2;
                else k = i + 1;
                driver.findElement(By.id("id_russia_tags_list"));
                driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div/div[14]/div/a[" + k + "]")).click();
                ++i;
            }
        } catch (Throwable thrown) {
            System.out.println("Test search failed!");
            Assertions.fail(thrown + "\n\n" + "Отсутствует выбор регионов на странице: " + (k - 1));
        }
        System.out.println("Test search passed successfully!");
    }

}
