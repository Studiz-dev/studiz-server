# 🚀 Render Blueprint 배포 가이드

Render Blueprint를 사용하면 `render.yaml` 파일 하나로 모든 서비스를 자동 생성합니다.

## 배포 방법

### 1. GitHub에 코드 푸시

```bash
git add .
git commit -m "Render Blueprint 배포 준비"
git push origin main
```

### 2. Render GitHub App 설치 (처음만)

1. [Render 대시보드](https://dashboard.render.com) 접속
2. **New +** → **Blueprint** 클릭
3. **"Install Render"** 또는 **"Connect GitHub"** 버튼 클릭
4. GitHub 로그인 및 권한 승인
5. 저장소 선택 (또는 전체 조직 선택)
6. **Install** 또는 **Approve** 클릭

### 3. Blueprint 배포

1. **New +** → **Blueprint** 클릭
2. GitHub 저장소 선택 (`studiz-server`)
3. `render.yaml` 파일이 자동으로 감지됨
4. **Apply** 클릭

### 4. 완료!

Render가 자동으로:
- ✅ PostgreSQL 데이터베이스 생성 (`studiz-postgres`)
- ✅ 웹 서비스 생성 및 배포 (`studiz-server`)
- ✅ 환경 변수 자동 연결
- ✅ 데이터베이스 연결 정보 자동 설정

## render.yaml 설명

### 서비스 설정

```yaml
services:
  - type: web              # 웹 서비스
    name: studiz-server    # 서비스 이름
    runtime: docker        # Docker 기반 배포
    plan: free            # 무료 플랜
    dockerfilePath: ./Dockerfile
    dockerContext: .
```

### 환경 변수

```yaml
envVars:
  - key: SPRING_PROFILES_ACTIVE
    value: prod
  - key: DB_HOST
    fromDatabase:          # 데이터베이스에서 자동 가져오기
      name: studiz-postgres
      property: host
  # ... 기타 환경 변수
```

**자동 연결되는 환경 변수:**
- `DB_HOST`: 데이터베이스 호스트
- `DB_PORT`: 데이터베이스 포트
- `DB_NAME`: 데이터베이스 이름
- `DB_USERNAME`: 데이터베이스 사용자
- `DB_PASSWORD`: 데이터베이스 비밀번호
- `JWT_SECRET`: 자동 생성 (32자 이상)

### 데이터베이스 설정

```yaml
databases:
  - name: studiz-postgres
    databaseName: studiz
    user: studiz
    plan: free
    postgresMajorVersion: 15
```

## 배포 확인

1. **Deployments** 탭에서 배포 상태 확인
2. 배포 완료 후 제공된 URL 확인 (예: `https://studiz-server.onrender.com`)
3. Swagger UI 접속: `https://studiz-server.onrender.com/api/swagger-ui.html`

## 중요 사항

### 1. SPRING_DATASOURCE_URL 문제 해결

Render는 `postgresql://` 형식의 URL을 제공하지만, PostgreSQL JDBC 드라이버는 `jdbc:postgresql://` 형식을 요구합니다.

**해결 방법:**
- `DataSourceConfig.java`가 자동으로 올바른 형식으로 변환합니다
- `SPRING_DATASOURCE_URL` 환경 변수는 설정하지 않습니다
- 개별 필드(`DB_HOST`, `DB_PORT` 등)만 사용합니다

### 2. Health Check

- Health Check Path를 설정하지 않으면 Render가 기본적으로 포트 8080을 확인합니다
- 필요시 `/api/api-docs`로 설정 가능

### 3. 무료 플랜 제한

- 15분간 요청이 없으면 서비스가 sleep 상태가 됩니다
- 첫 요청 시 깨우는데 시간이 걸릴 수 있습니다 (30초~1분)

## 트러블슈팅

### 배포 실패 시

1. **Logs** 탭에서 에러 확인
2. 환경 변수가 모두 설정되었는지 확인
3. `JWT_SECRET`이 자동 생성되었는지 확인

### 데이터베이스 연결 실패

- `DataSourceConfig.java`가 올바르게 작동하는지 확인
- `DB_HOST`, `DB_PORT` 등이 올바르게 설정되었는지 확인

### 403 에러

- `/api/swagger-ui.html`로 접속
- `/api/` 루트 경로도 접근 가능해야 함

## 업데이트 배포

GitHub에 푸시하면 자동으로 재배포됩니다:

```bash
git push origin main
```

Render가 자동으로 감지하고 배포를 시작합니다.

## Blueprint 장점

- ✅ 코드로 인프라 관리 (Infrastructure as Code)
- ✅ 자동화된 설정
- ✅ 재현 가능한 배포
- ✅ 환경 변수 자동 연결
- ✅ 한 번의 클릭으로 모든 서비스 생성

