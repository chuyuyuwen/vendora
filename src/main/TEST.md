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
com.store
├── entity           // 实体类（POJO）
├── dao              // 数据访问层接口
│   └── impl         // DAO实现类（JDBC）
├── service          // 业务逻辑层接口
│   └── impl         // Service实现类
├── web              // Servlet（控制器）
├── util             // 工具类（数据库连接、日期处理等）
└── filter           // 过滤器（字符编码等）
1. 实体类 (entity)
   Category（商品性质：id, categoryName）

Product（商品信息，对应数据库字段）

Purchase（买入记录，含总金额自动计算）

Sale（卖出记录，含总金额自动计算）

Inventory（库存对象，可含扩展信息）

2. DAO 层接口 (dao)
   每个实体对应一个 DAO 接口，定义增删改查及特殊查询方法：

CategoryDao：查所有性质、按ID查名称

ProductDao：增、删、改、查所有，按编号/名称模糊查询，按性质筛选

PurchaseDao：插入买入记录、分页查询买入记录（支持日期范围/商品编号）、汇总查询（按时间段、商品性质等汇总进货量）

SaleDao：插入卖出记录、分页查询卖出记录、汇总查询（为营业报表提供数据）

InventoryDao：查询当前库存（可按商品、按性质分组）、更新库存（增加/减少）

实现类放在 impl 包下，使用 JDBC PreparedStatement 或 连接池（如 HikariCP/Druid）。

3. Service 层接口 (service)
   封装业务逻辑（校验、事务、库存联动）：

ProductService：商品增删改查，关联性质名称

PurchaseService：

买入录入：调用 PurchaseDao.insert() + InventoryDao.increaseStock()（需事务）

买入查询/汇总打印

SaleService：

卖出录入：校验库存是否充足，调用 SaleDao.insert() + InventoryDao.decreaseStock()（事务）

卖出查询/汇总打印

InventoryService：库存查询（支持按商品、按性质、预警等）、报表数据组织

ReportService：

营业额查询：按时间段、商品性质统计销售总额、销售量、利润（可选）

营业分类统计输出

4. Web 表现层 (web.servlet)
   每个主要功能对应一个 Servlet，通过 doGet/doPost 分发，调用 Service，转发到 JSP 或输出 JSON（打印可用简易 HTML 表格 + 浏览器打印功能）：

ProductListServlet、ProductAddServlet …

PurchaseAddServlet（录入买入信息）、PurchaseQueryServlet、PurchasePrintServlet

SaleAddServlet、SaleQueryServlet、SalePrintServlet

InventoryQueryServlet、InventoryPrintServlet

ReportServlet（接受时间段、商品性质参数，返回统计结果）

5. JSP 页面（前端）
   放在 WebContent 或 src/main/webapp 下：

product_list.jsp、product_add.jsp

purchase_add.jsp、purchase_query.jsp、purchase_print.jsp

sale_add.jsp、sale_query.jsp、sale_print.jsp

inventory.jsp、report.jsp

可抽取头部、菜单栏为公共 <jsp:include>。

6. 工具类 (util)
   DBUtil：获取数据库连接（静态方法，可配合连接池）

DateUtils：日期格式转换，为查询提供 java.sql.Date 转换

PageBean：分页工具（封装当前页、总页数、记录列表等）

三、下一步建议
先建库建表，根据上面设计编写 SQL 脚本并导入 MySQL。

搭建项目基础结构：创建 Dynamic Web Project（或 Maven Web 项目），引入 Servlet、JSTL、MySQL 驱动、连接池依赖。

编写 DBUtil 和实体类。

实现一个简单的功能链（例如商品信息的添加和列表展示），打通 Servlet → Service → DAO → JSP 全流程。

再逐步增加买入、卖出、库存联动（重点练习事务）。

最后实现营业报表（复杂查询 + 页面展示）。