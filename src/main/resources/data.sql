-- 商品性质
INSERT IGNORE INTO category (id, category_name) VALUES (1, '食品饮料');
INSERT IGNORE INTO category (id, category_name) VALUES (2, '日用百货');
INSERT IGNORE INTO category (id, category_name) VALUES (3, '文具用品');
INSERT IGNORE INTO category (id, category_name) VALUES (4, '电子产品');
INSERT IGNORE INTO category (id, category_name) VALUES (5, '酒水饮品');

-- 商品信息
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP001', '康师傅红烧牛肉面', 1, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP002', '奥利奥饼干', 1, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP003', '海飞丝洗发水', 2, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP004', '维达抽纸', 2, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP005', '晨光中性笔', 3, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP006', '得力笔记本', 3, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP007', 'USB数据线', 4, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP008', '蓝牙耳机', 4, NOW(), 0);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP009', '青岛啤酒500ml', 5, NOW(), 1);
INSERT IGNORE product (product_id, product_name, category_id, create_time, status) VALUES ('SP010', '农夫山泉矿泉水', 5, NOW(), 1);

-- 库存
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP001', 50, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP002', 30, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP003', 20, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP004', 100, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP005', 200, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP006', 80, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP007', 45, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP008', 5, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP009', 60, NOW());
INSERT IGNORE inventory (product_id, quantity, update_time) VALUES ('SP010', 8, NOW());

-- 买入记录（最近30天）
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP001', 3.50, 100, DATE_SUB(CURDATE(), INTERVAL 30 DAY), 350.00, '月初进货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP002', 5.00, 60, DATE_SUB(CURDATE(), INTERVAL 28 DAY), 300.00, NULL);
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP003', 25.00, 30, DATE_SUB(CURDATE(), INTERVAL 25 DAY), 750.00, '洗发水批量采购');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP004', 2.00, 150, DATE_SUB(CURDATE(), INTERVAL 22 DAY), 300.00, NULL);
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP005', 1.00, 300, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 300.00, '开学季备货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP006', 3.00, 120, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 360.00, '开学季备货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP007', 6.00, 80, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 480.00, NULL);
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP008', 45.00, 20, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 900.00, '耳机进货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP009', 3.00, 120, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 360.00, NULL);
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP010', 0.80, 200, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 160.00, '矿泉水大量进货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP001', 3.20, 80, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 256.00, '补货');
INSERT IGNORE purchase (product_id, purchase_price, quantity, purchase_date, total_amount, remark) VALUES ('SP003', 24.00, 20, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 480.00, NULL);

-- 卖出记录（最近20天）
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP001', 5.00, 50, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 250.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP001', 5.00, 30, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 150.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP001', 5.00, 50, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 250.00, '周末促销');
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP002', 8.00, 30, DATE_SUB(CURDATE(), INTERVAL 18 DAY), 240.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP003', 39.00, 20, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 780.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP003', 39.00, 10, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 390.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP004', 3.50, 50, DATE_SUB(CURDATE(), INTERVAL 12 DAY), 175.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP005', 2.00, 100, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 200.00, '文具促销');
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP006', 5.00, 40, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 200.00, '文具促销');
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP007', 12.00, 35, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 420.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP008', 69.00, 15, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1035.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP009', 5.00, 60, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 300.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP010', 2.00, 180, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 360.00, '夏季热销');
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP010', 2.00, 12, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 24.00, NULL);
INSERT IGNORE sale (product_id, sale_price, quantity, sale_date, total_amount, remark) VALUES ('SP004', 3.50, 50, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 175.00, NULL);
