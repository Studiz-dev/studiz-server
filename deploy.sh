#!/bin/bash

# 배포 스크립트
# 사용법: ./deploy.sh [dev|prod]

set -e

ENV=${1:-prod}
echo "🚀 배포 환경: $ENV"

# 환경 변수 파일 확인
if [ ! -f ".env.$ENV" ]; then
    echo "❌ .env.$ENV 파일이 없습니다."
    echo "필요한 환경 변수:"
    echo "  - DB_PASSWORD"
    echo "  - JWT_SECRET"
    exit 1
fi

# 환경 변수 로드
export $(cat .env.$ENV | grep -v '^#' | xargs)

# JAR 빌드
echo "📦 JAR 파일 빌드 중..."
./gradlew clean build -x test --no-daemon

# Docker 이미지 빌드
echo "🐳 Docker 이미지 빌드 중..."
docker build -t studiz-server:latest .

# Docker Compose로 배포
if [ "$ENV" = "prod" ]; then
    echo "🚀 프로덕션 환경 배포 중..."
    docker-compose -f docker-compose.prod.yml down
    docker-compose -f docker-compose.prod.yml up -d
else
    echo "🚀 개발 환경 배포 중..."
    docker-compose down
    docker-compose up -d
fi

echo "✅ 배포 완료!"
echo "📊 로그 확인: docker-compose -f docker-compose.prod.yml logs -f app"
echo "🛑 중지: docker-compose -f docker-compose.prod.yml down"

