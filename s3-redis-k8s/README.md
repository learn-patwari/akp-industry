# AKP Industry — S3 Folder Hierarchy API

This project is a robust **Spring Boot microservice** that organizes a flat AWS S3 folder structure (like `YYYYMMDD`) into a virtual year → month → day hierarchy.

Key features:
- 📁 **Dynamic hierarchy**: Splits flat folders like `20240504` into `2024` → `05` → `04`
- 🚀 **Fast**: Uses **Redis cache** to handle millions of records efficiently
- 🔄 **Self-managed cache**: Clears daily at 3 AM KST and rebuilds automatically at 5 AM KST
- ☸️ **Cloud-native**: Ready to deploy on Kubernetes with multiple pods for high availability

---

## 📦 Features

✅ Fetch folders by year/month/day  
✅ Returns children as `List<FolderDTO>` with subfolder and file counts  
✅ Uses AWS SDK for Java to connect to S3  
✅ Caches results in Redis to avoid repeated S3 calls  
✅ Kubernetes-ready (includes ConfigMap, Secret, Deployment, and Service YAMLs)  
✅ Uses Spring Boot 2.x (Java 8 compatible) and Lombok

---

## 🚀 How to Deploy

1. **Install Redis in Kubernetes**

   ```bash
   helm repo add bitnami https://charts.bitnami.com/bitnami
   helm install my-redis bitnami/redis --namespace test_ns --set auth.enabled=false
