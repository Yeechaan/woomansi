![Frame 2608772](https://github.com/Yeechaan/woomansi/assets/41672138/3679f4bb-4081-4667-90c4-5dac525c8a78)

## Introduction

우만시(우리가 만난 시절) 프로젝트는,

- 데일 카네기의 '인간관계론'에서 아이디어를 얻어 주변 사람들에 대한 관심사와 함께 있었던 순간들을 기록하는 서비스
- 디자이너(2명), 서버 개발자(1명), 앱 개발자(1명)로 구성된 팀 프로젝트
- Android 앱
  - 모든 UI는 Jetpack Compose로 구성
  - Kotlin Coroutines으로 비동기 처리
- data layer의 비지니스 로직은 Kotlin Multiplatform으로 개발



## Tech stack & Open-source libraries

Kotlin, Coroutines, Flow, Jetpack(Compose, ViewModel, Navigation), Realm(Kotlin SDK), Ktor, Koin, Napier, Kotlin Multiplatform Mobile



## Architecture

follow the [Guide to app architecture](https://developer.android.com/topic/architecture) with Ui Layer and Data Layer.

- Ui Layer : Compose, ViewModel
  (androidApp/src/main)
- Data layer : Repository, Api, Dao
  (shared/src/commonMain)



## Database Encryption

Realm Database에 암호화를 적용하여 앱 외부에서 저장된 데이터에 접근을 못 하도록 한다.

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/Yeechaan/woomansi/main/diagram/RealmKeyEncryptFlow.puml)

- create and manage realm key with AndroidKeyStore. [EncryptionRealm.kt](https://github.com/Yeechaan/woomansi/blob/main/shared/src/androidMain/kotlin/com/lee/remember/encryption/EncryptionRealm.kt)
- initialize and open Realm Instance. [BaseRealm.kt](https://github.com/Yeechaan/woomansi/blob/main/shared/src/commonMain/kotlin/com/lee/remember/local/BaseRealm.kt) 
- manage(save, read) initial vector and encrypted realm key. [RealmDataStore.kt](https://github.com/Yeechaan/woomansi/blob/main/shared/src/commonMain/kotlin/com/lee/remember/local/datastore/RealmDataStore.kt)