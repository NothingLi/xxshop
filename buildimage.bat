mvn clean && ^
mvn package && ^
docker build -t xx-shop-backend:2.0 . && ^
docker tag xx-shop-backend:2.0 harbor.bielai.top/xxshop/xx-shop-backend:2.0 && ^
docker push harbor.bielai.top/xxshop/xx-shop-backend:2.0 && ^
ssh root@bielai.top "kubectl -n xxshop delete po xxshop-api-pod-0"