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
INSERT INTO Region(region_id, name)
VALUES (400, '동북아시아'),
       (401, '동남아시아'),
       (402, '기타');

INSERT INTO Country(country_id, region_id, name, flag, photo, currency_id)
VALUES (403, 400, '일본', 'image/sample', 'image/sample', 1),
       (404, 400, '중국', 'image/sample', 'image/sample', 1),
       (405, 400, '대만', 'image/sample', 'image/sample', 1),
       (406, 401, '태국', 'image/sample', 'image/sample', 1),
       (407, 401, '베트남', 'image/sample', 'image/sample', 1),
       (408, 401, '필리핀', 'image/sample', 'image/sample', 1),
       (409, 401, '싱가포르', 'image/sample', 'image/sample', 1),
       (410, 401, '인도네시아', 'image/sample', 'image/sample', 1),
       (411, 401, '말레이시아', 'image/sample', 'image/sample', 1),
       (412, 402, '괌', 'image/sample', 'image/sample', 1),
       (413, 402, '미국', 'image/sample', 'image/sample', 1);

/* Category */
INSERT
INTO Category(category_id, name)
VALUES (300, '식품'),
       (301, '의류'),
       (302, '잡화'),
       (303, '화장품');

INSERT INTO Subcategory (subcategory_id, name, category_id)
VALUES
    -- 식품 카테고리 (category_id: 300)
    (310, '라면 및 즉석식품', 300),
    (311, '스낵 및 과자', 300),
    (312, '조미료 및 소스', 300),
    (313, '보충제 및 건강식품', 300),
    (314, '음료 및 차', 300),
    (315, '주류', 300),

    -- 의류 카테고리 (category_id: 301)
    (320, '상의', 301),
    (321, '하의', 301),
    (322, '아우터', 301),
    (323, '원피스', 301),
    (324, '신발', 301),
    (325, '액세사리', 301),
    (326, '쥬얼리', 301),

    -- 잡화 카테고리 (category_id: 302)
    (330, '일반의약품', 302),
    (331, '주방잡화', 302),
    (332, '욕실잡화', 302),
    (333, '문구 및 완구', 302),

    -- 화장품 카테고리 (category_id: 303)
    (340, '스킨케어', 303),
    (341, '메이크업', 303),
    (342, '헤어케어', 303),
    (343, '바디케어', 303),
    (344, '위생용품', 303);
/* Filter */

/* Notification */
