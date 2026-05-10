20、小商店销售管理系统
系统功能的基本要求：
（1）买入管理：包括商品买入信息(商品编号、商品名称、商品性质、买入日期、
进价等等)录入、买入信息查询、买入信息汇总打印；  
（2）出售管理：包括商品出售信息录入（商品编号、卖出日期、卖出价格）、出
售信息查询、出售信息汇总打印；  
（3）商品库存管理：商品库存信息的查询和报表输出；  
（4）营业管理：可以根据不同时间段、商品性质等信息对营业额进行查询、分类统计和输出。
这个是我目前Javaee的实训需要完成的项目，我目前的选择是web+java+MYSQL，一步一步来，先告诉我需要哪些数据库，给出一个大纲（比如需要实现什么类、接口等等）


一、数据库设计
需要 4 张核心表，外加一个商品性质字典表（便于规范化管理商品性质）。

1. 商品性质字典表 category
   字段名	类型	说明
   id	INT	主键，自增
   category_name	VARCHAR(50)	商品性质（如“食品”“日用品”）
2. 商品信息表 product
   字段名	类型	说明
   product_id	VARCHAR(20)	商品编号（主键）
   product_name	VARCHAR(100)	商品名称
   category_id	INT	商品性质（外键）
   create_time	DATETIME	录入时间
   status	TINYINT	状态（1在用，0停用）
3. 买入记录表 purchase
   字段名	类型	说明
   id	INT	主键，自增
   product_id	VARCHAR(20)	商品编号（外键）
   purchase_price	DECIMAL(10,2)	进价
   quantity	INT	买入数量
   purchase_date	DATE	买入日期
   total_amount	DECIMAL(12,2)	总金额（=进价×数量）
   remark	VARCHAR(255)	备注
4. 卖出记录表 sale
   字段名	类型	说明
   id	INT	主键，自增
   product_id	VARCHAR(20)	商品编号（外键）
   sale_price	DECIMAL(10,2)	卖出价格
   quantity	INT	卖出数量
   sale_date	DATE	卖出日期
   total_amount	DECIMAL(12,2)	总金额（=售价×数量）
   remark	VARCHAR(255)	备注
5. 库存表 inventory
   为方便性能与报表，采用实时库存表（每次买入/卖出后更新数量）：

字段名	类型	说明
product_id	VARCHAR(20)	商品编号（主键，外键）
quantity	INT	当前库存数量
update_time	DATETIME	最后更新时间
也可以通过视图 v_inventory 直接计算（SUM(purchase.quantity) - SUM(sale.quantity)），你可以根据自己掌握程度选择。实时库存表在项目中更常见，也便于练习事务管理（买入/卖出时同时更新库存）。

二、项目大纲（类、接口与包结构）
建议的 Java EE 项目包结构：

text
com.city.vendora
├── entity           // JPA 实体类（已加 @Entity、Lombok）
├── repository       // Spring Data JPA Repository
├── service          // 业务逻辑层接口
│   └── impl         // Service实现类
├── controller       // Spring MVC @Controller
├── util             // 工具类（PageBean 等）
└── dto              // 数据传输对象（可选）
1. 实体类 (entity) — 使用 JPA 注解 + Lombok
   Category：@Entity, @Id, categoryName

Product：@Entity, @Id → productId, @ManyToOne → Category, status 用 Integer

Purchase：@Entity, @Id 自增, @ManyToOne → Product（代替 product_id 外键）

Sale：@Entity, @Id 自增, @ManyToOne → Product

Inventory：@Entity, @Id + @OneToOne + @MapsId → Product（product_id 既是主键又是外键）

2. Repository 层 (repository) — Spring Data JPA
   继承 JpaRepository，无需实现类，基础 CRUD 自动提供：

   CategoryRepository extends JpaRepository<Category, Integer>

   ProductRepository extends JpaRepository<Product, String>
       findByProductIdContainingAndProductNameContaining() — 模糊查询
       findByCategory_Id() — 按性质筛选

   PurchaseRepository extends JpaRepository<Purchase, Integer>
       @Query 分页条件查询（按商品编号/日期范围）
       @Query 进货汇总（按性质分组统计数量+金额）

   SaleRepository extends JpaRepository<Sale, Integer>
       @Query 分页条件查询
       @Query 销售汇总

   InventoryRepository extends JpaRepository<Inventory, String>
       @Query 按性质筛选库存
       findByQuantityLessThan() — 低库存预警

3. Service 层接口 (service)
   封装业务逻辑（校验、事务、库存联动）：

ProductService：商品增删改查，关联性质名称

PurchaseService：
   买入录入：purchaseRepository.save() + inventoryRepository.save()（@Transactional）
   买入查询/汇总打印

SaleService：
   卖出录入：校验库存 → saleRepository.save() + 扣库存（@Transactional）
   卖出查询/汇总打印

InventoryService：库存查询（支持按商品、按性质、预警等）、报表数据组织

ReportService：

营业额查询：按时间段、商品性质统计销售总额、销售量、利润（可选）

营业分类统计输出

4. Controller 层 (controller) — Spring MVC
   使用 @Controller，调用 Service，返回视图名到 Thymeleaf 模板：

   ProductController — /product/list、/product/add、/product/edit、/product/delete

   PurchaseController — /purchase/list、/purchase/add、/purchase/print

   SaleController — /sale/list、/sale/add、/sale/print

   InventoryController — /inventory/list、/inventory/alert

   ReportController — /report/sales、/report/profit

5. 前端模板（resources/templates/）— Thymeleaf
   放在 src/main/resources/templates/ 下，按 Controller 建子目录：

   product/list.html、product/form.html

   purchase/list.html、purchase/form.html

   sale/list.html、sale/form.html

   inventory/list.html

   report/sales.html、report/profit.html

   可抽取公共片段（fragment）到 common/ 目录下。

6. 工具类 (util)
   PageBean：分页工具（封装当前页、总页数、记录列表等）
   注意：Spring Boot 自动管理数据源和事务，无需手动 DBUtil

三、下一步建议
项目基础已搭好（Spring Boot + JPA + MySQL），当前完成度：

实体类 ✓ → Repository ✓ → Service（待写）→ Controller（待写）→ 模板（待写）

建议实现顺序：
1. Service 层 — 依次实现 Product → Purchase → Sale → Inventory → Report
2. Controller 层 — 对应每个 Service 的增删改查接口
3. Thymeleaf 模板 — 列表页、表单页、打印页

重点练习：
- Purchase 入库 + 库存联动（@Transactional）
- Sale 出库 + 库存校验 + 库存联动（@Transactional）
- 营业报表的 JPQL 复杂查询

## 四、用户登录与角色管理（后续扩展）

### 数据库
6. 用户表 user
   | 字段 | 类型 | 说明 |
   |---|---|---|
   | id | INT | 主键，自增 |
   | username | VARCHAR(50) | 用户名 |
   | password | VARCHAR(255) | 密码 |
   | role | VARCHAR(20) | 角色：ADMIN / USER |

### 新增文件
```
entity/User.java          — @Entity，字段：id, username, password, role
repository/UserRepository — findByUsernameAndPassword()
controller/LoginController — /login (GET表单 + POST登录), /logout
```

### 登录流程
1. 浏览器请求任意页面 → 先检查 session 有没有 loginUser
2. 没有 → 重定向到 /login
3. 输入用户名密码 → POST /login → 查数据库 → 匹配则存入 session → 跳转首页
4. 不匹配 → 返回登录页 + 错误提示

### 角色控制
- ADMIN：可以访问所有功能（增删改查）
- USER：只能查看（列表、查询），不能添加/修改/删除
- 在 Controller 方法里用 HttpSession 获取当前用户，判断 role