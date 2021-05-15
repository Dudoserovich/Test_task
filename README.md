# Тестовое задание
>Тестовое задание написано на ЯП Java с использованием
>Maven, Selenium WebDriver и JUnit.
> + Изначально планировалось написать ещё и 
> нагрузочные тесты, но в официальной 
> документации Selenium настоятельно не рекомендуют делать
> это с помощью их средства
> + Так же планировалось проверить время 
      интерпретации проектов на мобильных 
      устройствах, так как <a>developers.google.com/speed/pagespeed/insights</a>
      показывал не очень хорошие результаты, но с помощью Selenium 
      это сделать нельзя
> + Просьба скачать google driver и изменить путь к нему в тестах
## Задание 1
Найти в сумме не менее 6 ошибок на двух 
из следующих проектах Дрома:
+ http://travel.drom.ru/ 
+ http://catalog.drom.ru/ 
+ http://reviews.drom.ru/

Необходимо в понятной для программиста 
форме описать суть найденных проблем. 
Предложения по улучшению системы не принимаются, 
только некорректное поведение.
Написать автотесты, желательно (но не обязательно) 
на Java, в отформатированном читаемом виде.
## Задание 2
### 2.1.
Дана страница продаж авто в России 
http://auto.drom.ru/ Отфильтровать 
список объявлений по параметрам:
+ фирма автомобиля Toyota;
+ марка автомобиля Harrier;
+ гибрид;
+ непроданные;
+ пробег авто больше 1 км;
+ год выпуска от 2007.

Проверять, что на первой и второй 
страницах результатов поиска в списке объявлений:
нет проданных авто (отсутствует перечеркнутый текст);
год авто не меньше 2007;
у каждого объявления в списке есть пробег.
### 2.2. 
Написать тест, который авторизует пользователя 
в разделе продаж авто http://auto.drom.ru/ и 
добавляет объявление о продаже авто в избранное.
### 2.3. 
Дана страница http://auto.drom.ru/ 
В сером блоке фильтрации по объявлениям 
есть выпадающие списки "Фирма" и "Модель". 
В скобках напротив каждого пункта выпадающего 
списка указано количество объявлений по данной 
фирме или модели. Необходимо написать скрипт, 
который выводит список из 20 фирм с наибольшим 
количеством поданных объявлений в Приморском крае. 
Данные вывести в виде таблицы с двумя столбцами как 
указано на примере ниже.

| Фирма | Количество объявлений |

| Toyota | 17211 |
