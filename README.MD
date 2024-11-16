# Fulfillment Centers API

REST API для управления продуктами в различных центрах выполнения (Fulfillment Centers).

## Технологии

- Java 21
- Spring Boot 3.2
- PostgreSQL
- Docker
- OpenAPI (Swagger)

## Запуск проекта

1. Клонировать репозиторий:

```bash
  git clone [url-репозитория]
```

2. Запустить PostgreSQL через Docker:

```bash
  docker-compose up -d
```

3. Запустить приложение:

```bash
  ./gradlew bootRun
```

## API

API доступно по адресу: `http://localhost:8080`  
Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui.html`

### Основные эндпоинты:

- `GET /api/products`  
  Получение всех продуктов.

- `GET /api/products/{id}`  
  Получение продукта по ID.

- `POST /api/products`  
  Создание нового продукта.  
  Пример тела запроса:
  ```json
  {
    "productId": "p1",
    "status": "SELLABLE",
    "fulfillmentCenter": "fc5",
    "quantity": 4,
    "value": 400.0
  }

- `PUT /api/products/{id}`  
  Обновление продукта.
  Пример тела запроса:
  ```json
  {
    "productId": "p1",
    "status": "UNFULFILLABLE",
    "fulfillmentCenter": "fc3",
    "quantity": 5,
    "value": 450.0
  }

- `DELETE /api/products/{id}`
  Удаление продукта по ID.

### Дополнительные эндпоинты:

- `GET /api/products/status/{status}`  
  Фильтрация продуктов по статусу (`SELLABLE`, `UNFULFILLABLE`, `INBOUND`).

- `GET /api/products/sellable/total-value`  
  Получение общей стоимости продуктов со статусом `SELLABLE`

- `GET /api/products/center/{center}/total-value`   
  Получение общей стоимости продуктов для указанного центра выполнения заказов.

- `GET /api/products/product-id/{productId}`
  Поиск продукта по идентификатору `productId`

## Анализ исходных данных

Проанализировав предоставленные исходные данные, можно сделать вывод, 
что поле `productId` не является уникальным. Один и тот же продукт (`productId`) 
с одинаковым статусом (`status`) может находиться в одном и том же центре выполнения 
заказов (`fulfillmentCenter`), но иметь разное количество (`quantity`) и стоимость 
за единицу (`value`).  
Предположительно, каждая запись в таблице представляет уникальную партию продукта.  
Уникальные записи в базе будут идентифицироваться с помощью поля `id` (автоматически генерируется системой).

## Модель данных

Product:

- id (Long) - уникальный идентификатор (генерируется автоматически)
- productId (String) - идентификатор продукта
- status (Enum) - статус продукта (SELLABLE, UNFULFILLABLE, INBOUND)
- fulfillmentCenter (String) - центр выполнения
- quantity (Integer) - количество
- value (Double) - стоимость

## Тестирование

Запуск тестов:

```bash
  ./gradlew test
```