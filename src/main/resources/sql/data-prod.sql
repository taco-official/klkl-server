/* Region */
INSERT INTO region(region_id, name)
VALUES (1, '동북아시아'),
       (2, '동남아시아'),
       (3, '기타');

/* Currency */
INSERT INTO currency (currency_id, code, unit)
VALUES (1, 'JPY', '엔'),
       (2, 'CNH', '위안'),
       (3, 'TWD', '달러'),
       (4, 'THB', '바트'),
       (5, 'VND', '동'),
       (6, 'PHP', '페소'),
       (7, 'SGD', '달러'),
       (8, 'IDR', '루피아'),
       (9, 'MYR', '링깃'),
       (10, 'USD', '달러');

/* Country */
INSERT INTO country(country_id, region_id, name, code, wallpaper, currency_id)
VALUES (1, 1, '일본', 'JP', 'image/sample', 1),
       (2, 1, '중국', 'CN', 'image/sample', 2),
       (3, 1, '대만', 'TW', 'image/sample', 3),
       (4, 2, '태국', 'TH', 'image/sample', 4),
       (5, 2, '베트남', 'VN', 'image/sample', 5),
       (6, 2, '필리핀', 'PH', 'image/sample', 6),
       (7, 2, '싱가포르', 'SG', 'image/sample', 7),
       (8, 2, '인도네시아', 'ID', 'image/sample', 8),
       (9, 2, '말레이시아', 'MY', 'image/sample', 9),
       (10, 3, '괌', 'GU', 'image/sample', 10),
       (11, 3, '미국', 'US', 'image/sample', 10);

/* City */
INSERT INTO city (city_id, country_id, name)
VALUES (1, 1, '오사카'),
       (2, 1, '교토'),
       (3, 1, '도쿄'),
       (4, 1, '후쿠오카'),
       (5, 1, '오키나와'),
       (6, 1, '삿포로'),
       (7, 1, '나고야'),
       (8, 2, '홍콩'),
       (9, 2, '상하이'),
       (10, 2, '베이징'),
       (11, 3, '타이베이'),
       (12, 4, '방콕'),
       (13, 4, '치앙마이'),
       (14, 4, '푸켓'),
       (15, 5, '다낭'),
       (16, 5, '나트랑'),
       (17, 5, '호치민'),
       (18, 5, '하노이'),
       (19, 6, '세부'),
       (20, 6, '보라카이'),
       (21, 7, '싱가포르'),
       (22, 8, '발리'),
       (23, 9, '코타키나발루'),
       (24, 9, '쿠알라룸푸르'),
       (25, 10, '투몬'),
       (26, 11, '뉴욕'),
       (27, 11, '로스엔젤레스'),
       (28, 11, '하와이');

/* Category */
INSERT INTO category(category_id, name)
VALUES (1, '식품'),
       (2, '의류'),
       (3, '잡화'),
       (4, '화장품');

/* Subcategory */
INSERT INTO subcategory (subcategory_id, name, category_id)
VALUES
    (1, '라면 및 즉석식품', 1),
    (2, '스낵 및 과자', 1),
    (3, '조미료 및 소스', 1),
    (4, '보충제 및 건강식품', 1),
    (5, '음료 및 차', 1),
    (6, '주류', 1),
    (7, '상의', 2),
    (8, '하의', 2),
    (9, '아우터', 2),
    (10, '원피스', 2),
    (11, '신발', 2),
    (12, '액세사리', 2),
    (13, '쥬얼리', 2),
    (14, '일반의약품', 3),
    (15, '주방잡화', 3),
    (16, '욕실잡화', 3),
    (17, '문구 및 완구', 3),
    (18, '스킨케어', 4),
    (19, '메이크업', 4),
    (20, '헤어케어', 4),
    (21, '바디케어', 4),
    (22, '위생용품', 4);

/* Tag */
INSERT INTO tag (tag_id, name)
VALUES (1, '편의점'),
       (2, '고수');

INSERT INTO subcategory_tag (subcategory_tag_id, subcategory_id, tag_id)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 2, 1),
    (4, 2, 2),
    (5, 3, 1),
    (6, 3, 2),
    (7, 4, 1),
    (8, 5, 1),
    (9, 6, 1);