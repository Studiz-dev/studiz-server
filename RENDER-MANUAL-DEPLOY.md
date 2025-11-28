# 🚀 Render 수동 배포 가이드 (Blueprint 없이)

Render에서 Blueprint 없이 일반 프로젝트로 배포하는 방법입니다.

## 배포 방법

### 1. PostgreSQL 데이터베이스 생성

1. [Render 대시보드](https://dashboard.render.com) 접속
2. **New +** → **PostgreSQL** 클릭
3. 설정:
   - **Name**: `studiz-postgres`
   - **Database**: `studiz`
   - **User**: `studiz`
   - **Region**: 원하는 지역 선택 (예: Singapore)
   - **PostgreSQL Version**: 15
   - **Plan**: Free
4. **Create Database** 클릭
5. 데이터베이스 생성 완료 대기 (1-2분)

### 2. 웹 서비스 생성

1. **New +** → **Web Service** 클릭
2. GitHub 저장소 연결:
   - **Connect account** 또는 **Connect repository** 클릭
   - GitHub 권한 승인
   - 저장소 선택 (`studiz-server`)
3. 저장소 선택 후 설정:
   - **Name**: `studiz-server`
   - **Region**: 데이터베이스와 같은 지역
   - **Branch**: `main`
   - **Root Directory**: `.` (루트)
   - **Environment**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Docker Context**: `.`
4. **Advanced** 클릭하여 추가 설정:
   - **Auto-Deploy**: Yes (GitHub 푸시 시 자동 배포)
   - **Health Check Path**: 비워두기 (또는 `/api/api-docs`)

### 3. 환경 변수 설정

**Settings** → **Environment Variables**에서 다음 환경 변수 추가:

```
SPRING_PROFILES_ACTIVE=prod
DB_HOST=<데이터베이스 호스트>
DB_PORT=<데이터베이스 포트>
DB_NAME=studiz
DB_USERNAME=studiz
DB_PASSWORD=<데이터베이스 비밀번호>
JWT_SECRET=<32자 이상의 랜덤 문자열>
```

**데이터베이스 정보 확인 방법:**
1. PostgreSQL 서비스 페이지로 이동
2. **Connections** 탭 클릭
3. **Internal Database URL** 또는 개별 필드 확인:
   - **Host**: `dpg-xxxxx-a` 형식
   - **Port**: `5432`
   - **Database**: `studiz`
   - **User**: `studiz`
   - **Password**: 표시된 비밀번호 복사

**JWT_SECRET 생성:**
- 온라인 랜덤 문자열 생성기 사용
- 또는 터미널에서: `openssl rand -base64 32`
- 최소 32자 이상

### 4. 배포 확인

1. **Logs** 탭에서 배포 상태 확인
2. 배포 완료 후 제공된 URL 확인 (예: `https://studiz-server.onrender.com`)
3. Swagger UI 접속: `https://studiz-server.onrender.com/api/swagger-ui.html`

## 주의사항

### 1. 데이터베이스 연결 URL 형식

Render는 `postgresql://` 형식의 URL을 제공하지만, PostgreSQL JDBC 드라이버는 `jdbc:postgresql://` 형식을 요구합니다.

**해결 방법:**
- `SPRING_DATASOURCE_URL` 환경 변수를 **설정하지 마세요**
- 대신 개별 필드(`DB_HOST`, `DB_PORT`, `DB_NAME`)만 사용
- `DataSourceConfig.java`가 자동으로 올바른 형식으로 변환합니다

### 2. Health Check

- Health Check Path를 비워두면 Render가 기본적으로 포트 8080을 확인합니다
- 또는 `/api/api-docs`로 설정 (가벼운 엔드포인트)

### 3. 무료 플랜 제한

- 15분간 요청이 없으면 서비스가 sleep 상태가 됩니다
- 첫 요청 시 깨우는데 시간이 걸릴 수 있습니다 (30초~1분)

## 트러블슈팅

### 배포 실패 시

1. **Logs** 탭에서 에러 확인
2. 환경 변수가 모두 설정되었는지 확인
3. `DB_HOST`, `DB_PORT` 등이 올바른지 확인
4. `JWT_SECRET`이 32자 이상인지 확인

### 데이터베이스 연결 실패

- `SPRING_DATASOURCE_URL` 환경 변수가 있다면 **삭제**
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`만 사용
- 데이터베이스가 완전히 생성되었는지 확인 (1-2분 소요)

### 403 에러

- `/api/swagger-ui.html`로 접속
- `/api/` 루트 경로도 접근 가능해야 함

## 업데이트 배포

GitHub에 푸시하면 자동으로 재배포됩니다:

```bash
git push origin main
```

또는 Render 대시보드에서 **Manual Deploy** → **Deploy latest commit** 클릭

## Blueprint vs 수동 배포

| 항목 | Blueprint | 수동 배포 |
|------|-----------|-----------|
| 설정 파일 | `render.yaml` 필요 | 웹 UI에서 설정 |
| 데이터베이스 | 자동 생성 및 연결 | 수동 생성 및 연결 |
| 환경 변수 | 자동 설정 | 수동 입력 |
| 추천 | ✅ 코드로 관리 | ✅ 세밀한 제어 |

**수동 배포의 장점:**
- Blueprint 파일 불필요
- 웹 UI에서 직접 제어
- 설정 변경이 쉬움

