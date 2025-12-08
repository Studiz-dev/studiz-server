#!/bin/bash

# EC2 서버 상태 확인 스크립트

EC2_HOST="3.27.86.20"
EC2_USER="ec2-user"
SSH_KEY="~/.ssh/studiz-server-key.pem"

echo "============================================"
echo "🔍 EC2 서버 상태 확인"
echo "============================================"

# 1. 서버 연결 테스트
echo ""
echo "1. 서버 연결 테스트..."
if ping -c 1 -W 2 $EC2_HOST &> /dev/null; then
    echo "✅ 서버 연결 성공"
else
    echo "❌ 서버 연결 실패"
    exit 1
fi

# 2. 포트 8080 확인
echo ""
echo "2. 백엔드 서버 포트 확인 (8080)..."
if nc -zv -w 2 $EC2_HOST 8080 2>&1 | grep -q "succeeded"; then
    echo "✅ 포트 8080 열려있음"
    echo ""
    echo "API 테스트:"
    curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://$EC2_HOST:8080/api/ || echo "❌ API 응답 없음"
else
    echo "❌ 포트 8080 닫혀있거나 서버가 실행 중이 아님"
fi

# 3. SSH 접속 시도 (가능한 경우)
echo ""
echo "3. SSH 접속 테스트..."
if ssh -i $SSH_KEY -o ConnectTimeout=5 -o BatchMode=yes $EC2_USER@$EC2_HOST "echo 'SSH 연결 성공'" 2>/dev/null; then
    echo "✅ SSH 접속 가능"
    echo ""
    echo "서비스 상태 확인:"
    ssh -i $SSH_KEY $EC2_USER@$EC2_HOST "sudo systemctl status studiz-server --no-pager | head -n 10" 2>/dev/null || echo "서비스 상태 확인 실패"
else
    echo "⚠️  SSH 접속 불가 (보안 그룹 또는 네트워크 문제 가능)"
fi

echo ""
echo "============================================"





