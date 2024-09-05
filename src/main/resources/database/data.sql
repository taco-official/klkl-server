/* User */
INSERT INTO klkl_user(user_id, name, gender, age, description, created_at)
VALUES (1, 'testUser', '남', 20, '테스트입니다.', now());

/* Like */


/* Region */
INSERT INTO region(region_id, name)
VALUES (400, '동북아시아'),
       (401, '동남아시아'),
       (402, '기타');

INSERT INTO currency (currency_id, code, unit)
VALUES (438, 'JPY', '엔'),
       (439, 'CNH', '위안'),
       (440, 'TWD', '달러'),
       (441, 'THB', '바트'),
       (442, 'VND', '동'),
       (443, 'PHP', '페소'),
       (444, 'SGD', '달러'),
       (445, 'IDR', '루피아'),
       (446, 'MYR', '링깃'),
       (447, 'USD', '달러');

INSERT INTO country(country_id, region_id, name, code, photo, currency_id)
VALUES (403, 400, '일본', 'JP', 'image/sample', 438),
       (404, 400, '중국', 'CN', 'image/sample', 439),
       (405, 400, '대만', 'TW', 'image/sample', 440),
       (406, 401, '태국', 'TH', 'image/sample', 441),
       (407, 401, '베트남', 'VN', 'image/sample', 442),
       (408, 401, '필리핀', 'PH', 'image/sample', 443),
       (409, 401, '싱가포르', 'SG', 'image/sample', 444),
       (410, 401, '인도네시아', 'ID', 'image/sample', 445),
       (411, 401, '말레이시아', 'MY', 'image/sample', 446),
       (412, 402, '괌', 'GU', 'image/sample', 447),
       (413, 402, '미국', 'US', 'image/sample', 447);

INSERT INTO city (city_id, country_id, name)
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
       (437, 411, '쿠알라룸푸르'),
       (448, 412, '투몬'),
       (449, 413, '뉴욕'),
       (450, 413, '로스엔젤레스'),
       (451, 413, '하와이');

/* Category */
INSERT
INTO category(category_id, name)
VALUES (300, '식품'),
       (301, '의류'),
       (302, '잡화'),
       (303, '화장품');

INSERT INTO subcategory (subcategory_id, name, category_id)
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

/* tag */
INSERT INTO tag (tag_id, name)
VALUES (350, '편의점'),
       (351, '고수');

INSERT INTO subcategory_tag (subcategory_tag_id, subcategory_id, tag_id)
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

/* Product */
INSERT INTO product(product_id, user_id, name, description, address, price, like_count, rating,
                    city_id, subcategory_id, currency_id, created_at)
VALUES (101, 1, '곤약젤리', '탱글탱글 맛있는 곤약젤리', '신사이바시 메가돈키호테', 1000, 100, 5.0, 414, 311, 438, now()),
       (102, 1, '여름 원피스', '시원하고 여름 휴양지 느낌의 원피스', '방콕 짜뚜짝 시장', 300, 333, 4.5, 425, 323, 441, now()),
       (390, 1, '왕족발 보쌈 과자', '맛있는 왕족발 보쌈 과자', '상하이 장충동', 3000, 10, 3.0, 422, 311, 439, now());

/* Comment */
INSERT INTO comment(comment_id, product_id, user_id, content, created_at)
VALUES (500, 390, 1, '이거 정말 맛있는데 표현할 방법이 읎네.', now()),
       (501, 390, 1, '이거 정말 맛없는데 표현할 방법이 읎네.', now()),
       (502, 390, 1, '이거 정말 좋은데 표현할 방법이 읎네.', now());

/* Notification */
INSERT INTO notification(notification_id, is_read, created_at, comment_id)
VALUES (700, false, now(), 500),
       (701, false, now(), 501),
       (702, false, now(), 502);
