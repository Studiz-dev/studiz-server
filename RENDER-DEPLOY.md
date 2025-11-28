# 🚀 Render 배포 가이드

## Render란?

Render는 간단한 클라우드 플랫폼으로, GitHub에 푸시하면 자동으로 배포됩니다.

## 배포 방법

### 방법 1: Blueprint (자동) ⭐ 추천

`render.yaml` 파일을 사용하면 서비스와 데이터베이스를 자동으로 생성합니다.

#### 1. GitHub에 코드 푸시

```bash
git add .
git commit -m "Render 배포 준비"
git push origin main
```

#### 2. Render에서 Blueprint 생성

1. [Render 대시보드](https://dashboard.render.com) 접속
2. **New +** → **Blueprint** 클릭
3. GitHub 저장소 선택 및 연결
4. `render.yaml` 파일이 자동으로 감지됨
5. **Apply** 클릭

#### 3. 완료!

Render가 자동으로:
- ✅ PostgreSQL 데이터베이스 생성
- ✅ 웹 서비스 생성 및 배포
- ✅ 환경 변수 자동 연결
- ✅ Health check 설정

---

### 방법 2: 수동 생성

Blueprint를 사용하지 않고 수동으로 생성할 수도 있습니다.

#### 1. 데이터베이스 생성

1. **New +** → **PostgreSQL** 클릭
2. 설정:
   - Name: `studiz-postgres`
   - Database: `studiz`
   - User: `studiz`
   - Region: 원하는 지역 선택
3. **Create Database** 클릭

#### 2. 웹 서비스 생성

1. **New +** → **Web Service** 클릭
2. GitHub 저장소 연결
3. 설정:
   - **Name**: `studiz-server`
   - **Environment**: `Docker`
   - **Region**: 데이터베이스와 같은 지역
   - **Branch**: `main` (또는 원하는 브랜치)
   - **Root Directory**: `.` (루트)
   - **Dockerfile Path**: `./Dockerfile`
4. **Environment Variables** 추가:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DB_HOST=<데이터베이스 호스트>
   DB_PORT=<데이터베이스 포트>
   DB_NAME=studiz
   DB_USERNAME=studiz
   DB_PASSWORD=<데이터베이스 비밀번호>
   JWT_SECRET=<32자 이상의 랜덤 문자열>
   SPRING_DATASOURCE_URL=<데이터베이스 연결 문자열>
   ```
   (데이터베이스 정보는 데이터베이스 페이지의 **Connections** 탭에서 확인)
5. **Create Web Service** 클릭

### 3. 환경 변수 설정 (선택사항)

Render는 `render.yaml`에서 자동으로 설정하지만, 수동으로 추가하려면:

**Settings → Environment Variables**에서:
- `JWT_SECRET`: 자동 생성되거나 수동 입력 (최소 32자)
- 기타 필요한 환경 변수

### 4. 배포 확인

배포가 완료되면:
- **Logs** 탭에서 배포 로그 확인
- 제공된 URL로 접속 (예: `https://studiz-server.onrender.com`)
- Swagger UI: `https://studiz-server.onrender.com/api/swagger-ui.html`

## render.yaml 설명

현재 프로젝트는 **Blueprint 방식**을 사용합니다.

### render.yaml 구조

- **services**: 웹 서비스 설정
  - `runtime: docker`: Docker 기반 배포
  - `dockerfilePath`: Dockerfile 경로
  - `envVars`: 환경 변수 설정 (데이터베이스 정보 자동 연결)
- **databases**: PostgreSQL 데이터베이스 자동 생성
  - `name`: 데이터베이스 이름
  - `plan`: 플랜 (starter = 무료)

### Blueprint vs 수동 생성

| 항목 | Blueprint | 수동 생성 |
|------|-----------|-----------|
| 설정 파일 | `render.yaml` 필요 | 웹 UI에서 설정 |
| 데이터베이스 | 자동 생성 및 연결 | 수동 생성 및 연결 |
| 환경 변수 | 자동 설정 | 수동 입력 |
| 추천 | ✅ 간단하고 빠름 | 복잡하지만 세밀한 제어 가능 |

**결론**: Blueprint 방식이 훨씬 간단하고 추천합니다!

## 배포 후 확인

```bash
# Health check
curl https://your-app.onrender.com/api/swagger-ui.html

# API 문서
curl https://your-app.onrender.com/api/api-docs
```

## 주의사항

1. **무료 플랜 제한**:
   - 15분간 요청이 없으면 서비스가 sleep 상태가 됩니다
   - 첫 요청 시 깨우는데 시간이 걸릴 수 있습니다

2. **데이터베이스**:
   - Render가 자동으로 PostgreSQL을 생성하고 연결합니다
   - 환경 변수는 자동으로 설정됩니다

3. **JWT_SECRET**:
   - `generateValue: true`로 설정되어 자동 생성됩니다
   - 또는 수동으로 설정 가능합니다

## 트러블슈팅

### 배포 실패 시

1. **Logs 탭 확인**: 에러 메시지 확인
2. **Build Logs 확인**: Docker 빌드 에러 확인
3. **환경 변수 확인**: 필수 변수가 모두 설정되었는지 확인

### 서비스가 응답하지 않을 때

- 무료 플랜의 경우 15분 sleep 후 첫 요청이 느릴 수 있습니다
- Health check가 실패하면 서비스 상태 확인

## 업데이트 배포

GitHub에 푸시하면 자동으로 재배포됩니다:

```bash
git push origin main
```

Render가 자동으로 감지하고 배포를 시작합니다.

