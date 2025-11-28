# 🚂 Railway 배포 가이드

Railway는 무료 플랜을 제공하는 간단한 클라우드 플랫폼입니다. Render보다 설정이 간단하고 안정적입니다.

## Railway란?

- ✅ **무료 플랜**: 월 $5 크레딧 제공 (개인 프로젝트에 충분)
- ✅ **Docker 지원**: Dockerfile 자동 인식
- ✅ **PostgreSQL**: 무료 데이터베이스 제공
- ✅ **GitHub 연동**: 자동 배포
- ✅ **간단한 설정**: 몇 번의 클릭으로 완료

## 배포 방법

### 1. Railway 가입

1. [Railway](https://railway.app) 접속
2. **GitHub로 로그인** 클릭
3. GitHub 권한 승인

### 2. 프로젝트 생성

1. Railway 대시보드에서 **New Project** 클릭
2. **Deploy from GitHub repo** 선택
3. 저장소 선택 (`studiz-server`)
4. **Deploy Now** 클릭

### 3. PostgreSQL 데이터베이스 추가

1. 프로젝트 페이지에서 **+ New** 클릭
2. **Database** → **Add PostgreSQL** 선택
3. 자동으로 데이터베이스가 생성됩니다

### 4. 환경 변수 설정

프로젝트 페이지에서 **Variables** 탭 클릭 후 다음 환경 변수 추가:

```
SPRING_PROFILES_ACTIVE=prod
DB_HOST=${{Postgres.PGHOST}}
DB_PORT=${{Postgres.PGPORT}}
DB_NAME=${{Postgres.PGDATABASE}}
DB_USERNAME=${{Postgres.PGUSER}}
DB_PASSWORD=${{Postgres.PGPASSWORD}}
JWT_SECRET=<32자 이상의 랜덤 문자열>
```

**참고**: 
- `${{Postgres.XXX}}` 형식은 Railway가 자동으로 데이터베이스 정보를 제공합니다
- `JWT_SECRET`은 직접 입력하거나 Railway의 **Generate** 버튼 사용

### 5. 포트 설정 (선택사항)

Railway는 자동으로 포트를 할당하지만, Spring Boot가 `PORT` 환경 변수를 읽도록 설정할 수 있습니다:

**Variables**에 추가:
```
PORT=8080
```

또는 `application-prod.yml`에 다음 추가:
```yaml
server:
  port: ${PORT:8080}
```

### 6. 도메인 생성 및 확인

**도메인 생성 방법:**

1. 프로젝트 페이지에서 배포된 서비스(웹 서비스)를 클릭
2. **Settings** 탭 클릭
3. **Generate Domain** 버튼 클릭
4. 자동으로 생성된 도메인 확인 (예: `studiz-server.up.railway.app`)

**또는:**

1. 프로젝트 페이지에서 배포된 서비스 카드를 클릭
2. 상단에 **Public Domain** 섹션이 보이면 클릭
3. **Generate Domain** 클릭

**도메인 확인 위치:**

- 프로젝트 페이지의 서비스 카드 상단
- 서비스 페이지의 **Settings** 탭
- 서비스 페이지 상단의 **Public Domain** 섹션

### 7. 배포 확인

1. **Deployments** 탭에서 배포 상태 확인
2. 생성된 도메인으로 접속 (예: `https://studiz-server.up.railway.app`)
3. Swagger UI: `https://studiz-server.up.railway.app/api/swagger-ui.html`

## Railway vs Render

| 항목 | Railway | Render |
|------|---------|--------|
| 무료 플랜 | ✅ 월 $5 크레딧 | ✅ 무료 (제한적) |
| 설정 난이도 | ⭐ 매우 쉬움 | ⭐⭐ 보통 |
| Docker 지원 | ✅ 자동 인식 | ✅ 지원 |
| 데이터베이스 | ✅ 자동 연결 | ✅ 자동 연결 |
| Sleep 모드 | ❌ 없음 | ⚠️ 15분 후 sleep |
| 추천 | ✅✅✅ | ✅✅ |

## 주의사항

1. **무료 플랜 제한**:
   - 월 $5 크레딧 제공
   - 사용량이 적으면 충분히 무료로 사용 가능
   - 사용량이 많으면 유료 플랜 필요

2. **데이터베이스**:
   - Railway가 자동으로 PostgreSQL을 생성하고 연결
   - 환경 변수는 `${{Postgres.XXX}}` 형식으로 자동 연결

3. **자동 배포**:
   - GitHub에 푸시하면 자동으로 재배포
   - 수동 배포도 가능

## 트러블슈팅

### 배포 실패 시

1. **Logs** 탭에서 에러 확인
2. 환경 변수가 모두 설정되었는지 확인
3. `JWT_SECRET`이 32자 이상인지 확인

### 데이터베이스 연결 실패

- 환경 변수 이름 확인 (`DB_HOST`, `DB_PORT` 등)
- `${{Postgres.XXX}}` 형식이 올바른지 확인

## 업데이트 배포

GitHub에 푸시하면 자동으로 재배포됩니다:

```bash
git push origin main
```

Railway가 자동으로 감지하고 배포를 시작합니다.

