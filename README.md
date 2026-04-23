# Notes App

Простое Android-приложение для хранения заметок с локальным сохранением данных.

## Реализованный функционал

- создание заметки: заголовок + текст
- редактирование заметки
- удаление заметки
- просмотр списка заметок
- локальное хранение через `SharedPreferences`

## Технологии

- Kotlin
- Android Views + ViewBinding
- RecyclerView
- JUnit 4
- Robolectric
- Espresso

## Структура проекта

- `app/src/main` - основной код приложения
- `app/src/test` - модульные тесты
- `app/src/androidTest` - UI-тесты Espresso
- `report` - шаблон отчёта и список скриншотов

## Как запустить

1. Откройте папку `notes-app` в Android Studio.
2. Установите Android SDK Platform и Build Tools для API 36.
3. Выполните Gradle Sync.
4. Запустите приложение на эмуляторе или устройстве.

## Какие тесты уже добавлены

- модульные тесты на создание, редактирование, удаление и загрузку заметок
- тест сериализации/десериализации
- UI-тесты Espresso на создание, редактирование и удаление

После синхронизации проекта в Android Studio тесты можно запускать стандартными задачами:

- `test`
- `connectedAndroidTest`

## Android Profiler

Для отчёта снимите показатели Android Profiler во время:

- запуска приложения
- создания заметки
- редактирования заметки
- удаления заметки
- повторного открытия приложения с загрузкой сохранённых заметок

- ## Scrinshots
- <img width="863" height="1822" alt="2db2d2b8-302f-4f46-9d7e-887af9acaa21" src="https://github.com/user-attachments/assets/5f4739c2-a8cd-4ff3-b801-7ec9826d515d" />
<img width="1672" height="941" alt="81ee27cc-7a24-48bc-9042-152b0c2e0cda" src="https://github.com/user-attachments/assets/ccbd8ed8-db7e-4c3a-8a31-4495200bbc97" />
<img width="863" height="1822" alt="a06c1365-e35b-4af6-9ae1-de4a964d519d" src="https://github.com/user-attachments/assets/ed468eb0-9f04-4a68-ae4f-574169b3e0e0" />
<img width="863" height="1822" alt="2dc54bc5-0a3c-441a-b004-8eab085d6a23" src="https://github.com/user-attachm<img width="1672" height="941" alt="29cfee9d-f9c7-4e8e-b560-6dd477b2ba49" src="https://github.com/user-attachments/assets/45030cac-cabd-495c-b7ce-0a32381f73d6" />
ents/assets/81290d4e-7ca8-46fc-9dca-1b7c562b7ecf" />


