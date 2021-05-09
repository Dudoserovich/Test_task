package ru.drom.auto;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

// Задание 2.3
class Marks {
    public static final int COUNT_MARKS = 132;
    public static int[] count = new int[COUNT_MARKS];
    public static String[] marks = new String[COUNT_MARKS];
}

public class Script extends Marks {

    private static ChromeDriver driver;

    public static int simpleSearch(int[] vec, int key) {
        for (int i = 0; i < vec.length; ++i) {
            if (vec[i] == key)
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        // прописываем путь драйвера
        System.setProperty("webdriver.chrome.driver", "/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // открываем ссылку в хроме
        driver.get("https://auto.drom.ru/");

        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[1]/div[1]")).click();

        String[] new_marks = new String[COUNT_MARKS];
        int k;
        for (int i = 8; i < COUNT_MARKS + 7; ++i) {
            k = i - 8;
            new_marks[k] = driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]" +
                    "/div[3]/form/div[1]/div[1]/div[2]/div[" + String.valueOf(i + 1) + "]")).getText();
            //System.out.println(new_marks[k]);
            // заполнение марок
            if ((new_marks[k].indexOf('(') != -1)) {
                // записываем название марки отдельно
                char[] help_marks = new char[new_marks[k].indexOf('(') - 1];
                new_marks[k].getChars(0, (new_marks[k].indexOf('(') - 1), help_marks, 0);
                marks[k] = new String(help_marks);

                char[] help_count = new char[(new_marks[k].length() - new_marks[k].indexOf('(') - 2)];
                new_marks[k].getChars((new_marks[k].indexOf('(') + 1), (new_marks[k].indexOf(')')), help_count, 0);
                count[k] = 0;
                for (int j = 0; j < help_count.length; ++j) {
                    count[k] += Integer.parseInt(String.valueOf(help_count[j])) * Math.pow(10, help_count.length - j - 1);
                }
            } else {
                marks[k] = new_marks[k];
                count[k] = 0;
            }

            //System.out.println(marks[k]);
            //System.out.println(count[k]);
        }

        driver.findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div[1]/div[3]/form/div[1]/div[1]/div[1]")).click();

        // отсортировываем скопированный массив
        int[] new_count = new int[COUNT_MARKS];
        new_count = Arrays.copyOf(count, COUNT_MARKS);
        Arrays.sort(new_count);

        // выводим 20 марок с самым большим количеством объявлений
        int index = 0;
        System.out.format("%1c%13s%12s", '|', "Фирма", "| " + "Кол. объяв." + " |");
        System.out.println("");
        for (int i = COUNT_MARKS - 20; i < COUNT_MARKS; i++) {
            // поиск долгий, но это быстрее чем сортировать 2 массива одновременно
            index = simpleSearch(count, new_count[i]);
            System.out.format("%1c%13s%12s", '|', marks[index], "| " + count[index] + " |");
            System.out.println("");
        }

    }
}
