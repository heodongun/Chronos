# Chronos 배포 옵션

## 현재 상태

✅ **JitPack 배포 완료** - 즉시 사용 가능!
⏳ **Maven Central 배포** - 자격증명 필요

## 옵션 1: JitPack 사용 (✅ 이미 완료, 권장)

### 장점
- ✅ 이미 배포 완료
- ✅ 자격증명 불필요
- ✅ GitHub 태그 기반 자동 빌드
- ✅ 즉시 사용 가능
- ✅ 무료

### 사용 방법

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.heodongun:Chronos:v1.0.0")
}
```

### 확인
- JitPack 페이지: https://jitpack.io/#heodongun/Chronos
- GitHub: https://github.com/heodongun/Chronos
- 릴리즈: https://github.com/heodongun/Chronos/releases/tag/v1.0.0

---

## 옵션 2: Maven Central 배포 (추가 설정 필요)

### 필요한 작업

#### 1. Sonatype 계정 생성
1. https://central.sonatype.com/ 방문
2. 계정 생성 및 이메일 인증
3. `io.github.heodongun` namespace 인증

#### 2. GPG 설정 완료

현재 GPG 키 정보:
- Key ID: `DC51EA56B80B01EB`
- Email: heodongun08@gmail.com

**GPG 키 서버에 업로드:**
```bash
gpg --keyserver keyserver.ubuntu.com --send-keys DC51EA56B80B01EB
```

**Secret key 파일 생성:**
```bash
gpg --export-secret-keys DC51EA56B80B01EB > ~/.gnupg/secring.gpg
```

#### 3. gradle.properties 업데이트

`/Users/heodongun/Desktop/JSONRefair/gradle.properties` 파일 수정:

```properties
# Maven Central Publishing Credentials
mavenCentralUsername=YOUR_SONATYPE_USERNAME
mavenCentralPassword=YOUR_SONATYPE_TOKEN

# GPG Signing Configuration
signing.keyId=DC51EA56B80B01EB
signing.password=YOUR_GPG_PASSWORD
signing.secretKeyRingFile=/Users/heodongun/.gnupg/secring.gpg
signing.required=true
```

#### 4. 배포 명령 실행

```bash
./gradlew :chronos:publishAndReleaseToMavenCentral --no-configuration-cache
```

### 장점
- 공식 Maven Central 저장소
- 더 넓은 인지도
- 기본 Maven/Gradle 저장소

### 단점
- 복잡한 설정 과정
- 계정 및 인증 필요
- GPG 키 관리 필요
- 첫 배포 시 2-4시간 소요

---

## 🎯 권장 사항

### 대부분의 사용자에게는 JitPack으로 충분합니다!

**JitPack 사용을 권장하는 이유:**
1. ✅ 이미 배포 완료되어 즉시 사용 가능
2. ✅ GitHub 태그만으로 자동 관리
3. ✅ 추가 설정이나 계정 불필요
4. ✅ Kotlin 커뮤니티에서 널리 사용
5. ✅ 새 버전 배포가 매우 간단 (태그만 추가)

**Maven Central은 다음 경우에만 고려:**
- 대기업이나 공공 프로젝트에서 사용
- Maven Central 정책이 필수인 경우
- 더 넓은 배포를 원하는 경우

---

## 📦 현재 사용 가능한 배포

### JitPack (즉시 사용 가능)

```kotlin
// build.gradle.kts
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.heodongun:Chronos:v1.0.0")
}
```

**테스트 프로젝트:**
```bash
# 새 프로젝트 생성
mkdir chronos-test && cd chronos-test

# build.gradle.kts 생성
cat > build.gradle.kts << 'EOF'
plugins {
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.heodongun:Chronos:v1.0.0")
}
EOF

# 테스트 코드 생성
mkdir -p src/main/kotlin
cat > src/main/kotlin/Main.kt << 'EOF'
import io.github.heodongun.chronos.*

fun main() {
    val now = DateTime.now()
    val future = now + 5.days
    println("Now: $now")
    println("Future: $future")
    println("Difference: ${now.diffForHumans(future)}")
}
EOF

# 실행
gradle run
```

---

## 🔄 새 버전 릴리즈 방법

### JitPack (간단)

```bash
# 코드 수정 후
git add .
git commit -m "feat: 새 기능 추가"
git push chronos master

# 새 버전 태그
git tag -a v1.1.0 -m "Release v1.1.0"
git push chronos v1.1.0
```

그러면 사용자는 즉시 `v1.1.0`을 사용할 수 있습니다!

### Maven Central (복잡)

1. 버전 업데이트: `chronos/build.gradle.kts`
2. 변경사항 커밋 및 태그
3. `./gradlew :chronos:publishAndReleaseToMavenCentral`
4. Central Portal에서 검증 대기
5. 2-4시간 후 Maven Central 동기화

---

## 📊 배포 상태 요약

| 항목 | JitPack | Maven Central |
|------|---------|---------------|
| 상태 | ✅ 완료 | ⏳ 설정 필요 |
| 사용 가능 | ✅ 즉시 | ❌ 설정 후 |
| 설정 난이도 | 쉬움 | 어려움 |
| 유지보수 | 자동 | 수동 |
| 비용 | 무료 | 무료 |
| 인기도 | 높음 (Kotlin) | 매우 높음 |

---

## 💡 결론

**Chronos는 이미 JitPack을 통해 배포되어 전 세계 개발자들이 사용할 수 있습니다!**

Maven Central 배포를 원하시면 `MAVEN_CENTRAL_SETUP.md` 파일의 단계를 따라하세요.

대부분의 경우 JitPack만으로도 충분하며, 실제로 많은 인기 Kotlin 라이브러리들이 JitPack을 사용합니다.

---

**다음 단계:**
1. JitPack 사용 (권장) - 추가 작업 없음
2. 또는 Maven Central 설정 완료 후 배포

프로젝트를 홍보하고 사용자 피드백을 받는 것이 더 중요합니다! 🚀
