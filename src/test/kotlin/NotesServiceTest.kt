import org.testng.annotations.Test
import org.junit.Assert.*

class NotesServiceTest {

    @Test
    fun addNote() {
        val noteOne = Note(2,"Title", "Text", 0, 0, false)
        val notes = NotesService

        notes.addNote(noteOne)
        assertEquals(Note(2,"Title", "Text", 0, 0, false), noteOne)
    }
}