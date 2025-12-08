# EC2 배포 가이드

## 현재 문제: Mixed Content 에러

**증상:**
- 프론트엔드 (Vercel): `https://studiz-fe.vercel.app/` (HTTPS)
- 백엔드 (EC2): `http://3.27.86.20:8080` (HTTP)
- 브라우저가 HTTPS 페이지에서 HTTP API 호출을 차단

**해결 방법:**

### 방법 1: 프론트엔드에서 HTTP 허용 (임시 해결)

Vercel 환경 변수 설정:
```
NEXT_PUBLIC_API_URL=http://3.27.86.20:8080/api
```

프론트엔드 코드에서:
```javascript
// 개발 환경에서는 HTTP 허용
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://3.27.86.20:8080/api';
```

**주의:** 이 방법은 브라우저가 여전히 차단할 수 있습니다.

### 방법 2: 백엔드를 HTTPS로 변경 (권장)

1. Nginx 리버스 프록시 설정
2. Let's Encrypt SSL 인증서 발급
3. 포트 443으로 HTTPS 제공

자세한 내용은 `deploy/` 디렉토리의 스크립트 참고.

### 방법 3: 도메인 사용 + HTTPS

1. 도메인 구매 (예: studiz-api.com)
2. EC2에 Elastic IP 할당
3. DNS A 레코드 설정
4. SSL 인증서 발급
5. 프론트엔드 API URL을 `https://api.studiz.com`으로 변경

## 서버 상태 확인

```bash
# 서버 연결 확인
./deploy/check-server.sh

# 또는 직접 확인
curl http://3.27.86.20:8080/api/
```

## 배포

```bash
# 빌드
./gradlew clean build -x test

# 배포
./deploy/ec2-deploy.sh
```
