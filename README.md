# Studiz Server

스터디 관리 플랫폼 백엔드 서버

## 기술 스택

- **Java 17**
- **Spring Boot 3.2.0**
- **PostgreSQL 15**
- **Spring Data JPA**
- **Spring Security + JWT**
- **Swagger/OpenAPI 3.0**

## 개발 환경 세팅

### 1. 필수 요구사항

- Java 17 이상
- Docker & Docker Compose (데이터베이스용)
- IntelliJ IDEA (권장)

### 2. 데이터베이스 실행

```bash
# PostgreSQL 컨테이너 실행
docker-compose up -d

# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f postgres
```

**데이터베이스 접속 정보:**
- Host: localhost
- Port: 5432
- Database: studiz
- Username: studiz
- Password: studiz

### 3. 프로젝트 실행

#### IntelliJ IDEA에서 실행

1. 프로젝트 열기: `File > Open` → `studiz-server` 폴더 선택
2. Gradle 동기화 대기
3. `StudizServerApplication.java` 실행

#### 터미널에서 실행

```bash
# Gradle 빌드 및 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/studiz-server-0.0.1-SNAPSHOT.jar
```

### 4. API 문서 확인

서버 실행 후 아래 URL에서 Swagger UI 확인 가능:

```
http://localhost:8080/api/swagger-ui.html
```

## 프로젝트 구조

```
src/main/java/com/studiz/
├── StudizServerApplication.java
├── domain/
│   ├── user/              # 사용자 관리 (박지현)
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   └── controller/
│   ├── auth/              # 인증/인가 (박지현)
│   │   ├── dto/
│   │   ├── service/
│   │   └── controller/
│   ├── club/              # 동아리 관리 (박지현)
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   └── controller/
│   ├── study/             # 스터디 관리 (김서준)
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   └── controller/
│   └── studymember/       # 스터디 멤버 관리 (김서준)
│       ├── entity/
│       ├── repository/
│       ├── service/
│       └── controller/
├── global/                # 공통 모듈 (박지현)
│   ├── config/            # 설정 (Security, Swagger 등)
│   ├── security/          # JWT, Filter 등
│   ├── exception/         # 예외 처리
│   └── common/            # 공통 응답, 유틸리티
└── resources/
    └── application.yml
```

## ERD

### Users (사용자)
- id (PK)
- login_id (unique)
- password
- name
- profile_image_url
- role (USER, ADMIN)
- status (ACTIVE, INACTIVE, SUSPENDED)
- created_at
- updated_at

### Clubs (동아리)
- id (PK)
- name
- description
- logo_image_url
- status (ACTIVE, INACTIVE)
- created_at
- updated_at

### Studies (스터디)
- id (PK)
- name
- invite_code (unique)
- club_id (FK → Clubs)
- owner_id (FK → Users)
- description
- status (ACTIVE, INACTIVE, COMPLETED)
- created_at
- updated_at

### StudyMembers (스터디 멤버)
- id (PK)
- study_id (FK → Studies)
- user_id (FK → Users)
- role (OWNER, MEMBER)
- joined_at
- created_at

## 역할 분담

### 박지현
- 사용자 인증 및 기본 정보 관리 (Auth, Users)
- 동아리(Clubs) 관련 기능
- 공통 모듈 (JWT 인증, 예외 처리, API 응답 포맷)

### 김서준
- 스터디(Studies) 관리
- 스터디 멤버(StudyMembers) 관리
- 초대 코드 및 참여 기능
- 권한 관리 로직

## 개발 가이드

### API 응답 포맷

```json
{
  "success": true,
  "data": { },
  "message": "성공",
  "timestamp": "2025-10-12T10:30:00"
}
```

### 에러 응답 포맷

```json
{
  "success": false,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  },
  "timestamp": "2025-10-12T10:30:00"
}
```

### JWT 인증

- Access Token: 1시간 유효
- Refresh Token: 7일 유효
- Header: `Authorization: Bearer {token}`

## 유용한 명령어

```bash
# 데이터베이스 중지
docker-compose down

# 데이터베이스 초기화 (데이터 삭제)
docker-compose down -v

# Gradle 캐시 정리
./gradlew clean

# 테스트 실행
./gradlew test

# 빌드 (테스트 제외)
./gradlew build -x test
```

## 환경 변수

필요시 환경 변수로 설정 가능:

- `DB_USERNAME`: 데이터베이스 사용자명 (기본값: studiz)
- `DB_PASSWORD`: 데이터베이스 비밀번호 (기본값: studiz)
- `JWT_SECRET`: JWT 시크릿 키 (프로덕션에서 필수 변경)

## 문제 해결

### Port 5432 already in use
PostgreSQL이 이미 실행 중인 경우, 기존 프로세스를 종료하거나 docker-compose.yml에서 포트 변경

### Gradle 동기화 실패
```bash
./gradlew --refresh-dependencies
```

### 데이터베이스 연결 실패
1. Docker 컨테이너가 실행 중인지 확인
2. application.yml의 데이터베이스 설정 확인
3. 방화벽/보안 그룹 설정 확인

