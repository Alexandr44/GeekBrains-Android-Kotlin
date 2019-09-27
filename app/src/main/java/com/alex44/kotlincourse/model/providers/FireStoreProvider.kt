package com.alex44.kotlincourse.model.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import timber.log.Timber

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes_collection"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener {snapshot, error ->
            if (error != null) {
                result.value = NoteResult.Error(error)
            }
            else if (snapshot != null) {
                val notes = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener {
                    result.value = NoteResult.Success(it.toObject(Note::class.java))
                }
                .addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    Timber.d("Note $note is saved to firestore")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Timber.e("Error while saving note $note with message: ${it.message}")
                    result.value = NoteResult.Error(it)
                }
        return result
    }

    override fun deleteNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).delete()
                .addOnSuccessListener {
                    Timber.d("Note $note is deleted from firestore")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Timber.e("Error while deleting note $note with message: ${it.message}")
                    result.value = NoteResult.Error(it)
                }
        return result
    }

}