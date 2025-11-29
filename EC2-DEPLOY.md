# 🖥️ AWS EC2 무료 배포 가이드

AWS EC2 Free Tier를 사용하여 Spring Boot 애플리케이션을 배포하는 방법입니다.

## AWS EC2 Free Tier란?

- ✅ **12개월 무료**: 신규 AWS 계정에 한해 12개월간 무료
- ✅ **t2.micro 인스턴스**: 750시간/월 무료
- ✅ **제한**: 1GB RAM, 1 vCPU (작은 프로젝트에 충분)
- ⚠️ **주의**: 12개월 후 또는 사용량 초과 시 과금

## 사전 준비

1. **AWS 계정 생성**
   - [AWS 콘솔](https://aws.amazon.com/ko/) 가입
   - 신용카드 등록 필요 (무료 티어 사용 시 과금 안 됨)

2. **로컬에서 JAR 파일 빌드**
   ```bash
   ./gradlew clean build -x test
   ```

## 배포 단계

### 1. EC2 인스턴스 생성

1. **EC2 콘솔 접속**
   - AWS 콘솔 → **EC2** 서비스 선택

2. **인스턴스 시작**
   - **Launch Instance** 클릭
   - **Name**: `studiz-server`

3. **AMI 선택**
   - **Amazon Linux 2023** 또는 **Ubuntu 22.04 LTS** 선택 (Free Tier)

4. **인스턴스 유형**
   - **t2.micro** 선택 (Free Tier)

5. **키 페어 생성**
   - **Create new key pair** 클릭
   - **Name**: `studiz-server-key`
   - **Key pair type**: RSA
   - **Private key file format**: `.pem`
   - **Create key pair** 클릭
   - ⚠️ **중요**: 다운로드한 `.pem` 파일을 안전하게 보관!

   **.pem 파일 저장 위치 (권장):**
   ```bash
   # 홈 디렉토리의 .ssh 폴더에 저장 (권장)
   mkdir -p ~/.ssh
   mv ~/Downloads/studiz-server-key.pem ~/.ssh/
   chmod 400 ~/.ssh/studiz-server-key.pem
   
   # 또는 프로젝트 외부의 별도 폴더
   mkdir -p ~/aws-keys
   mv ~/Downloads/studiz-server-key.pem ~/aws-keys/
   chmod 400 ~/aws-keys/studiz-server-key.pem
   ```
   
   ⚠️ **절대 프로젝트 디렉토리 안에 두지 마세요!** (Git에 실수로 커밋될 수 있음)

   **⚠️ 키 페어 파일을 잃어버린 경우:**
   - EC2 키 페어는 **한 번만 다운로드 가능**하며, 삭제되면 다시 다운로드할 수 없습니다
   - **해결 방법:**
     1. **인스턴스가 삭제된 경우**: 새 인스턴스를 생성하고 새 키 페어를 만들면 됩니다
     2. **인스턴스는 있는데 키 페어만 잃어버린 경우**: 아래 "키 페어 복구" 섹션 참고

6. **네트워크 설정**
   - **Security group**: 새로 생성
   - **Security group name**: `studiz-server-sg`
   - **Inbound rules** 추가:
     - **Type**: SSH, **Port**: 22, **Source**: My IP
     - **Type**: Custom TCP, **Port**: 8080, **Source**: 0.0.0.0/0 (모든 IP)

7. **스토리지**
   - **8 GB gp3** (Free Tier)

8. **Launch Instance** 클릭

### 2. EC2 인스턴스 접속

**Mac/Linux:**
```bash
# 키 파일 권한 설정 (처음 한 번만)
chmod 400 ~/.ssh/studiz-server-key.pem
# 또는
chmod 400 ~/aws-keys/studiz-server-key.pem

# SSH 접속 (Amazon Linux)
ssh -i ~/.ssh/studiz-server-key.pem ec2-user@<EC2-PUBLIC-IP>

# SSH 접속 (Ubuntu)
ssh -i ~/.ssh/studiz-server-key.pem ubuntu@<EC2-PUBLIC-IP>
```

**SSH Config 설정 (선택사항, 더 편하게 사용):**
```bash
# ~/.ssh/config 파일 생성 또는 수정
vi ~/.ssh/config
```

다음 내용 추가:
```
Host studiz-ec2
    HostName <EC2-PUBLIC-IP>
    User ec2-user
    IdentityFile ~/.ssh/studiz-server-key.pem
```

이제 간단하게 접속 가능:
```bash
ssh studiz-ec2
```

**Windows (PowerShell):**
```powershell
# WSL 또는 Git Bash 사용 권장
# 또는 PuTTY 사용
```

### 3. EC2에 Java 17 설치

**Amazon Linux 2023:**
```bash
sudo dnf update -y
sudo dnf install java-17-amazon-corretto -y
java -version
```

**Ubuntu 22.04:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
java -version
```

### 4. PostgreSQL 설치 및 설정

**Amazon Linux 2023:**
```bash
sudo dnf install postgresql15 postgresql15-server -y
sudo postgresql-setup --initdb
sudo systemctl enable postgresql
sudo systemctl start postgresql

# PostgreSQL 사용자 설정
sudo -u postgres psql
```

**PostgreSQL 설정:**
```sql
-- PostgreSQL 접속 후
CREATE DATABASE studiz;
CREATE USER studiz WITH PASSWORD 'your-secure-password';
GRANT ALL PRIVILEGES ON DATABASE studiz TO studiz;
\q
```

**PostgreSQL 원격 접속 허용 (선택사항):**
```bash
# pg_hba.conf 수정
sudo vi /var/lib/pgsql/data/pg_hba.conf
# 다음 줄 추가:
host    all             all             0.0.0.0/0               md5

# postgresql.conf 수정
sudo vi /var/lib/pgsql/data/postgresql.conf
# 다음 줄 수정:
listen_addresses = '*'

# PostgreSQL 재시작
sudo systemctl restart postgresql
```

### 5. 애플리케이션 배포

**방법 1: SCP로 JAR 파일 전송 (권장)**

로컬에서:
```bash
# .pem 파일이 ~/.ssh/에 있는 경우
scp -i ~/.ssh/studiz-server-key.pem build/libs/studiz-server-0.0.1-SNAPSHOT.jar ec2-user@<EC2-PUBLIC-IP>:/home/ec2-user/

# 또는 SSH Config를 설정한 경우
scp build/libs/studiz-server-0.0.1-SNAPSHOT.jar studiz-ec2:/home/ec2-user/
```

**방법 2: Git으로 클론 후 빌드**

EC2에서:
```bash
# Git 설치
sudo dnf install git -y  # Amazon Linux
# 또는
sudo apt install git -y  # Ubuntu

# 프로젝트 클론
git clone https://github.com/Studiz-dev/studiz-server.git
cd studiz-server

# Gradle 설치
wget https://services.gradle.org/distributions/gradle-8.5-bin.zip
sudo unzip gradle-8.5-bin.zip -d /opt
export PATH=$PATH:/opt/gradle-8.5/bin

# 빌드
./gradlew clean build -x test
```

### 6. 애플리케이션 디렉토리 설정

EC2에서:
```bash
# 애플리케이션 디렉토리 생성
sudo mkdir -p /opt/studiz-server
sudo chown ec2-user:ec2-user /opt/studiz-server
cd /opt/studiz-server

# JAR 파일 복사
cp ~/studiz-server-0.0.1-SNAPSHOT.jar app.jar
# 또는 Git으로 빌드한 경우
cp ~/studiz-server/build/libs/studiz-server-0.0.1-SNAPSHOT.jar app.jar
```

### 7. 환경 변수 설정

```bash
# 환경 변수 파일 생성
vi /opt/studiz-server/.env
```

`.env` 파일 내용:
```
SPRING_PROFILES_ACTIVE=prod
DB_HOST=localhost
DB_PORT=5432
DB_NAME=studiz
DB_USERNAME=studiz
DB_PASSWORD=your-secure-password
JWT_SECRET=your-very-long-jwt-secret-key-minimum-32-characters-long
```

### 8. Systemd 서비스 생성

```bash
sudo vi /etc/systemd/system/studiz-server.service
```

서비스 파일 내용:
```ini
[Unit]
Description=Studiz Server Application
After=network.target postgresql.service

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/studiz-server
EnvironmentFile=/opt/studiz-server/.env
ExecStart=/usr/bin/java -jar /opt/studiz-server/app.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

서비스 시작:
```bash
sudo systemctl daemon-reload
sudo systemctl enable studiz-server
sudo systemctl start studiz-server
sudo systemctl status studiz-server
```

### 9. 로그 확인

```bash
# 서비스 로그
sudo journalctl -u studiz-server -f

# 최근 로그만 보기
sudo journalctl -u studiz-server -n 50

# 애플리케이션 로그 (application-prod.yml에서 파일 로그 설정한 경우)
tail -f /opt/studiz-server/logs/studiz-server.log
```

### 10. 방화벽 설정 (필요시)

**Amazon Linux 2023:**
```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

**Ubuntu:**
```bash
sudo ufw allow 8080/tcp
sudo ufw reload
```

## 접속 확인

1. **EC2 Public IP 확인**
   - EC2 콘솔 → 인스턴스 선택 → **Public IPv4 address** 확인

2. **애플리케이션 접속**
   - `http://<EC2-PUBLIC-IP>:8080/api/`
   - `http://<EC2-PUBLIC-IP>:8080/api/swagger-ui.html`

## 도메인 연결 (선택사항)

### Route 53 사용 (유료)

1. Route 53에서 도메인 구매 또는 기존 도메인 연결
2. A 레코드 생성: `@` → EC2 Public IP
3. CNAME 레코드 생성: `www` → 도메인

### 무료 도메인 서비스

- [Freenom](https://www.freenom.com/) - 무료 도메인 (.tk, .ml 등)
- [No-IP](https://www.noip.com/) - 동적 DNS

## 보안 강화

### 1. HTTPS 설정 (Let's Encrypt)

```bash
# Certbot 설치
sudo dnf install certbot -y  # Amazon Linux
# 또는
sudo apt install certbot -y  # Ubuntu

# 인증서 발급
sudo certbot certonly --standalone -d your-domain.com
```

### 2. Nginx 리버스 프록시 설정 (선택사항)

```bash
# Nginx 설치
sudo dnf install nginx -y  # Amazon Linux
# 또는
sudo apt install nginx -y  # Ubuntu

# 설정 파일 생성
sudo vi /etc/nginx/conf.d/studiz-server.conf
```

설정 내용:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

```bash
# Nginx 시작
sudo systemctl enable nginx
sudo systemctl start nginx
```

## 비용 관리

### 무료 티어 제한

- **EC2**: t2.micro 750시간/월 (약 31일)
- **데이터 전송**: 1GB/월 무료
- **스토리지**: 30GB EBS 무료

### 비용 절감 팁

1. **인스턴스 중지**: 사용하지 않을 때 중지 (스토리지 비용만 발생)
2. **스냅샷 삭제**: 불필요한 스냅샷 삭제
3. **알람 설정**: CloudWatch에서 비용 알람 설정

## 트러블슈팅

### 키 페어 파일을 잃어버린 경우

**상황 1: 인스턴스가 삭제된 경우**
- 새 인스턴스를 생성하고 새 키 페어를 만들면 됩니다
- 기존 인스턴스의 데이터는 복구할 수 없습니다

**상황 2: 인스턴스는 있는데 키 페어만 잃어버린 경우**

**방법 1: EC2 Instance Connect 사용 (가장 쉬움)**
1. EC2 콘솔 → 인스턴스 선택 → **Connect** 클릭
2. **EC2 Instance Connect** 탭 선택
3. **Connect** 클릭 → 브라우저에서 터미널 열림
4. 새 키 페어를 생성하고 인스턴스에 추가:

```bash
# EC2 Instance Connect로 접속한 후
# 새 키 페어 생성 (로컬에서)
# AWS 콘솔 → EC2 → Key Pairs → Create key pair
# 새 키 페어 다운로드: studiz-server-key-new.pem

# 로컬에서 새 키 페어의 공개키 추출
ssh-keygen -y -f ~/.ssh/studiz-server-key-new.pem > ~/.ssh/new-key.pub

# EC2 Instance Connect 터미널에서
mkdir -p ~/.ssh
chmod 700 ~/.ssh
vi ~/.ssh/authorized_keys
# 위에서 추출한 공개키 내용을 붙여넣기
chmod 600 ~/.ssh/authorized_keys
```

이제 새 키 페어로 SSH 접속 가능:
```bash
ssh -i ~/.ssh/studiz-server-key-new.pem ec2-user@<EC2-IP>
```

**방법 2: Systems Manager Session Manager 사용**
1. EC2 인스턴스에 **SSM Agent** 설치 (Amazon Linux 2023는 기본 설치됨)
2. IAM 역할에 `AmazonSSMManagedInstanceCore` 정책 추가
3. EC2 콘솔 → 인스턴스 선택 → **Connect** → **Session Manager** 탭
4. 위와 동일하게 새 키 페어 추가

**방법 3: 새 키 페어로 인스턴스 교체 (데이터 백업 필요)**
1. EBS 스냅샷 생성 (데이터 백업)
2. 새 인스턴스 생성 (새 키 페어 사용)
3. 스냅샷에서 볼륨 복원

### 애플리케이션이 시작되지 않을 때

```bash
# 서비스 상태 확인
sudo systemctl status studiz-server

# 로그 확인
sudo journalctl -u studiz-server -n 50

# 환경 변수 확인
cat /opt/studiz-server/.env

# 포트 사용 확인
sudo netstat -tlnp | grep 8080
# 또는
sudo ss -tlnp | grep 8080
```

### 데이터베이스 연결 실패

```bash
# PostgreSQL 상태 확인
sudo systemctl status postgresql

# PostgreSQL 로그 확인
sudo tail -f /var/lib/pgsql/data/log/postgresql-*.log

# 연결 테스트
psql -h localhost -U studiz -d studiz
```

### 포트 접근 불가

1. **Security Group 확인**
   - EC2 콘솔 → Security Groups → 인바운드 규칙 확인
   - 포트 8080이 열려있는지 확인

2. **방화벽 확인**
   ```bash
   sudo firewall-cmd --list-all  # Amazon Linux
   sudo ufw status  # Ubuntu
   ```

### 메모리 부족

t2.micro는 1GB RAM만 있어서 메모리 부족할 수 있습니다:

```bash
# 메모리 사용량 확인
free -h

# JVM 힙 메모리 제한 (application-prod.yml에 추가)
# -Xmx512m -Xms256m
```

`application-prod.yml`에 추가:
```yaml
server:
  port: 8080
  servlet:
    context-path: /api
  # JVM 옵션은 systemd 서비스 파일에서 설정
```

systemd 서비스 파일 수정:
```ini
ExecStart=/usr/bin/java -Xmx512m -Xms256m -jar /opt/studiz-server/app.jar
```

## 업데이트 배포

```bash
# 새 버전 빌드 후 로컬에서
scp -i ~/.ssh/studiz-server-key.pem build/libs/studiz-server-0.0.1-SNAPSHOT.jar ec2-user@<EC2-PUBLIC-IP>:/opt/studiz-server/app.jar

# 또는 SSH Config를 설정한 경우
scp build/libs/studiz-server-0.0.1-SNAPSHOT.jar studiz-ec2:/opt/studiz-server/app.jar

# EC2에서
sudo systemctl restart studiz-server
sudo systemctl status studiz-server
```

## EC2 vs 다른 플랫폼

| 항목 | EC2 | Render | Railway |
|------|-----|--------|---------|
| 무료 기간 | 12개월 | 무제한 | 무제한 |
| 설정 난이도 | ⭐⭐⭐ 어려움 | ⭐⭐ 보통 | ⭐ 쉬움 |
| 제어권 | ✅✅✅ 완전 | ✅✅ 보통 | ✅✅ 보통 |
| 자동 배포 | ❌ 수동 | ✅ 자동 | ✅ 자동 |
| 추천 | 학습용 | 프로덕션 | 프로덕션 |

**EC2 추천 상황:**
- AWS 학습 목적
- 완전한 제어가 필요한 경우
- 복잡한 인프라 구성이 필요한 경우

**다른 플랫폼 추천:**
- 빠른 배포가 필요한 경우
- 자동 배포가 필요한 경우
- 간단한 설정을 원하는 경우

