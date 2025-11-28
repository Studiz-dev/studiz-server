#!/bin/bash

# 환경 변수 설정 스크립트
# 사용법: ./setup-env.sh

echo "🔐 환경 변수 설정 도우미"
echo "================================"
echo ""

# JWT Secret 생성
echo "1️⃣ JWT Secret 생성 중..."
JWT_SECRET=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
echo "생성된 JWT Secret: $JWT_SECRET"
echo ""

# DB Password 입력 받기
echo "2️⃣ 데이터베이스 비밀번호를 입력하세요:"
read -s DB_PASSWORD
echo ""

# 비밀번호 확인
echo "비밀번호를 다시 입력하세요 (확인):"
read -s DB_PASSWORD_CONFIRM
echo ""

if [ "$DB_PASSWORD" != "$DB_PASSWORD_CONFIRM" ]; then
    echo "❌ 비밀번호가 일치하지 않습니다."
    exit 1
fi

# .env.prod 파일 생성
cat > .env.prod << EOF
# 데이터베이스 설정
DB_PASSWORD=$DB_PASSWORD

# JWT 설정
JWT_SECRET=$JWT_SECRET

# 서버 포트 (선택사항)
SERVER_PORT=8080
EOF

echo "✅ .env.prod 파일이 생성되었습니다!"
echo ""
echo "⚠️  주의: .env.prod 파일은 절대 Git에 커밋하지 마세요!"
echo ""
echo "다음 단계: ./deploy.sh prod"

