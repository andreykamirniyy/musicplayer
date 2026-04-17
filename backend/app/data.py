from .schemas import Track

TRACKS: list[Track] = [
    Track(id=1, title="Night Drive", artist="Polar Lights", genre="synthwave", mood="focus", energy=0.62, bpm=114),
    Track(id=2, title="Sunrise Pulse", artist="Neon District", genre="pop", mood="happy", energy=0.78, bpm=126),
    Track(id=3, title="Soft Rain", artist="Cloud Harbor", genre="lofi", mood="calm", energy=0.32, bpm=84),
    Track(id=4, title="City Run", artist="Nova Run", genre="electronic", mood="energetic", energy=0.88, bpm=132),
    Track(id=5, title="Deep Letters", artist="Velvet Lane", genre="indie", mood="sad", energy=0.40, bpm=92),
    Track(id=6, title="Warm Coffee", artist="Blue Porch", genre="acoustic", mood="calm", energy=0.28, bpm=78),
    Track(id=7, title="Bright Monday", artist="Signal Pop", genre="pop", mood="happy", energy=0.72, bpm=122),
    Track(id=8, title="Bassline Road", artist="Static Beam", genre="electronic", mood="energetic", energy=0.90, bpm=136),
    Track(id=9, title="Quiet Library", artist="Paper Tones", genre="lofi", mood="focus", energy=0.30, bpm=80),
    Track(id=10, title="Late Window", artist="Echo Frames", genre="indie", mood="sad", energy=0.36, bpm=88),
]

TRACK_BY_ID = {track.id: track for track in TRACKS}
