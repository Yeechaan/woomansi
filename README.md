![Frame 2608772](https://github.com/Yeechaan/woomansi/assets/41672138/3679f4bb-4081-4667-90c4-5dac525c8a78)

## Introduction

우만시(우리가 만난 시절) 서비스는,

- 주변 사람들과의 추억을 기록하며 좋은 관계를 유지하기 위해 진행
- 디자이너(2명), 서버 개발자(1명), 앱 개발자(1명)로 구성된 팀 프로젝트
- Android 앱
  - 모든 UI는 Jetpack Compose로 구성
  - Kotlin Coroutines으로 비동기 처리
- 추후 iOS 앱 개발을 고려하여 data layer의 비지니스 로직은 Kotlin Multiplatfrom으로 개발



## Tech stack & Open-source libraries

- Minimum SDK level 24
- 100% [Jetpack Compose](https://developer.android.com/jetpack/compose) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Compose: Android’s modern toolkit for building native UI.
  - ViewModel: UI related data holder and lifecycle aware.
  - Navigation: for navigating screens with [Compose Navigation](https://developer.android.com/jetpack/compose/navigation).
- [realm-kotlin](https://github.com/realm/realm-kotlin) : a mobile database that runs on Kotlin Multiplatform and Android.
- [Ktor](https://github.com/ktorio/ktor) : building asynchronous servers and clients in Kotlin by JetBrains.
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization) : 
- [Koin](https://github.com/InsertKoinIO/koin) : for pragmatic lightweight dependency injection framework.
- [Napier](https://github.com/AAkira/Napier) : a logger library for Kotlin Multiplatform. It supports Android, Darwin, JVM, JavaScript.



## Architecture

follow the [Guide to app architecture](https://developer.android.com/topic/architecture) with Ui Layer and Data Layer.

- Ui Layer : Compose, ViewModel
  (androidApp/src/main)
- Data layer : Repository, Api, Dao
  (shared/src/commonMain)

