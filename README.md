# marketing
server for hotel booking system, developed by akka-http


1 http://139.129.25.229:1121/marketing/hotel/getInfo?hotelId=1 获取酒店信息

2 http://139.129.25.229:1121/marketing/room/list?hotelId=1&dateBegin=20170411&dateEnd=20170413 房间列表

3 http://localhost:1121/marketing/order/create 创建订单
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

4、http://139.129.25.229:1121/marketing/order/getMyOrder