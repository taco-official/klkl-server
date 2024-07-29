/* User */
INSERT INTO User(user_id, profile, name, gender, age, description, created_at)
VALUES (1, 'image/test.jpg', 'testUser', '남', 20, '테스트입니다.', now());

/* Product */

/* Like */

/* Comment */

/* Region */
INSERT INTO Region(region_id, name)
VALUES (400, '동북아시아'),
       (401, '동남아시아'),
       (402, '기타');

/* Category */
INSERT
INTO Category(category_id, name)
VALUES (300, '식품'),
       (301, '의류'),
       (302, '잡화'),
       (303, '화장품');

/* Filter */

/* Notification */
