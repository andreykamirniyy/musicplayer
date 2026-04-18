from backend.app.data import TRACKS
from backend.app.recommender import recommend_wave


def test_recommend_wave_returns_limit_items():
    favorite_tracks = [TRACKS[0], TRACKS[1]]
    recommendations = recommend_wave(favorite_tracks, mood="happy", limit=3)
    assert len(recommendations) == 3


def test_recommend_wave_excludes_favorites():
    favorite_tracks = [TRACKS[0]]
    recommendations = recommend_wave(favorite_tracks, mood="focus", limit=10)
    assert all(track.id != TRACKS[0].id for track in recommendations)


def test_recommend_wave_fallback_without_favorites():
    recommendations = recommend_wave([], mood="energetic", limit=5)
    assert len(recommendations) > 0
