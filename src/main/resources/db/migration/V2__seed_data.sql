INSERT INTO categories (name, description) VALUES
('ELECTRONICS', 'Electronic products and accessories'),
('BOOKS', 'Printed and digital books'),
('FOOD', 'Groceries and beverages');

INSERT INTO suppliers (name, contact_email) VALUES
('TechSource Lanka', 'sales@techsource.lk'),
('ReadMore Distributors', 'contact@readmore.lk'),
('FreshLine Traders', 'orders@freshline.lk');

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at) VALUES
('Wireless Mouse', 'Ergonomic wireless mouse', 29.99, 8, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mechanical Keyboard', 'RGB mechanical keyboard', 89.00, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring in Action', 'Comprehensive Spring guide', 45.50, 6, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Green Tea Pack', 'Organic tea - 100 bags', 12.30, 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product_suppliers (product_id, supplier_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3),
(1, 2);

