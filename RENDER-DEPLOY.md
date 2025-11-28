# 🚀 Render 배포 가이드

## Render란?

Render는 간단한 클라우드 플랫폼으로, GitHub에 푸시하면 자동으로 배포됩니다.

## 배포 방법

### 1. GitHub에 코드 푸시

```bash
git add .
git commit -m "배포 준비"
git push origin main
```

### 2. Render에서 서비스 생성

1. [Render 대시보드](https://dashboard.render.com) 접속
2. **New +** → **Blueprint** 클릭
3. GitHub 저장소 연결
4. `render.yaml` 파일이 자동으로 감지됨
5. **Apply** 클릭

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

- **services**: 웹 서비스 설정
  - `runtime: docker`: Docker 기반 배포
  - `dockerfilePath`: Dockerfile 경로
- **databases**: PostgreSQL 데이터베이스 자동 생성
- **envVars**: 환경 변수 자동 연결

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

