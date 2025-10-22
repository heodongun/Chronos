# Maven Central 배포 가이드

## 📋 사전 준비사항

Maven Central에 배포하기 위해서는 다음이 필요합니다:

### 1. Sonatype Central Portal 계정

1. https://central.sonatype.com/ 방문
2. "Sign Up" 클릭하여 계정 생성
3. 이메일 인증 완료

### 2. Namespace 인증 (io.github.heodongun)

계정 생성 후:

1. Central Portal에 로그인
2. "Namespaces" 메뉴로 이동
3. "Add Namespace" 클릭
4. `io.github.heodongun` 입력
5. GitHub 저장소를 통한 인증:
   - GitHub에 `OSSRH-XXXXX` 이름의 public 저장소 생성 (티켓 번호는 이메일로 받음)
   - 또는 GitHub username을 통한 자동 인증

### 3. GPG 키 설정

GPG 키는 이미 있습니다:
- Key ID: `DC51EA56B80B01EB`
- Email: heodongun08@gmail.com

#### GPG 키 서버에 업로드

```bash
# 공개 키를 키 서버에 업로드
gpg --keyserver keyserver.ubuntu.com --send-keys DC51EA56B80B01EB

# 또는
gpg --keyserver keys.openpgp.org --send-keys DC51EA56B80B01EB
```

#### GPG 키 내보내기 (signing용)

```bash
# Secret key 내보내기
gpg --export-secret-keys DC51EA56B80B01EB > ~/.gnupg/secring.gpg

# 또는 armor 형식으로
gpg --armor --export-secret-keys DC51EA56B80B01EB > ~/.gnupg/secring.asc
```

## 🔧 gradle.properties 설정

`/Users/heodongun/Desktop/JSONRefair/gradle.properties` 파일을 다음과 같이 수정:

```properties
# Gradle Build Configuration
org.gradle.caching=true
org.gradle.configuration-cache=false

# Maven Central Publishing Credentials
# Central Portal에서 생성한 토큰 사용 (Username 탭에서 Generate User Token)
mavenCentralUsername=YOUR_SONATYPE_USERNAME
mavenCentralPassword=YOUR_SONATYPE_PASSWORD

# GPG Signing Configuration
signing.keyId=DC51EA56B80B01EB
signing.password=YOUR_GPG_PASSWORD
signing.secretKeyRingFile=/Users/heodongun/.gnupg/secring.gpg

# Make signing required for releases
signing.required=true
```

### 보안 팁

중요한 자격증명은 환경변수로 관리하는 것이 좋습니다:

```bash
# ~/.zshrc 또는 ~/.bashrc에 추가
export ORG_GRADLE_PROJECT_mavenCentralUsername="your_username"
export ORG_GRADLE_PROJECT_mavenCentralPassword="your_password"
export ORG_GRADLE_PROJECT_signingPassword="your_gpg_password"
```

그러면 gradle.properties에서 자격증명을 빼도 됩니다.

## 🚀 배포 명령어

### 1. 테스트 빌드

```bash
./gradlew :chronos:build --no-configuration-cache
```

### 2. 로컬 Maven에 발행 (테스트용)

```bash
./gradlew :chronos:publishToMavenLocal --no-configuration-cache
```

발행된 위치 확인:
```bash
ls -la ~/.m2/repository/io/github/heodongun/chronos/
```

### 3. Maven Central에 발행 및 릴리즈

```bash
./gradlew :chronos:publishAndReleaseToMavenCentral --no-configuration-cache
```

또는 단계별로:

```bash
# 1. Staging 저장소에 발행
./gradlew :chronos:publishAllPublicationsToMavenCentral --no-configuration-cache

# 2. Central Portal에서 수동으로 릴리즈
# https://central.sonatype.com/ 로그인 → Deployments 확인
```

### 4. 전체 프로세스 (권장)

```bash
# 빌드 → 테스트 → 발행 → 릴리즈 (한 번에)
./gradlew :chronos:clean :chronos:build :chronos:publishAndReleaseToMavenCentral --no-configuration-cache
```

## ✅ 배포 확인

### 1. Sonatype Central Portal에서 확인

1. https://central.sonatype.com/ 로그인
2. "Deployments" 메뉴로 이동
3. 배포 상태 확인:
   - PENDING: 검증 중
   - VALIDATED: 검증 완료, 릴리즈 대기
   - PUBLISHING: Maven Central에 배포 중
   - PUBLISHED: 배포 완료

### 2. Maven Central에서 검색

배포 후 2-4시간 후:
- https://central.sonatype.com/artifact/io.github.heodongun/chronos
- https://repo1.maven.org/maven2/io/github/heodongun/chronos/

### 3. 실제 사용 테스트

새 프로젝트에서:

```kotlin
dependencies {
    implementation("io.github.heodongun:chronos:1.0.0")
}
```

## 🔍 문제 해결

### "401 Unauthorized" 에러

- mavenCentralUsername과 mavenCentralPassword 확인
- Central Portal에서 "Generate User Token" 사용 권장

### "Could not find signing key" 에러

```bash
# GPG 키 확인
gpg --list-secret-keys

# Secret key 다시 내보내기
gpg --export-secret-keys DC51EA56B80B01EB > ~/.gnupg/secring.gpg
```

### "POM validation failed" 에러

build.gradle.kts의 pom {} 섹션 확인:
- name, description, url 필수
- licenses 필수
- developers 필수
- scm 필수

### "Namespace not verified" 에러

Central Portal에서 io.github.heodongun namespace 인증 완료해야 함

## 📊 현재 상태

### ✅ 완료
- [x] 프로젝트 빌드 설정
- [x] GPG 키 존재 확인
- [x] POM 설정 완료
- [x] 빌드 테스트 성공

### ⏳ 필요한 작업
- [ ] Sonatype Central Portal 계정 생성
- [ ] io.github.heodongun namespace 인증
- [ ] GPG 키를 키 서버에 업로드
- [ ] gradle.properties에 자격증명 추가
- [ ] publishAndReleaseToMavenCentral 실행

## 🎯 빠른 시작 체크리스트

1. [ ] https://central.sonatype.com/ 에서 계정 생성
2. [ ] `io.github.heodongun` namespace 인증
3. [ ] GPG 키 업로드: `gpg --keyserver keyserver.ubuntu.com --send-keys DC51EA56B80B01EB`
4. [ ] gradle.properties 설정
5. [ ] 명령 실행: `./gradlew :chronos:publishAndReleaseToMavenCentral --no-configuration-cache`
6. [ ] Central Portal에서 배포 상태 확인
7. [ ] 2-4시간 후 Maven Central에서 확인

## 📚 참고 자료

- **Central Portal 가이드**: https://central.sonatype.org/publish/publish-guide/
- **Gradle 플러그인**: https://github.com/vanniktech/gradle-maven-publish-plugin
- **GPG 가이드**: https://central.sonatype.org/publish/requirements/gpg/

## 🆘 도움이 필요하면

Sonatype 지원:
- Slack: https://sonatype-community.slack.com/
- JIRA: https://issues.sonatype.org/

---

**참고**: JitPack 배포는 이미 완료되었으므로, Maven Central은 선택사항입니다.
JitPack 사용: `implementation("com.github.heodongun:Chronos:v1.0.0")`
