/* User */
INSERT INTO User(user_id, profile, name, gender, age, description, created_at)
VALUES (1, 'image/test.jpg', 'testUser', '남', 20, '테스트입니다.', now());

/* Product */
INSERT INTO Product(product_id, user_id, name, description, address, price, city_id, subcategory_id, currency_id,
                    created_at)
VALUES (1, 1, '곤약젤리', '탱글탱글 맛있는 곤약젤리', '신사이바시 메가돈키호테', 1000, 2, 3, 4, now());

/* Like */

/* Comment */
INSERT INTO Comment(comment_id, product_id, user_id, content, created_at)
VALUES (500, 1, 1, '이거 정말 맛있는데 표현할 방법이 읎네.', now()),
       (501, 1, 1, '이거 정말 맛없는데 표현할 방법이 읎네.', now()),
       (502, 1, 1, '이거 정말 좋은데 표현할 방법이 읎네.', now());


/* Region */
INSERT
INTO Region(region_id, name)
VALUES (400, '동북아시아'),
       (401, '동남아시아'),
       (402, '기타');

INSERT INTO Currency (currency_id, code, flag)
VALUES (438, 'JPY', 'japan_flag.png'),
       (439, 'CNY', 'china_flag.png'),
       (440, 'TWD', 'taiwan_flag.png'),
       (441, 'THB', 'thailand_flag.png'),
       (442, 'VND', 'vietnam_flag.png'),
       (443, 'PHP', 'philippines_flag.png'),
       (444, 'SGD', 'singapore_flag.png'),
       (445, 'IDR', 'indonesia_flag.png'),
       (446, 'MYR', 'malaysia_flag.png'),
       (447, 'USD', 'usa_flag.png');

INSERT INTO Country(country_id, region_id, name, flag, photo, currency_id)
VALUES (403, 400, '일본', 'image/sample', 'image/sample', 438),
       (404, 400, '중국', 'image/sample', 'image/sample', 439),
       (405, 400, '대만', 'image/sample', 'image/sample', 440),
       (406, 401, '태국', 'image/sample', 'image/sample', 441),
       (407, 401, '베트남', 'image/sample', 'image/sample', 442),
       (408, 401, '필리핀', 'image/sample', 'image/sample', 443),
       (409, 401, '싱가포르', 'image/sample', 'image/sample', 444),
       (410, 401, '인도네시아', 'image/sample', 'image/sample', 445),
       (411, 401, '말레이시아', 'image/sample', 'image/sample', 446),
       (412, 402, '괌', 'image/sample', 'image/sample', 447),
       (413, 402, '미국', 'image/sample', 'image/sample', 447);

INSERT INTO City (city_id, country_id, name)
VALUES (414, 403, '오사카'),
       (415, 403, '교토'),
       (416, 403, '도쿄'),
       (417, 403, '후쿠오카'),
       (418, 403, '오키나와'),
       (419, 403, '삿포로'),
       (420, 403, '나고야'),
       (421, 404, '홍콩'),
       (422, 404, '상하이'),
       (423, 404, '베이징'),
       (424, 405, '타이베이'),
       (425, 406, '방콕'),
       (426, 406, '치앙마이'),
       (427, 406, '푸켓'),
       (428, 407, '다낭'),
       (429, 407, '나트랑'),
       (430, 407, '호치민'),
       (431, 407, '하노이'),
       (432, 408, '세부'),
       (433, 408, '보라카이'),
       (434, 409, '싱가포르'),
       (435, 410, '발리'),
       (436, 411, '코타키나발루'),
       (437, 411, '쿠알라룸푸르');

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
INSERT INTO Filter (filter_id, name)
VALUES (350, '편의점'),
       (351, '고수');

INSERT INTO Subcategory_Filter (subcategory_filter_id, subcategory_id, filter_id)
VALUES
    --서브카테고리 필터 관계 테이블
    -- 라면 및 즉석식품 - 필터
    (360, 310, 350),
    (361, 310, 351),
    -- 스낵 및 과자 - 필터
    (362, 311, 350),
    (363, 311, 351),
    --조미료 및 소스 - 필터
    (364, 312, 350),
    (365, 312, 351),
    -- 보충제 및 건강식품 - 필터
    (366, 313, 350),
    -- 음료 및 차 - 필터
    (367, 314, 350),
    -- 주류 -필터
    (368, 315, 350);

/* Notification */
