from pydantic import BaseModel


class Track(BaseModel):
    id: int
    title: str
    artist: str
    album: str
    genre: str
    mood: str
    energy: float
    bpm: int


class FavoritePayload(BaseModel):
    track_id: int


class FeedbackPayload(BaseModel):
    track_id: int
    action: str  # like | skip | finish


class WaveResponse(BaseModel):
    user_id: str
    mood: str
    recommendations: list[Track]
