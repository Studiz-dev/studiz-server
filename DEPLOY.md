# 배포 가이드

## 배포 방법

### 1. 환경 변수 설정

`.env.prod` 파일을 생성하고 필요한 환경 변수를 설정하세요:

```bash
cp .env.example .env.prod
```

`.env.prod` 파일을 편집하여 실제 값으로 변경:
- `DB_PASSWORD`: 데이터베이스 비밀번호
- `JWT_SECRET`: JWT 시크릿 키 (충분히 긴 랜덤 문자열)

### 2. Docker를 사용한 배포 (권장)

#### 프로덕션 배포
```bash
# 배포 스크립트 실행
chmod +x deploy.sh
./deploy.sh prod
```

또는 수동으로:
```bash
# JAR 빌드
./gradlew clean build -x test

# Docker 이미지 빌드
docker build -t studiz-server:latest .

# Docker Compose로 실행
docker-compose -f docker-compose.prod.yml up -d
```

#### 개발 환경 배포
```bash
docker-compose up -d
```

### 3. JAR 파일 직접 실행

```bash
# JAR 빌드
./gradlew clean build -x test

# JAR 실행
java -jar build/libs/studiz-server-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.password=${DB_PASSWORD} \
  --jwt.secret=${JWT_SECRET}
```

### 4. 배포 확인

```bash
# 컨테이너 상태 확인
docker-compose -f docker-compose.prod.yml ps

# 로그 확인
docker-compose -f docker-compose.prod.yml logs -f app

# Health check
curl http://localhost:8080/api/swagger-ui.html
```

### 5. 배포 중지

```bash
docker-compose -f docker-compose.prod.yml down
```

## 환경별 설정

- **개발 환경**: `application.yml` (로컬 PostgreSQL)
- **프로덕션 환경**: `application-prod.yml` (환경 변수 사용)

## 주의사항

1. **프로덕션 환경 변수**:
   - `JWT_SECRET`: 충분히 긴 랜덤 문자열 사용 (최소 32자 이상)
   - `DB_PASSWORD`: 강력한 비밀번호 사용

2. **데이터베이스**:
   - 프로덕션에서는 `ddl-auto: validate` 사용 (스키마 자동 생성 비활성화)
   - 마이그레이션은 별도로 관리 권장

3. **보안**:
   - `.env.prod` 파일은 절대 Git에 커밋하지 마세요
   - `.gitignore`에 추가되어 있습니다

4. **로그**:
   - 프로덕션에서는 로그 레벨을 INFO로 설정
   - 로그 파일은 `logs/studiz-server.log`에 저장

## 트러블슈팅

### 포트 충돌
```bash
# 포트 사용 중인 프로세스 확인
lsof -i :8080
lsof -i :5432
```

### 데이터베이스 연결 실패
- PostgreSQL 컨테이너가 실행 중인지 확인
- 환경 변수가 올바르게 설정되었는지 확인

### 메모리 부족
- JVM 옵션 조정: `-Xmx512m -Xms256m`

