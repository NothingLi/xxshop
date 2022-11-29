mvn clean && ^
mvn package && ^
docker build -t xx-shop-backend . && ^
docker tag xx-shop-backend harbor.bielai.top/xxshop/xx-shop-backend && ^
docker push harbor.bielai.top/xxshop/xx-shop-backend && ^
ssh root@bielai.top "kubectl -n xxshop delete po xxshop-api-pod-0"