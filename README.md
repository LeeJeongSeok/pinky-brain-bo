# Pink & Brain Admin Backoffice

**Pink & Brain Admin Backoffice**는 오프라인 보세 옷가게 **Pink & Brain**에서 사용하는 관리자 전용 웹 애플리케이션입니다. 관리자는 이 페이지를 통해 상품의 등록, 수정, 삭제와 같은 작업을 간편하게 수행할 수 있습니다.

## 주요 기능

### 1. 상품 업로드
- 새로운 상품을 등록할 수 있는 기능을 제공합니다.
- 상품의 이름, 가격, 설명, 카테고리, 이미지 등을 입력하여 등록할 수 있습니다.

### 2. 상품 수정
- 이미 등록된 상품의 정보를 수정할 수 있는 기능을 제공합니다.
- 상품의 이름, 가격, 설명, 이미지 등을 변경할 수 있습니다.

### 3. 상품 삭제
- 등록된 상품을 삭제할 수 있는 기능을 제공합니다.
- 삭제된 상품은 더 이상 백오피스와 사용자 페이지에서 조회되지 않습니다.

## 기술 스택

- **Frontend**: HTML, CSS, JavaScript, Thymeleaf (필요시 React.js 또는 Vue.js로 확장 가능)
- **Backend**: Spring Boot
- **Database**: MySQL
- **Version Control**: Git, GitHub
- **Deployment**: AWS EC2, Docker (선택 사항)

## 설치 및 실행 방법

### 1. 사전 요구사항
- Java 11 이상
- MySQL 데이터베이스
- Git
- IDE (IntelliJ IDEA 추천)

### 2. 설치
1. 프로젝트를 클론합니다.
   ```bash
   git clone https://github.com/LeeJeongSeok/pinky-brain-bo.git
   ```

2. 프로젝트 디렉토리로 이동합니다.
   ```bash
   cd pinky-brain-bo
   ```

3. `application.yml` 파일을 설정합니다.
   ```yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/pink_brain
       username: your_username
       password: your_password
   ```

4. MySQL 데이터베이스를 생성합니다.
   ```sql
   CREATE DATABASE pink_brain;
   ```

5. 종속성을 설치하고 애플리케이션을 실행합니다.
   ```bash
   ./gradlew bootRun
   ```

### 3. 실행
- 로컬 환경에서 애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

## 사용 방법

1. **상품 관리**
   - **상품 업로드**: 상품 정보를 입력하고 등록 버튼을 누릅니다.
   - **상품 수정**: 상품 목록에서 수정할 상품을 선택하고 정보를 업데이트합니다.
   - **상품 삭제**: 상품 목록에서 삭제할 상품을 선택하고 삭제 버튼을 누릅니다.

## 향후 계획
- 상품 검색 및 필터링 기능 추가
- 데이터 분석을 위한 통계 대시보드 추가
- 사용자 권한 관리 시스템 도입

## 기여 방법
1. 이 프로젝트를 포크합니다.
2. 새로운 브랜치를 만듭니다.
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. 변경 사항을 커밋합니다.
   ```bash
   git commit -m "Add your commit message"
   ```
4. 브랜치를 푸시합니다.
   ```bash
   git push origin feature/your-feature-name
   ```
5. Pull Request를 생성합니다.

