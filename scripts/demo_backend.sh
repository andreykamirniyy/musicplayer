#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-http://127.0.0.1:8000}"

echo "== health =="
curl -sS "${BASE_URL}/health"; echo

echo "== tracks =="
curl -sS "${BASE_URL}/tracks"; echo

echo "== search query=Morning (track/album/artist) =="
curl -sS "${BASE_URL}/search?query=Morning"; echo

echo "== add favorite track_id=1 for user demo =="
curl -sS -X POST "${BASE_URL}/favorites/demo" \
  -H 'Content-Type: application/json' \
  -d '{"track_id":1}'; echo

echo "== wave (focus) =="
curl -sS "${BASE_URL}/wave/demo?mood=focus"; echo
