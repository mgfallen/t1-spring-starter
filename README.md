 # Spring Boot Starter

В данном учебном проекте реализовано логирования всех HTTP запросов и ответов в Spring Boot, а также данная логика завернута в отдельный starter.

## Оглавление

1. [Возможности](#возможности)
2. [Установка](#установка)
3. [Конфигурация](#конфигурация)
   - [Пример конфигурации в application.properties](#пример-конфигурации-в-applicationproperties)
4. [Использование](#использование)
   - [Пример логов](#пример-логов)
5. [Тестирование](#тестирование)
6. [Разработка и сборка](#разработка-и-сборка)
   - [Сборка проекта](#сборка-проекта)

## Возможности

- Логирование входящих HTTP запросов: метод, URL, заголовки, параметры запроса.
- Логирование исходящих HTTP ответов: статус-код, заголовки, тело запроса, время выполнения.
- Логирование времени обработки запроса.

## Установка

Для использования этого стартер-пакета, добавьте следующую зависимость в ваш `pom.xml` (изменения НЕ залиты в maven central):

```xml
<dependency>
    <groupId>t1.holding</groupId>
    <artifactId>http-logging-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Конфигурация

Укажите в application.properties необходимый уровень логирования через следующие параметры:
- http.logging.logRequestMethod=true (default *true*)
- http.logging.logRequestUrl=true (default *true*)
- http.logging.logRequestHeaders=true (default *true*)
- http.logging.logRequestParams=true (default *true*)
- http.logging.logResponseHeaders=true (default *true*) 
- http.logging.logResponseStatus=true (default *true*)
- http.logging.logResponseBody=true (default *true*)
- http.logging.logDuration=true (default *true*)
  
Также можно указать, через какую стратегию вы хотите логировать http запросы с помощью параметра:
- http.logging.strategy=aspect   (default *filter*; avaible also *aspect* or *interceptor*)
  
## Использование

Добавьте стартер в зависимость, укажите соответствующие параметры логирования.

### Пример логов
- К примеру, обратимся к веб-серверу томката по адресу localhost:8080 с дефолтным по умолчанию уровнем логирования

Входящие запоросы никак не обрабатываются, поэтому в ответ закономерно получаем ошибку. Однако видно, что логируется все (был установлен максимальный уровень логирования)

2024-08-20T11:58:34.565+03:00  INFO 13940 --- [starter] [nio-8080-exec-5] t.h.starter.filter.HttpLoggingFilter     : Incoming Request: method=GET uri=/test headers=host=localhost:8080; connection=keep-alive; sec-ch-ua="Not)A;Brand";v="99", "Google Chrome";v="127", "Chromium";v="127"; sec-ch-ua-mobile=?0; sec-ch-ua-platform="Windows"; dnt=1; upgrade-insecure-requests=1; user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36; accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7; sec-fetch-site=none; sec-fetch-mode=navigate; sec-fetch-user=?1; sec-fetch-dest=document; accept-encoding=gzip, deflate, br, zstd; accept-language=ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7;  queryParams=null

2024-08-20T11:58:34.569+03:00  INFO 13940 --- [starter] [nio-8080-exec-5] t.h.starter.filter.HttpLoggingFilter     : Outgoing Response: status=404 headers=Vary=Origin; Vary=Origin; Vary=Origin;  duration=4ms

2024-08-20T11:58:34.570+03:00  INFO 13940 --- [starter] [nio-8080-exec-5] t.h.starter.aspect.HttpLoggingAspect     : Incoming Request: method=GET, uri=/error, headers=host=localhost:8080; connection=keep-alive; sec-ch-ua="Not)A;Brand";v="99", "Google Chrome";v="127", "Chromium";v="127"; sec-ch-ua-mobile=?0; sec-ch-ua-platform="Windows"; dnt=1; upgrade-insecure-requests=1; user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36; accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7; sec-fetch-site=none; sec-fetch-mode=navigate; sec-fetch-user=?1; sec-fetch-dest=document; accept-encoding=gzip, deflate, br, zstd; accept-language=ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7; 

2024-08-20T11:58:34.573+03:00  INFO 13940 --- [starter] [nio-8080-exec-5] t.h.starter.aspect.HttpLoggingAspect     : Outgoing Response: status=404, headers=Vary=Origin; Vary=Origin; Vary=Origin; , duration=2ms


- Теперь оставим только логирование методов входящих запросов: http.logging.logRequestMethod=true

2024-08-20T12:15:09.685+03:00  INFO 17936 --- [starter] [nio-8080-exec-2] t.h.starter.filter.HttpLoggingFilter     : Incoming Request: method=GET
2024-08-20T12:15:09.722+03:00  INFO 17936 --- [starter] [nio-8080-exec-2] t.h.starter.filter.HttpLoggingFilter     : Outgoing Response:


## Тестирование
Юнит тесты лежат внутри каталога src/test/java. Они проверяют корреткность работы логирования с помощью фильтров.
Запуск тестов с помощью команды
```
mvn test
or
./mvnw test
```

## Разработка и сборка
Для локальной сборки стартер-пакета выполните команду:
```
mvn clean package
or
./mvnw clean package
```

