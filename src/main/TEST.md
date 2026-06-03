# Vendora 小商店管理系统 — 项目大纲

## 一、技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0 + Spring MVC |
| ORM | Spring Data JPA (Hibernate) |
| 模板引擎 | Thymeleaf |
| 数据库 | MySQL (Vendora) |
| 工具 | Lombok, Bean Validation, ECharts |

## 二、项目结构（全部已完成）

```
src/main/java/com/city/vendora/
├── entity/          Category, Product, Purchase, Sale, Inventory
├── repository/      Category, Product, Purchase, Sale, Inventory
├── service/         Product, Purchase, Sale, Inventory, Report
├── service/impl/    5个Service实现类
├── controller/      Index, Trade, Product, Purchase, Sale, Inventory, Report
└── util/            PageBean<T>

src/main/resources/
├── templates/
│   ├── index.html              首页仪表板（统计卡片+饼图+折线图）
│   ├── trade/list.html          交易管理（买入/卖出标签页）
│   ├── product/list.html + form.html
│   ├── purchase/list.html + form.html + print.html
│   ├── sale/list.html + form.html + print.html
│   ├── inventory/list.html
│   ├── report/sales.html + profit.html
│   └── common/header.html      公共导航+页头
├── application.properties
└── data.sql                    种子数据
```

## 三、数据库

| 表名 | 主键 | 说明 |
|------|------|------|
| `category` | `id` (自增) | 商品性质 |
| `product` | `product_id` | 商品信息 |
| `purchase` | `purchase_id` (自增) | 买入记录 |
| `sale` | `sale_id` (自增) | 卖出记录 |
| `inventory` | `product_id` (FK) | 库存 |

> 注意: Purchase 和 Sale 实体的 @Column(name) 必须与数据库列名一致（purchase_id / sale_id）。

## 四、接口汇总（共20个）

| # | 模块 | HTTP | URL | 说明 |
|---|------|------|-----|------|
| 1 | 首页 | GET | `/` | 仪表板，Controller 提供 salesByCategoryJson / dailySalesJson（JSON字符串） |
| 2 | 交易 | GET | `/trade?tab=purchase\|sale` | 交易管理页，买入/卖出标签切换 |
| 3 | 商品 | GET | `/product/list` | 商品列表+搜索 |
| 4 | 商品 | GET | `/product/add` | 新增表单 |
| 5 | 商品 | GET | `/product/edit/{id}` | 编辑表单 |
| 6 | 商品 | POST | `/product/save` | 保存商品 |
| 7 | 商品 | GET | `/product/disable/{id}` | 停用商品 |
| 8 | 买入 | GET | `/purchase/list` | 买入列表（保留兼容） |
| 9 | 买入 | GET | `/purchase/add` | 买入录入 |
| 10 | 买入 | POST | `/purchase/save` | 提交后重定向 /trade?tab=purchase |
| 11 | 买入 | GET | `/purchase/print` | 买入汇总打印 |
| 12 | 卖出 | GET | `/sale/list` | 卖出列表（保留兼容） |
| 13 | 卖出 | GET | `/sale/add` | 卖出录入（选商品自动显示库存+最近售价） |
| 14 | 卖出 | GET | `/sale/product-info?productId=x` | REST，返回 {latestPrice, stock} |
| 15 | 卖出 | POST | `/sale/save` | 提交后重定向 /trade?tab=sale |
| 16 | 卖出 | GET | `/sale/print` | 卖出汇总打印 |
| 17 | 库存 | GET | `/inventory/list` | 库存列表 |
| 18 | 库存 | GET | `/inventory/alert` | 低库存预警 |
| 19 | 报表 | GET | `/report/sales` | 营业额报表 |
| 20 | 报表 | GET | `/report/profit` | 利润分析 |

## 五、页面导航

```
首页 / → 仪表板
├── 商品管理 → /product/list ──新增/编辑──→ /product/form
├── 交易管理 → /trade
│   ├── 买入标签 ──录入──→ /purchase/form ──保存──→ /trade?tab=purchase
│   │           └──打印──→ /purchase/print
│   └── 卖出标签 ──录入──→ /sale/form ──保存──→ /trade?tab=sale
│               └──打印──→ /sale/print
├── 库存管理 → /inventory/list + /inventory/alert
└── 营业报表 → /report/sales + /report/profit
```

## 六、关键注意事项

1. Purchase 主键是 purchase_id，Sale 主键是 sale_id
2. 首页图表：Controller 预序列化为 JSON 字符串，前端 JSON.parse 解析
3. 卖出录入：选择商品后自动查询最近售价和库存
4. 模板 head 统一使用 th:replace 避免嵌套
5. 买入/卖出保存后统一重定向到 /trade