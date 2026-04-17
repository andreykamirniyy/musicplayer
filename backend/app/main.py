from fastapi import FastAPI, HTTPException

from .data import TRACKS, TRACK_BY_ID
from .recommender import recommend_wave
from .schemas import FavoritePayload, FeedbackPayload, Track, WaveResponse

app = FastAPI(title="T-Music MVP API", version="0.1.0")

favorites_by_user: dict[str, set[int]] = {}
feedback_events: list[dict] = []


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.get("/tracks", response_model=list[Track])
def list_tracks() -> list[Track]:
    return TRACKS


@app.get("/favorites/{user_id}", response_model=list[Track])
def get_favorites(user_id: str) -> list[Track]:
    ids = favorites_by_user.get(user_id, set())
    return [TRACK_BY_ID[track_id] for track_id in ids if track_id in TRACK_BY_ID]


@app.post("/favorites/{user_id}")
def add_favorite(user_id: str, payload: FavoritePayload) -> dict[str, str]:
    if payload.track_id not in TRACK_BY_ID:
        raise HTTPException(status_code=404, detail="Track not found")
    favorites = favorites_by_user.setdefault(user_id, set())
    favorites.add(payload.track_id)
    return {"result": "added"}


@app.post("/feedback/{user_id}")
def push_feedback(user_id: str, payload: FeedbackPayload) -> dict[str, str]:
    if payload.track_id not in TRACK_BY_ID:
        raise HTTPException(status_code=404, detail="Track not found")
    if payload.action not in {"like", "skip", "finish"}:
        raise HTTPException(status_code=400, detail="Action must be like|skip|finish")
    feedback_events.append({"user_id": user_id, "track_id": payload.track_id, "action": payload.action})
    return {"result": "recorded"}


@app.get("/wave/{user_id}", response_model=WaveResponse)
def get_wave(user_id: str, mood: str = "focus", limit: int = 10) -> WaveResponse:
    favorites = [TRACK_BY_ID[tid] for tid in favorites_by_user.get(user_id, set()) if tid in TRACK_BY_ID]
    recommendations = recommend_wave(favorites, mood=mood, limit=limit)
    return WaveResponse(user_id=user_id, mood=mood, recommendations=recommendations)
