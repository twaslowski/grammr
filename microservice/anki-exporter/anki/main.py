import tempfile

import genanki
from fastapi import FastAPI
from fastapi.responses import FileResponse

from anki.domain.deck import Deck
from anki.model import MODEL

app = FastAPI()


@app.post("/create")
def create_deck(deck_data: Deck):
    deck = genanki.Deck(deck_data.id, deck_data.name)
    notes = create_anki_notes(deck_data)
    for note in notes:
        deck.add_note(note)

    with tempfile.NamedTemporaryFile(delete=False, suffix=".apkg") as temp_file:
        package = genanki.Package(deck)
    package.write_to_file(temp_file.name)
    temp_file_path = temp_file.name

    return FileResponse(
        temp_file_path,
        filename=f"{deck_data.name}.apkg",
        media_type="application/octet-stream",
    )


@app.get("/health")
async def health():
    return {"status": "UP"}


def create_anki_notes(deck_data):
    return [
        genanki.Note(model=MODEL, fields=[note.question, note.answer])
        for note in deck_data.notes
    ]
