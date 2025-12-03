#!/bin/bash
# 빠른 테스트 데이터 삽입 스크립트

echo "============================================"
echo "Studiz 서버 테스트 데이터 삽입"
echo "============================================"

# EC2 서버 정보
EC2_HOST="3.27.86.20"
EC2_USER="ec2-user"
SSH_KEY="~/.ssh/studiz-server-key.pem"
SQL_FILE="insert_test_data.sql"

echo ""
echo "1. SQL 파일을 EC2로 전송 중..."
scp -i $SSH_KEY $SQL_FILE $EC2_USER@$EC2_HOST:/tmp/

echo ""
echo "2. 데이터베이스에 데이터 삽입 중..."
ssh -i $SSH_KEY $EC2_USER@$EC2_HOST "sudo -u postgres psql -d studiz -f /tmp/$SQL_FILE"

echo ""
echo "3. 데이터 확인 중..."
ssh -i $SSH_KEY $EC2_USER@$EC2_HOST "sudo -u postgres psql -d studiz -c \"SELECT 'Users' as table_name, COUNT(*) as count FROM users UNION ALL SELECT 'Studies', COUNT(*) FROM studies UNION ALL SELECT 'Schedules', COUNT(*) FROM schedules UNION ALL SELECT 'Todos', COUNT(*) FROM todos;\""

echo ""
echo "============================================"
echo "완료!"
echo "============================================"
