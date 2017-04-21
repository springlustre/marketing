# marketing
server for hotel booking system, developed by akka-http


1 http://139.129.25.229:1121/marketing/hotel/getInfo?hotelId=1 获取酒店信息

2 http://139.129.25.229:1121/marketing/room/list?hotelId=1&dateBegin=20170411&dateEnd=20170413 房间列表

3 http://localhost:1121/marketing/order/create 创建订单
  ```
  {
    "hotelId":1,
    "roomId":1,
    "packageId":1,
    "dateStart":"20170412",
    "dateEnd":"20170413",
    "comeTime":"1371234567800",
    "totalPrice":300.2,
    "priceInfo":"20170413:200",
    "userName":"王春泽",
    "userMobile":"13512345678",
    "userIdentifyNum":"132123456778992222",
    "invoice":"抬头",
    "remark":"备注"
  }
  ```

4、http://139.129.25.229:1121/marketing/order/getMyOrder

5、http://localhost:1121/marketing/order/getDetail?orderId=1 获取订单详情

## for business
1、login http://localhost:1121/marketing/account/login?login=test@test&password=test@test

2、create hotel: http://localhost:1121/marketing/hotel/create
```
 {
 	   "name": "testName",
     "logo": "http://p3.so.qhimgs1.com/bdr/_240_/t01b0bc44b5cc8236f3.jpg",
     "img": "http://p3.so.qhimgs1.com/bdr/_240_/t01b0bc44b5cc8236f3.jpg",
     "address": "test address",
     "longitude": 137,
     "latitude": 40,
     "phone": "010-12345678",
     "star": 3,
     "openTime": "2017-04-20",
     "description": "停车场:免费"
 }
 ```
 
 3、create room  http://localhost:1121/marketing/room/create
 
 ```aidl
{
	"hotelId":1,
    "name":"豪华间",
    "logo":"http://pavo.elongstatic.com/i/Hotel120_120/0000gmP6.jpg",
    "img":"http://pavo.elongstatic.com/i/Hotel120_120/0000gmP6.jpg",
    "stock":30,
    "description":"面积:30,床数:2,尺寸:2m"
}
```

4,setPrice http://localhost:1121/marketing/room/setPrice
```aidl
{
	"hotelId":1,
    "roomList":[{"roomId":1,"packageId":1,"price":100},{"roomId":2,"packageId":2,"price":100},{"roomId":3,"packageId":3,"price":100}],
    "startTime":"20170419",
    "endTime":"20170425"
}
```

5,modify hotel  http://localhost:1121/marketing/hotel/modify

```
 {
     "id":1,
     "name": "testName",
     "logo": "http://p3.so.qhimgs1.com/bdr/_240_/t01b0bc44b5cc8236f3.jpg",
     "img": "http://p3.so.qhimgs1.com/bdr/_240_/t01b0bc44b5cc8236f3.jpg",
     "address": "test address",
     "longitude": 137,
     "latitude": 40,
     "phone": "010-12345678",
     "star": 3,
     "openTime": "2017-04-20",
     "description": "停车场:免费"
 }
 ```

6、modify room  http://localhost:1121/marketing/room/modify
 ```aidl
{
	"roomId":4,
    "name":"超豪华间",
    "logo":"http://pavo.elongstatic.com/i/Hotel120_120/0000gmP6.jpg",
    "img":"http://pavo.elongstatic.com/i/Hotel120_120/0000gmP6.jpg",
    "stock":30,
    "description":"面积:30,床数:2,尺寸:2m"
}
```
7、list room by business
   http://localhost:1121/marketing/room/listByBusiness?hotelId=1

 返回数据和c端list接口一样
 
 
 
 
 