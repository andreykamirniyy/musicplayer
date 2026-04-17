# T-Музыка MVP

MVP музыкального приложения для Android с режимом **Т-Волна** (аналог "Моя Волна"), где рекомендации строятся по двум условиям:
1. Пользователь добавил треки в Избранное.
2. Рекомендации максимально похожи на Избранное и учитывают настроение.

## Что уже есть

- `backend/` — FastAPI API + базовый рекомендательный алгоритм.
- `android/` — Android-приложение (Kotlin + Jetpack Compose) с экранами MVP.
- `scripts/demo_backend.sh` — скрипт быстрой проверки API.
- `docker-compose.yml` — запуск backend в контейнере.

## Откуда берутся треки

- В текущем MVP треки и метаданные хранятся в `backend/app/data.py` (демо-каталог).
- В Android для демонстрации воспроизведения используются публичные demo-stream URL (`SoundHelix`).
- Для production нужно подключать легальный каталог/лицензированный провайдер (правообладатели, CDN, DRM).

## Как увидеть, что проект рабочий

### Вариант A (рекомендуется): через Docker

```bash
docker compose up --build
```

В отдельном терминале:

```bash
./scripts/demo_backend.sh
```

Если всё ок, увидишь:
- `{"status":"ok"}` на `/health`
- список треков на `/tracks`
- `{"result":"added"}` после добавления в избранное
- JSON с `recommendations` на `/wave/demo?mood=focus`

Проверка поиска (трек/альбом):

```bash
curl 'http://127.0.0.1:8000/search?query=Morning'
```

Остановить:

```bash
docker compose down
```

### Вариант B: локально Python

```bash
cd backend
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

В другом терминале из корня проекта:

```bash
./scripts/demo_backend.sh
```

## Android запуск

1. Открыть папку `android/` в Android Studio.
2. Дождаться Gradle Sync.
3. Запустить на эмуляторе/устройстве (minSdk 26).
4. Проверить сценарий:
   - открыть приложение;
   - использовать строку `Поиск трека / альбома`;
   - поставить несколько треков в Избранное (кнопка `☆/★`);
   - выбрать mood (`focus`, `happy`, `calm`, `energetic`, `sad`);
   - убедиться, что секция "Т-Волна" меняется;
   - нажать `Play` и увидеть "Сейчас играет".

> В текущем MVP Android-клиент использует локальный репозиторий демо-данных для оффлайн-проверки UX. Следующий шаг — подключить Repository к FastAPI через Retrofit.

## Воспроизведение и качество звука

- В Android-клиенте добавлен реальный проигрыватель на `ExoPlayer` (Media3).
- В настройках доступны два режима качества:
  - **Наилучшее (высокое)**
  - **Низкое**
- При нажатии `Play` трек запускается с URL, соответствующим выбранному качеству.

## Формула рекомендаций (MVP)

- `0.55 * similarity`
- `0.30 * mood_match`
- `0.15 * novelty`

## API endpoints

- `GET /health`
- `GET /tracks`
- `GET /search?query=<text>` (поиск по `title`, `album`, `artist`)
- `GET /favorites/{user_id}`
- `POST /favorites/{user_id}`
- `POST /feedback/{user_id}`
- `GET /wave/{user_id}?mood=focus&limit=10`
