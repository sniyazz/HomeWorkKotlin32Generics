

fun main(args: Array<String>) {
    //Создание заметки
    val noteOne = NotesService.addNote(Note(1, "title1", "Text1", 0, 0))
    val noteTwo = NotesService.addNote(Note(2, "title2", "Text2", 0, 0))
    val noteThree = NotesService.addNote(Note(3, "title3", "Text3", 0, 0))

//Создание комментария
    val commentOne = NotesService.createComment(1, NoteСomment(1, 201, 301,"Comment1"))
    val commentTwo = NotesService.createComment(2, NoteСomment(2, 202, 302,"Comment2"))
    val commentThree = NotesService.createComment(3, NoteСomment(2, 203, 303,"Comment3"))

// Редактирование заметки:
    NotesService.getNotes()
    NotesService.editNote(1, Note(1, "Edited", "Edited text", 0, 0))
    NotesService.getNotes()

    //Удаление заметок
    println("Заметок до удаления - " + NotesService.notes.size)
    NotesService.removeNote(1)
    println("Заметок после удаления - " + NotesService.notes.size)
    println()

    //Список комментов
    NotesService.getComment(2)
    println()

    //Редактирование комментария заметки
    NotesService.editComment(202, NoteСomment(2,202, 302, "Edited comment 2" ))
    NotesService.getComment(2)
    println()

    //Удаление комментария заметки
    println("Комментариев до удаления - " + NotesService.comments.size)
    NotesService.removeComment(2, 202)
    println("Комментариев после удаления - " + NotesService.comments.size)
    NotesService.getComment(2)
    println()

    //Восстановление удаленного комментария
    println("Комментариев до восстановления - " + NotesService.comments.size)
    NotesService.restoreComment(2,202)
    println("Комментариев после восстановления - " + NotesService.comments.size)
    NotesService.getComment(2)
    println()

}

class NoteNotFoundException(message: String) : RuntimeException(message)

data class Note(
    var noteId: Int,
    val title: String,
    val text: String,
    val privacy: Int = 0, //Уровень доступа к заметке: All Users
    val commentPrivacy: Int = 0, //Уровень доступа к комментированию заметки: All Users
    var deleted: Boolean = false //Удалена ли заметка

)

data class NoteСomment(
    val noteId: Int,
    val commentId: Int,
    val ownerId: Int,
    val message: String,
    var deleted: Boolean = false //Удален ли комментарий
)

object NotesService { //Create Note
    var notes = emptyArray<Note>()
    var comments = emptyArray<NoteСomment>()
    var deletedComments = emptyArray<NoteСomment>()

    fun addNote(note: Note): Note {
        notes += note.copy(noteId = note.noteId)
        return notes.last()
    }

    fun editNote(noteId: Int, note: Note): Note {
        for ((index, n) in notes.withIndex())
            if (n.noteId == noteId && !note.deleted) {
                notes[index] = n.copy(
                    title = note.title,
                    text = note.text,
                    privacy = note.privacy,
                    commentPrivacy = note.commentPrivacy
                )
                return notes[index]
            }
        throw NoteNotFoundException("Not found Note ID $noteId")
    }

    fun getNotes() {
        for ((index, n) in notes.withIndex()) {
            println("Notes list: " + n.noteId + " - " + n.title + " - " + n.text)
        }
        println()
    }

    fun removeNote(noteId: Int): Note {
        for ((index, n) in notes.withIndex()) {
            if (n.noteId == noteId && !n.deleted) {
                notes[index] = n.copy(deleted = true)
                notes = notes.filter { it.deleted == n.deleted }.toTypedArray()
            }
        }
        return notes.last()
    }

    fun createComment(noteId: Int, noteСomment: NoteСomment): NoteСomment {
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId && !note.deleted) {
                comments += noteСomment.copy(
                    commentId = noteСomment.commentId, ownerId = noteСomment.ownerId,
                    message = noteСomment.message
                )
            }
        }
        return comments.last()
    }

    fun editComment(commentId: Int, comment: NoteСomment): NoteСomment {
        for ((index, com) in comments.withIndex()) {
            if (com.commentId == commentId && !com.deleted) {
                comments[index] = comment.copy(message = comment.message)
            }
            return comments[index]
        }
        throw NoteNotFoundException("Not found Note ID $commentId")
    }

    fun removeComment(noteId: Int, commentId: Int): NoteСomment {
        for ((index, commen) in comments.withIndex()) {
            if (commen.noteId == noteId && commen.commentId == commentId && !commen.deleted ) {
                comments[index] = commen.copy(deleted = true)
                deletedComments+= comments[index]
                comments = comments.filter { it.deleted == commen.deleted }.toTypedArray()
            }
        }
        return comments.last()
    }

    fun getComment(noteId: Int) {
        for ((index, commen) in comments.withIndex()) {
            if (commen.noteId == noteId  && !commen.deleted) {
                println("Comments for noteID $noteId -  " + commen.commentId + " * " + commen.ownerId + " * " + commen.message)
            }
        }
    }

    fun restoreComment(noteId: Int, commentId: Int): NoteСomment {
        for ((index, commR) in deletedComments.withIndex()) {
            if (commR.noteId == noteId && commR.commentId == commentId && commR.deleted) {
                deletedComments[index] = commR.copy(deleted = false)
                comments += deletedComments[index]
            }
        }
        return comments.last()
    }
}

