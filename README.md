# CNA Azure 1차수 (5조_든든한 아침식사)

# 추가 서비스 시나리오

## 든든한 아침식사

기능적 요구사항

1. 고객이 주문 후 결제가 완료되면 주방팀에서 확인한다.
2. 주방팀의 확인이 되면 주문 내역이 배송팀에게 전달된다.
3. 배송팀이 주문 확인 후 배송 처리한다.
4. 주방팀(매니저)가 주문을 취소하면 주문 취소확인 후 배송 및 결제가 취소된다.
5. 주방팀이 주문 취소를 하면 음식 Catalog 의 재고수량에 반영이 된다.
6. 주문을 생성하고 결제 및 주방이 완료되면 쿠폰이 생성된다.

비기능적 요구사항

1. 트랜잭션
    1. 결제가 되지 않은 주문건은 아예 거래가 성립되지 않아야 한다.
2. 장애격리
    1. 결제시스템이 과중되면 사용자를 잠시동안 받지 않고 잠시후에 하도록 유도한다.  Circuit breaker, fallback
3. 성능
    1. 고객은 자신이 주문한 내용과 배달상태를 나의 주문정보(프론트엔드)에서 확인할 수 있어야 한다.  CQRS
    2. 고객은 등록된 상품에 리뷰를 남길 수 있고 리뷰정보(프론트엔드)에서 확인할 수 있어야 한다. CQRS


# Event Storming 모델
 ![image](https://user-images.githubusercontent.com/25506725/105278413-c1fc2680-5be8-11eb-89df-fb8411e5e348.png)

## 구현 점검

### 모든 서비스 정상 기동 

![image](https://user-images.githubusercontent.com/25506725/105275917-9f1b4380-5be3-11eb-9db1-81afd07e3c09.png)

```
* gateway 설정
http http://gateway:8080/orders
http http://gateway:8080/deliveries
http http://gateway:8080/customers
http http://gateway:8080/myOrders
http http://gateway:8080/pays
http http://gateway:8080/foodCatalogs
http http://gateway:8080/foodCatalogViews
http http://gateway:8080/kithens
http http://gateway:8080/coupons
http http://gateway:8080/reviews
http http://gateway:8080/reviewViews
```

### Kafka 기동 및 모니터링 용 Consumer 연결
```
kubectl -n kafka exec -ti my-kafka-0 -- /usr/bin/kafka-console-consumer --bootstrap-server my-kafka:9092 --topic gmfd --from-beginning
```

### 고객 생성
```
http POST http://gateway:8080/customers name=yang phone=01011 address=korea11 age=27
```
![image](https://user-images.githubusercontent.com/25506725/105141820-11851880-5b3d-11eb-89f9-21fa67e0a203.png)


### FoodCatalog 정보 생성
```
# http POST http://gateway:8080/foodCatalogs name=cake stock=100 price=4.0  
```
![image](https://user-images.githubusercontent.com/25506725/105142051-6cb70b00-5b3d-11eb-92bc-3753eabcc8d4.png)


### 주문 생성
```
http POST http://gateway:8080/orders qty=10 foodcaltalogid=1 customerid=1
```
![image](https://user-images.githubusercontent.com/25506725/105142436-f1098e00-5b3d-11eb-8e66-812b7c2ebeb4.png)

### 주문 확인
```
http http://gateway:8080/orders
```
![image](https://user-images.githubusercontent.com/25506725/105142747-4c3b8080-5b3e-11eb-9289-ee053ed5570c.png)

##### Message 전송 확인 결과
![image](https://user-images.githubusercontent.com/25506725/105153140-6af44400-5b4b-11eb-969f-98f5212fc4d8.png)

##### MyOrder 조회 (CQRS), 현재상태 확인
```
# http http://gateway:8080/myOrders
```
![image](https://user-images.githubusercontent.com/25506725/105147541-ae977f80-5b44-11eb-8890-876c49ff2019.png)

### 수량(foodcatalogView) 확인
```
# http DELETE http://gateway:8080/foodCatalogViews
```
![image](https://user-images.githubusercontent.com/25506725/105154306-c541d480-5b4c-11eb-9f5a-6376a0888259.png)

### Coupon 확인
주방이 완료되면 쿠폰이 생성된다

![image](https://user-images.githubusercontent.com/25506725/105276843-90359080-5be5-11eb-83e2-442c6b7a70fa.png)

### 주방(kithen) 취소
```
# http DELETE http://gateway:8080/kitchens/1
# http http://order:8080/orders
```
![image](https://user-images.githubusercontent.com/25506725/105154618-1356d800-5b4d-11eb-9360-92bed80a707d.png)

##### Kithen 취소, 주문취소, Delivery 취소, Payment 취소 메시지 전송
![image](https://user-images.githubusercontent.com/25506725/105155008-76486f00-5b4d-11eb-9679-cd8444fd87e6.png)

##### kitchen 취소 시 foodcatalog 수량 확인
![image](https://user-images.githubusercontent.com/25506725/105172726-66d42080-5b63-11eb-828c-be733939465a.png)

##### Coupon 확인
주방이 취소되면 취소된 쿠폰도 같이 취소된다

![image](https://user-images.githubusercontent.com/25506725/105278243-6336ad00-5be8-11eb-9814-9e467ee970dc.png)

##### MyOrder 조회 (CQRS), 현재상태 확인
```
# http http://gateway:8080/myOrders
```
![image](https://user-images.githubusercontent.com/25506725/105156335-0aff9c80-5b4f-11eb-8a03-eb7b9b998b66.png)
 
#### review 등록 및 조회 (CQRS)
```
# http http://gateway:8080/reviews username=yang productname=cake content=good
# http http://gateway:8080/reviewView 
```
![image](https://user-images.githubusercontent.com/25506725/105173947-1d84d080-5b65-11eb-8fd3-9975adb5aff2.png)

####Kithen 서비스 장애 시 주문 불가

## 장애격리

주방 취소와 쿠폰 취소는 비동기 통신으로 쿠폰 취소 서비스가 죽은 상태에서도 정상 동작한다

1. coupon 서비스 중지

![image](https://user-images.githubusercontent.com/25506725/105277525-ec4ce480-5be6-11eb-9b64-9391dc62988c.png)
	
![image](https://user-images.githubusercontent.com/25506725/105277585-071f5900-5be7-11eb-9f51-6fa876dc4d48.png)


2. coupon 서비스를 살리면 coupon취소 프로스세스가 진행된다.

![image](https://user-images.githubusercontent.com/25506725/105278275-70ec3280-5be8-11eb-8aad-596b8ab805a5.png)

![image](https://user-images.githubusercontent.com/25506725/105278243-6336ad00-5be8-11eb-9814-9e467ee970dc.png)

## Circuit Breaker 점검

### Hystrix로 설정
application.yml에 Hystrix를 설정

![image](https://user-images.githubusercontent.com/25506725/105178182-0e088600-5b6b-11eb-8216-3339cd68bf27.png)

Coupon 서비스 의 임의 부하 처리 - 400 밀리에서 증감 220 밀리 정도 왔다갔다 하게 설정

![image](https://user-images.githubusercontent.com/25506725/105279826-bc541000-5beb-11eb-8eb8-b5c98972694c.png)

### 실행결과

![image](https://user-images.githubusercontent.com/25506725/105279621-4b145d00-5beb-11eb-9e4a-05b77adbad6b.png)



## Autoscale 점검

1. Deployment.yaml 파일 설정

![image](https://user-images.githubusercontent.com/25506725/105182173-1616f480-5b70-11eb-85ca-d9a5f44d180b.png)

1. HPA 생성 및 설정
 kubectl autoscale deployment kitchen --cpu-percent=10 --min=1 --max=10
 
![image](https://user-images.githubusercontent.com/25506725/105181828-a4d74180-5b6f-11eb-82aa-70eecce67fca.png)

2. Siege 실행 및 pod 개수 확인
AutoScale적용 후 seige를 통해서 부하 테스트 시 pod 개수가 증가함

  ![image](https://user-images.githubusercontent.com/25506725/105190386-49aa4c80-5b79-11eb-94db-927f988b0435.png)


## Readiness Probe 점검
### 설정 확인

![image](https://user-images.githubusercontent.com/25506725/105185808-56787180-5b74-11eb-98d2-3e8719683fbb.png)

1.siege로 계속 호출하는 중에 kubectl set image를 통해서 배포 시 무중단 배포 확인, 

 kubectl set image deployment kitchen order=skcc10.azurecr.io/kitchen:latest
 
2.Readiness 적용 전: 소스배포시 오류 발생

![image](https://user-images.githubusercontent.com/25506725/105185984-8c1d5a80-5b74-11eb-9f1e-a5b77d60a754.png)

3. 적용 후: 소스배포시 100% 수행

![image](https://user-images.githubusercontent.com/25506725/105186132-b96a0880-5b74-11eb-8d6a-a3fa3a5e981c.png)


