#!/bin/bash

# EC2 초기 설정 스크립트
# Amazon Linux 2023 또는 Ubuntu 22.04에서 실행

set -e

echo "🚀 Studiz Server EC2 초기 설정 시작..."

# Java 17 설치 확인 및 설치
if ! command -v java &> /dev/null; then
    echo "📦 Java 17 설치 중..."
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        if [[ "$ID" == "amzn" ]]; then
            sudo dnf update -y
            sudo dnf install java-17-amazon-corretto -y
        elif [[ "$ID" == "ubuntu" ]]; then
            sudo apt update
            sudo apt install openjdk-17-jdk -y
        fi
    fi
    echo "✅ Java 17 설치 완료"
else
    echo "✅ Java 이미 설치됨: $(java -version 2>&1 | head -n 1)"
fi

# PostgreSQL 설치 확인 및 설치
if ! command -v psql &> /dev/null; then
    echo "📦 PostgreSQL 설치 중..."
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        if [[ "$ID" == "amzn" ]]; then
            sudo dnf install postgresql15 postgresql15-server -y
            sudo postgresql-setup --initdb
            sudo systemctl enable postgresql
            sudo systemctl start postgresql
        elif [[ "$ID" == "ubuntu" ]]; then
            sudo apt install postgresql postgresql-contrib -y
            sudo systemctl enable postgresql
            sudo systemctl start postgresql
        fi
    fi
    echo "✅ PostgreSQL 설치 완료"
else
    echo "✅ PostgreSQL 이미 설치됨"
fi

# 애플리케이션 디렉토리 생성
echo "📁 애플리케이션 디렉토리 생성 중..."
sudo mkdir -p /opt/studiz-server
sudo chown $USER:$USER /opt/studiz-server
echo "✅ 디렉토리 생성 완료"

# Systemd 서비스 파일 생성
echo "⚙️ Systemd 서비스 파일 생성 중..."
sudo tee /etc/systemd/system/studiz-server.service > /dev/null <<EOF
[Unit]
Description=Studiz Server Application
After=network.target postgresql.service

[Service]
Type=simple
User=$USER
WorkingDirectory=/opt/studiz-server
EnvironmentFile=/opt/studiz-server/.env
ExecStart=/usr/bin/java -Xmx512m -Xms256m -jar /opt/studiz-server/app.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
echo "✅ Systemd 서비스 파일 생성 완료"

echo ""
echo "✅ 초기 설정 완료!"
echo ""
echo "다음 단계:"
echo "1. JAR 파일을 /opt/studiz-server/app.jar로 복사"
echo "2. /opt/studiz-server/.env 파일 생성 (환경 변수 설정)"
echo "3. sudo systemctl start studiz-server"
echo "4. sudo systemctl status studiz-server"

