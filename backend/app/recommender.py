from collections import Counter

from .data import TRACKS
from .schemas import Track


def recommend_wave(favorite_tracks: list[Track], mood: str, limit: int = 10) -> list[Track]:
    if not favorite_tracks:
        return [track for track in TRACKS if track.mood == mood][:limit] or TRACKS[:limit]

    genre_counter = Counter(track.genre for track in favorite_tracks)
    top_genre = genre_counter.most_common(1)[0][0]

    avg_energy = sum(t.energy for t in favorite_tracks) / len(favorite_tracks)
    avg_bpm = sum(t.bpm for t in favorite_tracks) / len(favorite_tracks)

    candidates = [track for track in TRACKS if track.id not in {f.id for f in favorite_tracks}]

    scored = []
    for track in candidates:
        similarity_score = 1.0
        if track.genre != top_genre:
            similarity_score -= 0.25
        similarity_score -= min(abs(track.energy - avg_energy), 1.0) * 0.5
        similarity_score -= min(abs(track.bpm - avg_bpm) / 100, 1.0) * 0.25

        mood_match = 1.0 if track.mood == mood else 0.2
        novelty = 0.5
        final_score = 0.55 * max(similarity_score, 0) + 0.30 * mood_match + 0.15 * novelty
        scored.append((final_score, track))

    scored.sort(key=lambda item: item[0], reverse=True)
    return [track for _, track in scored[:limit]]
