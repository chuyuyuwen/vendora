# 小商店销售管理系统 — 前后端接口对照大纲

## 一、项目技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0 + Spring MVC |
| ORM | Spring Data JPA (Hibernate) |
| 模板引擎 | Thymeleaf |
| 数据库 | MySQL |
| 工具 | Lombok, Bean Validation |

## 二、已完成部分（entity + repository + service）

```
已完成的类（无需再写）:
├── entity/   Category, Product, Purchase, Sale, Inventory
├── repository/  CategoryRepository, ProductRepository, PurchaseRepository, SaleRepository, InventoryRepository
├── service/  ProductService, PurchaseService, SaleService, InventoryService, ReportService
├── service/impl/  全部5个Service实现类
└── util/     PageBean<T>
```

## 三、待完成部分总览

```
待完成:
├── controller/   5个Controller（约15个接口）
├── templates/    8~10个Thymeleaf页面
└── application.properties  数据库配置
```

---

## 四、模块一：商品管理

### 4.1 涉及的前端页面

| 页面 | 模板路径 | 功能说明 |
|------|----------|----------|
| 商品列表页 | `templates/product/list.html` | 展示所有商品，支持搜索/筛选，提供"新增/编辑/停用"按钮 |
| 商品表单页 | `templates/product/form.html` | 新增 & 编辑共用同一表单页 |

### 4.2 列表页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ① | 进入列表页，默认展示全部商品 | `GET` | `/product/list` | 无 | 视图名 `product/list`，Model中放入 `List<Product>` | `productService.listAllProducts()` |
| ② | 点击搜索按钮 | `GET` | `/product/list` | `?productId=xxx&productName=xxx&categoryId=x` | 同①，带筛选条件 | `productService.searchProducts(productId, productName, categoryId)` |
| ③ | 点击"停用"按钮 | `GET` | `/product/disable/{productId}` | 路径变量 productId | 重定向到 `/product/list` | `productService.disableProduct(productId)` |

### 4.3 表单页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ④ | 点击"新增"按钮（空白表单） | `GET` | `/product/add` | 无 | 视图名 `product/form`，Model中放入空Product + 全部Category列表（供下拉选择） | `categoryRepository.findAll()` |
| ⑤ | 点击"编辑"按钮（回填表单） | `GET` | `/product/edit/{productId}` | 路径变量 productId | 视图名 `product/form`，Model中放入已有Product + Category列表 | `productService.getProductById(productId)` |
| ⑥ | 提交表单（新增/编辑） | `POST` | `/product/save` | 表单字段：productId, productName, category.id, status | 重定向到 `/product/list` | `productService.addProduct()` 或 `productService.modifyProduct()` |

### 4.4 Controller 代码骨架（ProductController）

```
@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public String list(@RequestParam(required=false) String productId,
                       @RequestParam(required=false) String productName,
                       @RequestParam(required=false) Integer categoryId,
                       Model model) { ... }

    @GetMapping("/add")
    public String addForm(Model model) { ... }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable String productId, Model model) { ... }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) { ... }

    @GetMapping("/disable/{productId}")
    public String disable(@PathVariable String productId) { ... }
}
```

---

## 五、模块二：买入管理

### 5.1 涉及的前端页面

| 页面 | 模板路径 | 功能说明 |
|------|----------|----------|
| 买入列表页 | `templates/purchase/list.html` | 分页展示买入记录，按商品编号/日期范围查询 |
| 买入录入页 | `templates/purchase/form.html` | 录入新的买入记录 |
| 买入汇总打印页 | `templates/purchase/print.html` | 按日期范围展示买入汇总统计（只读打印视图） |

### 5.2 列表页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑦ | 进入买入列表（分页） | `GET` | `/purchase/list` | `?pageNo=1&pageSize=10` | 视图 `purchase/list`，Model放入 `PageBean<Purchase>` | `purchaseService.queryPurchases(null, null, null, pageNo, pageSize)` |
| ⑧ | 按条件查询 | `GET` | `/purchase/list` | `?productId=xxx&startDate=2026-01-01&endDate=2026-05-20&pageNo=1&pageSize=10` | 同⑦，带筛选 | `purchaseService.queryPurchases(productId, startDate, endDate, pageNo, pageSize)` |

### 5.3 录入页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑨ | 打开买入录入表单 | `GET` | `/purchase/add` | 无 | 视图 `purchase/form`，Model放入全部商品列表（供下拉选择） | `productService.listAllProducts()` |
| ⑩ | 提交买入记录 | `POST` | `/purchase/save` | 表单：product.productId, purchasePrice, quantity, purchaseDate, remark | 重定向到 `/purchase/list`（失败则返回表单页+错误提示） | `purchaseService.addPurchase(purchase)`（含事务：insert买入记录 + 更新inventory库存） |

### 5.4 汇总打印页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑪ | 查看买入汇总打印 | `GET` | `/purchase/print` | `?startDate=xxx&endDate=xxx` | 视图 `purchase/print`，Model放入汇总数据 `List<Map<String,Object>>` | `purchaseService.getPurchaseSummary(startDate, endDate)` |

### 5.5 Controller 代码骨架（PurchaseController）

```
@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @GetMapping("/list")
    public String list(@RequestParam(required=false) String productId,
                       @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                       @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                       @RequestParam(default="1") int pageNo,
                       @RequestParam(default="10") int pageSize,
                       Model model) { ... }

    @GetMapping("/add")
    public String addForm(Model model) { ... }

    @PostMapping("/save")
    public String save(@ModelAttribute Purchase purchase, RedirectAttributes ra) { ... }

    @GetMapping("/print")
    public String print(@RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                        @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                        Model model) { ... }
}
```

---

## 六、模块三：卖出管理

### 6.1 涉及的前端页面

| 页面 | 模板路径 | 功能说明 |
|------|----------|----------|
| 卖出列表页 | `templates/sale/list.html` | 分页展示卖出记录，按条件查询 |
| 卖出录入页 | `templates/sale/form.html` | 录入卖出记录（需校验库存） |
| 卖出汇总打印页 | `templates/sale/print.html` | 按日期范围展示卖出汇总 |

### 6.2 列表页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑫ | 进入/查询卖出列表 | `GET` | `/sale/list` | `?productId=xxx&startDate=xxx&endDate=xxx&pageNo=1&pageSize=10` | 视图 `sale/list`，Model放入 `PageBean<Sale>` | `saleService.querySales(...)` |

### 6.3 录入页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑬ | 打开卖出录入表单 | `GET` | `/sale/add` | 无 | 视图 `sale/form`，Model放入商品列表 | `productService.listAllProducts()` |
| ⑭ | 提交卖出记录 | `POST` | `/sale/save` | 表单：product.productId, salePrice, quantity, saleDate, remark | 成功→重定向 `/sale/list`；库存不足→返回表单+错误提示 | `saleService.addSale(sale)`（含事务：校验库存→insert卖出记录→扣减库存） |

### 6.4 汇总打印页需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑮ | 查看卖出汇总打印 | `GET` | `/sale/print` | `?startDate=xxx&endDate=xxx` | 视图 `sale/print`，Model放入 `List<Map<String,Object>>` | `saleService.getSaleSummary(...)` |

### 6.5 Controller 代码骨架（SaleController）

```
@Controller
@RequestMapping("/sale")
public class SaleController {

    @GetMapping("/list")
    public String list(@RequestParam(required=false) String productId,
                       @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                       @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                       @RequestParam(default="1") int pageNo,
                       @RequestParam(default="10") int pageSize,
                       Model model) { ... }

    @GetMapping("/add")
    public String addForm(Model model) { ... }

    @PostMapping("/save")
    public String save(@ModelAttribute Sale sale, RedirectAttributes ra) { ... }

    @GetMapping("/print")
    public String print(@RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                        @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                        Model model) { ... }
}
```

---

## 七、模块四：库存管理

### 7.1 涉及的前端页面

| 页面 | 模板路径 | 功能说明 |
|------|----------|----------|
| 库存列表页 | `templates/inventory/list.html` | 展示所有商品库存，支持按商品编号/性质筛选，低库存高亮 |

### 7.2 需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑯ | 查看库存列表 | `GET` | `/inventory/list` | `?productId=xxx&categoryId=x` | 视图 `inventory/list`，Model放入 `List<Inventory>` | `inventoryService.getInventoryList(productId, categoryId)` |
| ⑰ | 低库存预警 | `GET` | `/inventory/alert` | `?threshold=10` | 视图 `inventory/alert`（或嵌在list页），Model放入低库存列表 | `inventoryService.getLowStockProducts(threshold)` |

### 7.3 Controller 代码骨架（InventoryController）

```
@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @GetMapping("/list")
    public String list(@RequestParam(required=false) String productId,
                       @RequestParam(required=false) Integer categoryId,
                       Model model) { ... }

    @GetMapping("/alert")
    public String alert(@RequestParam(default="10") int threshold,
                        Model model) { ... }
}
```

---

## 八、模块五：营业管理（报表）

### 8.1 涉及的前端页面

| 页面 | 模板路径 | 功能说明 |
|------|----------|----------|
| 营业额报表页 | `templates/report/sales.html` | 按时间段+商品性质查询销售额/销售量，表格+汇总 |
| 利润分析页 | `templates/report/profit.html` | 按时间段+商品性质分析利润 |

### 8.2 需要的后端接口

| # | 前端交互 | HTTP方法 | URL | 参数 | 后端返回值 | 调用的Service方法 |
|---|----------|----------|-----|------|-----------|-------------------|
| ⑱ | 查询营业额报表 | `GET` | `/report/sales` | `?startDate=xxx&endDate=xxx&categoryId=x` | 视图 `report/sales`，Model放入 `List<Map<String,Object>>`（按性质分组统计的销售总额+销量） | `reportService.getSalesReport(startDate, endDate, categoryId)` |
| ⑲ | 查询利润分析 | `GET` | `/report/profit` | `?startDate=xxx&endDate=xxx&categoryId=x` | 视图 `report/profit`，Model放入利润数据 | `reportService.getProfitReport(startDate, endDate, categoryId)` |

### 8.3 Controller 代码骨架（ReportController）

```
@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/sales")
    public String salesReport(@RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                              @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                              @RequestParam(required=false) Integer categoryId,
                              Model model) { ... }

    @GetMapping("/profit")
    public String profitReport(@RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate startDate,
                               @RequestParam(required=false) @DateTimeFormat(iso=DATE) LocalDate endDate,
                               @RequestParam(required=false) Integer categoryId,
                               Model model) { ... }
}
```

---

## 九、所有接口汇总表

| 序号 | 模块 | HTTP | URL | 用途 |
|------|------|------|-----|------|
| ① | 商品 | GET | `/product/list` | 商品列表（默认全量） |
| ② | 商品 | GET | `/product/list?productId=&productName=&categoryId=` | 商品搜索 |
| ③ | 商品 | GET | `/product/disable/{id}` | 停用商品 |
| ④ | 商品 | GET | `/product/add` | 新增商品表单 |
| ⑤ | 商品 | GET | `/product/edit/{id}` | 编辑商品表单 |
| ⑥ | 商品 | POST | `/product/save` | 保存商品（新增/编辑） |
| ⑦ | 买入 | GET | `/purchase/list` | 买入列表（分页） |
| ⑧ | 买入 | GET | `/purchase/list?productId=&startDate=&endDate=` | 买入条件查询 |
| ⑨ | 买入 | GET | `/purchase/add` | 买入录入表单 |
| ⑩ | 买入 | POST | `/purchase/save` | 提交买入记录 |
| ⑪ | 买入 | GET | `/purchase/print` | 买入汇总打印 |
| ⑫ | 卖出 | GET | `/sale/list` | 卖出列表（分页） |
| ⑬ | 卖出 | GET | `/sale/add` | 卖出录入表单 |
| ⑭ | 卖出 | POST | `/sale/save` | 提交卖出记录 |
| ⑮ | 卖出 | GET | `/sale/print` | 卖出汇总打印 |
| ⑯ | 库存 | GET | `/inventory/list` | 库存列表 |
| ⑰ | 库存 | GET | `/inventory/alert` | 低库存预警 |
| ⑱ | 报表 | GET | `/report/sales` | 营业额报表 |
| ⑲ | 报表 | GET | `/report/profit` | 利润分析 |

**共 19 个接口，5 个 Controller，8~10 个 Thymeleaf 页面。**

---

## 十、数据库配置（application.properties）

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/store_db?useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.thymeleaf.cache=false
```

---

## 十一、推荐实现顺序

| 步骤 | 内容 | 说明 |
|------|------|------|
| Step 1 | `application.properties` | 配好数据库连接，启动项目确认能跑 |
| Step 2 | `ProductController` + `product/list.html` + `product/form.html` | 最简单，不涉及事务和分页，练手首选 |
| Step 3 | `PurchaseController` + `purchase/list.html` + `purchase/form.html` | 练分页 + 事务（买入→自动更新库存） |
| Step 4 | `SaleController` + `sale/list.html` + `sale/form.html` | 练库存校验 + 事务（卖出→校验库存→扣库存） |
| Step 5 | `purchase/print.html` + `sale/print.html` | 汇总打印页，复用已有的Controller接口 |
| Step 6 | `InventoryController` + `inventory/list.html` | 库存查看 + 低库存预警 |
| Step 7 | `ReportController` + `report/sales.html` + `report/profit.html` | 营业报表，最复杂的JPQL查询已写好 |

---

## 十二、页面间导航关系

```
首页（可设为商品列表）
├── 商品管理 → /product/list ──新增/编辑──→ /product/form
├── 买入管理 → /purchase/list ──录入──→ /purchase/form
│                            └──打印──→ /purchase/print
├── 卖出管理 → /sale/list ──录入──→ /sale/form
│                         └──打印──→ /sale/print
├── 库存管理 → /inventory/list
│            └──预警──→ /inventory/alert
└── 营业管理 → /report/sales
             └──利润──→ /report/profit
```
