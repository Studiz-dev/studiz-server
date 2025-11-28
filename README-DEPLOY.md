# 🚀 배포 가이드

## 빠른 시작

### 1. 환경 변수 설정 (자동 생성) ⭐ 추천

```bash
# 환경 변수 자동 생성 스크립트 실행
./setup-env.sh
```

이 스크립트가:
- JWT Secret을 자동으로 생성합니다
- 데이터베이스 비밀번호를 안전하게 입력받습니다
- `.env.prod` 파일을 자동으로 생성합니다

### 1-1. 환경 변수 수동 설정

자동 스크립트를 사용하지 않는 경우:

```bash
# .env.prod 파일 생성
cat > .env.prod << EOF
DB_PASSWORD=원하는-데이터베이스-비밀번호
JWT_SECRET=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
EOF
```

**비밀번호 생성 팁:**
- **DB_PASSWORD**: 원하는 비밀번호를 직접 입력 (예: `MySecurePass123!`)
- **JWT_SECRET**: 위 명령어가 자동으로 생성하거나, 최소 32자 이상의 랜덤 문자열 사용

### 2. 배포 실행

```bash
# 배포 스크립트 실행
./deploy.sh prod
```

## 배포 방법

### 방법 1: Docker Compose (권장) ⭐

가장 간단한 방법입니다. 데이터베이스와 애플리케이션을 함께 배포합니다.

```bash
# 1. 환경 변수 설정
cp .env.example .env.prod
# .env.prod 파일을 편집하여 실제 값 입력

# 2. 배포
docker-compose -f docker-compose.prod.yml up -d

# 3. 로그 확인
docker-compose -f docker-compose.prod.yml logs -f app
```

### 방법 2: JAR 파일 직접 실행

```bash
# 1. JAR 빌드
./gradlew clean build -x test

# 2. 환경 변수 설정 후 실행
export DB_PASSWORD=your-password
export JWT_SECRET=your-secret
java -jar build/libs/studiz-server-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

### 방법 3: 배포 스크립트 사용

```bash
# 프로덕션 배포
./deploy.sh prod

# 개발 환경 배포
./deploy.sh dev
```

## 환경 변수

### 필수 환경 변수

| 변수명 | 설명 | 생성 방법 |
|--------|------|-----------|
| `DB_PASSWORD` | PostgreSQL 비밀번호 | 직접 입력 (예: `MyPassword123!`) |
| `JWT_SECRET` | JWT 토큰 시크릿 키 (최소 32자) | 자동 생성 또는 수동 입력 |

### 비밀번호 생성 방법

#### 방법 1: 자동 생성 (추천) ⭐
```bash
./setup-env.sh
```

#### 방법 2: 수동 생성

**JWT Secret 생성:**
```bash
# macOS/Linux
openssl rand -base64 32 | tr -d "=+/" | cut -c1-32

# 또는
openssl rand -hex 32
```

**DB Password:**
- 원하는 비밀번호를 직접 입력하세요
- 예: `MySecurePassword123!`, `studiz2024!pass`

선택 환경 변수:

| 변수명 | 기본값 | 설명 |
|--------|--------|------|
| `DB_HOST` | `postgres` | 데이터베이스 호스트 |
| `DB_PORT` | `5432` | 데이터베이스 포트 |
| `DB_NAME` | `studiz` | 데이터베이스 이름 |
| `DB_USERNAME` | `studiz` | 데이터베이스 사용자명 |
| `SERVER_PORT` | `8080` | 서버 포트 |

## 배포 확인

### Health Check

```bash
# Swagger UI 접속
curl http://localhost:8080/api/swagger-ui.html

# API 문서 확인
curl http://localhost:8080/api/api-docs
```

### 로그 확인

```bash
# 실시간 로그
docker-compose -f docker-compose.prod.yml logs -f app

# 최근 100줄
docker-compose -f docker-compose.prod.yml logs --tail=100 app
```

### 컨테이너 상태

```bash
# 실행 중인 컨테이너 확인
docker-compose -f docker-compose.prod.yml ps

# 리소스 사용량
docker stats studiz-server-prod
```

## 배포 중지

```bash
# 컨테이너 중지 및 제거
docker-compose -f docker-compose.prod.yml down

# 볼륨까지 제거 (데이터 삭제됨!)
docker-compose -f docker-compose.prod.yml down -v
```

## 프로덕션 체크리스트

배포 전 확인사항:

- [ ] `.env.prod` 파일에 실제 비밀번호 설정
- [ ] `JWT_SECRET`이 충분히 긴 랜덤 문자열인지 확인 (최소 32자)
- [ ] 데이터베이스 백업 (기존 데이터가 있는 경우)
- [ ] 포트 8080, 5432가 사용 가능한지 확인
- [ ] 방화벽 설정 확인 (필요한 경우)

## 트러블슈팅

### 포트가 이미 사용 중입니다

```bash
# 포트 사용 중인 프로세스 확인
lsof -i :8080
lsof -i :5432

# 프로세스 종료
kill -9 <PID>
```

### 데이터베이스 연결 실패

```bash
# PostgreSQL 컨테이너 상태 확인
docker-compose -f docker-compose.prod.yml ps postgres

# PostgreSQL 로그 확인
docker-compose -f docker-compose.prod.yml logs postgres

# 컨테이너 재시작
docker-compose -f docker-compose.prod.yml restart postgres
```

### 메모리 부족

Dockerfile의 JVM 옵션 수정:
```dockerfile
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
```

### 로그 파일 위치

- Docker: `docker-compose -f docker-compose.prod.yml logs app`
- JAR 직접 실행: `logs/studiz-server.log`

## 추가 리소스

- [Docker 공식 문서](https://docs.docker.com/)
- [Spring Boot 배포 가이드](https://spring.io/guides/gs/spring-boot-docker/)

