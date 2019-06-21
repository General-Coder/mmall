## aglio 使用方法 doc
```bash
aglio -i api_md/index.md  --theme-template triple -s

aglio -i api_md/index.md -o index.html

```

## dredd 使用方法 test
```bash
dredd index.md http://127.0.0.1:8888  --hookfiles=./hooks.js 

```

## drakov 使用方法 mockserver
```bash
drakov -f index.md -p 8080 --ignoreHeader Accept --ignoreHeader Content-Type 

```

## 数据模型对象

<!-- include(后台_产品接口.md) -->
<!-- include(后台_品类接口.md) -->
<!-- include(后台_用户接口.md) -->
<!-- include(后台_统计接口.md) -->
<!-- include(后台_订单接口.md) -->
<!-- include(门户_产品接口.md) -->
<!-- include(门户_支付接口.md) -->
<!-- include(门户_收货地址接口.md) -->
<!-- include(门户_用户接口.md) -->
<!-- include(门户_订单接口.md) -->
<!-- include(门户_购物车接口.mdn) -->
