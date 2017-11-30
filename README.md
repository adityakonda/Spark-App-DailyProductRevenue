# Daily Product Revenue Application

## DailyProductRevenue in MySQL & Spark 

This application will calculate the daily product revenue that displays date in ascending order and revenue in decending order in Spark & MySQL. It also demonstrates how to reduce Stages & Task in Spark using broadcast variables. 
	

## Retail Database Schema 

<p align="center">
  <img width="700" height="450" src="https://user-images.githubusercontent.com/7428555/33092798-37222b68-cec9-11e7-8595-d0b2f4a1333b.jpg">
</p>

### MySQL - DailyProductRevenue

```

SELECT  o.order_date, sum(oi.order_item_subtotal), p.product_name
FROM 
 (
	retail_db.order_items oi JOIN retail_db.orders o ON oi.order_item_order_id = o.order_id
	JOIN
	retail_db.products p ON p.product_id = oi.order_item_product_id
  )
 GROUP BY o.order_date, p.product_name
 ORDER BY o.order_date, sum(oi.order_item_subtotal) DESC;

 ```

### Spark - DailyProductRevenue - Joining Data without using Broadcast Variables

<p align="center">
  <img width="700" height="350" src="https://user-images.githubusercontent.com/7428555/33091792-1f402aa2-cec6-11e7-981b-0781cb991dd7.PNG">
</p>

- **Stage 0:** Reading **orders** data from HDFS and convering into (K,V) --> *orderMap --> (orderID, orderDate)*
- **Stage 1:** Reading **order_items** data from HDFS and convering into (K,V) --> *orderItemMap -> (orderID, (productID, order_itemSubTotal))*
- **Stage 2:** Joining orderMap & orderItemMap --> *ordersJoin(K,V) --> (orderID, (orderDate ,(productID, order_itemSubTotal)))* and convering into (K,V) --> *orderJoinMap(K,V) --> ((orderDate, productID), order_itemSubTotal)*
- **Stage 3:** grouping orderJoinMap(K,V) and aggregating the oroduct revenue --> *dailyRevenuePerProductID(K,V) --> ((orderDate, productID), sum(order_itemSubTotal))* and converting to (K,V) --> *dailyRevenuePerProductIDMap(K,V) --> (productID, (orderDate, sum(order_itemSubTotal)))*
- **Stage 4:** Reading **product** data from Local File System and converting to (K,V) *productRDDMap(K,V) --> (productID, productName)*
- **Stage 5:** Joining dailyRevenuePerProductIDMap(K,V) & productRDDMap(K,V) --> *dailyRevenuePerProductNameLocal(K,V) --> ((orderDate, productName), sum(order_itemSubTotal))*

### Spark - DailyProductRevenue - Joining Data using Broadcast Variables
<p align="center">
  <img width="700" height="350" src="https://user-images.githubusercontent.com/7428555/33142228-b135e1b4-cf83-11e7-9d21-2cc923e9412c.PNG">
</p>

- **Stage 0:** Reading **orders** data from HDFS and convering into (K,V) --> *orderMap --> (orderID, orderDate)*
- **Stage 1:** Reading **order_items** data from HDFS and convering into (K,V) --> *orderItemMap -> (orderID, (productID, order_itemSubTotal))*
- **Stage 2:** Joining orderMap & orderItemMap --> *ordersJoin(K,V) --> (orderID, (orderDate ,(productID, order_itemSubTotal)))* and convering into (K,V) --> *orderJoinMap(K,V) --> ((orderDate, productID), order_itemSubTotal)*
- **Stage 3:** grouping orderJoinMap(K,V) and aggregating the oroduct revenue --> *dailyRevenuePerProductID(K,V) --> ((orderDate, productID), sum(order_itemSubTotal))* and converting to (K,V) using broadcast-variable(**product**) -->  *dailyRevenuePerProductName(K,V) --> ((orderDate, sum(order_itemSubTotal)) , productName)*


