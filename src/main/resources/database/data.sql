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

INSERT INTO Country()
VALUES (392, 400, "일본"),
       (156, 400, "중국"),
       (158, 400, "대만"),
       (764, 401, "태국"),
       (704, 401, "베트남"),
       (608, 401, "필리핀"),
       (702, 401, "싱가포르"),
       (360, 401, "인도네시아"),
       (458, 401, "말레이시아"),
       (316, 402, "괌"),
       (840, 402, "미국");

/* Category */
INSERT INTO Category(category_id, name)
VALUES (300, '식품'),
       (301, '의류'),
       (302, '잡화'),
       (303, '화장품');

/* Filter */

/* Notification */
