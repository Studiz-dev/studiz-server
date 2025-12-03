#!/bin/bash

# EC2 배포 스크립트
# 빌드된 JAR 파일을 EC2로 복사하고 서비스를 재시작합니다.

set -e

# EC2 서버 정보
EC2_HOST="3.27.86.20"
EC2_USER="ec2-user"
SSH_KEY="~/.ssh/studiz-server-key.pem"
JAR_FILE="build/libs/studiz-server-0.0.1-SNAPSHOT.jar"
REMOTE_PATH="/opt/studiz-server/app.jar"
SERVICE_NAME="studiz-server"

echo "============================================"
echo "🚀 Studiz Server EC2 배포 시작"
echo "============================================"

# JAR 파일 존재 확인
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ 오류: JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    echo "먼저 './gradlew clean build -x test'를 실행하여 빌드하세요."
    exit 1
fi

echo ""
echo "1. JAR 파일을 EC2로 전송 중..."
scp -i $SSH_KEY $JAR_FILE $EC2_USER@$EC2_HOST:$REMOTE_PATH

echo ""
echo "2. 서비스 재시작 중..."
ssh -i $SSH_KEY $EC2_USER@$EC2_HOST "sudo systemctl restart $SERVICE_NAME"

echo ""
echo "3. 서비스 상태 확인 중..."
sleep 2
ssh -i $SSH_KEY $EC2_USER@$EC2_HOST "sudo systemctl status $SERVICE_NAME --no-pager"

echo ""
echo "============================================"
echo "✅ 배포 완료!"
echo "============================================"
echo ""
echo "서비스 로그 확인: ssh -i $SSH_KEY $EC2_USER@$EC2_HOST 'sudo journalctl -u $SERVICE_NAME -f'"

