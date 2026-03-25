# Product Inventory API

This project is a Spring Boot Product API with PostgreSQL persistence, Flyway-managed schema migrations, and OpenAPI docs.

## Data model

- `Category` 1 -> N `Product`
- `Product` N <-> N `Supplier` (join table `product_suppliers`)

## Migrations

Flyway scripts are in `src/main/resources/db/migration`:

- `V1__create_schema.sql`
- `V2__seed_data.sql`

## Run locally

1. Ensure PostgreSQL is running and `application.properties` credentials are valid.
2. Start the app:

```powershell
.\mvnw.cmd spring-boot:run
```

## Query low-stock products

Endpoint:

- `GET /api/products/low-stock?threshold=10`

This uses one efficient fetch query with `JOIN FETCH` for category and suppliers:

```jpql
SELECT DISTINCT p FROM Product p
JOIN FETCH p.category c
LEFT JOIN FETCH p.suppliers s
WHERE p.stockQuantity < :threshold
ORDER BY p.stockQuantity ASC
```

With SQL logging enabled (`logging.level.org.hibernate.SQL=DEBUG`), you can see the generated SQL in application logs.

