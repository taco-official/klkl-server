/* User */
INSERT INTO User(user_id, profile, name, gender, age, description, created_at)
VALUES (1, 'image/test.jpg', 'testUser', '남', 20, '테스트입니다.', now());

/* Product */
INSERT INTO Product(product_id, user_id, name, description, address, price, city_id, subcategory_id, currency_id,
                    created_at)
VALUES (1, 1, '곤약젤리', '탱글탱글 맛있는 곤약젤리', '신사이바시 메가돈키호테', 1000, 2, 3, 4, now());

/* Like */

/* Comment */

/* Region */

/* Category */
INSERT
INTO Category(category_id, name)
VALUES (300, '식품'),
       (301, '의류'),
       (302, '잡화'),
       (303, '화장품');

/* Filter */

/* Notification */
