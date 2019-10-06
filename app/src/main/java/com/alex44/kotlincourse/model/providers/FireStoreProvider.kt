package com.alex44.kotlincourse.model.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.dtos.User
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes_collection"
        private const val USERS_COLLECTION = "users_collection"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }
    private val fireStoreUser
        get() = FirebaseAuth.getInstance().currentUser


    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection()
                    .addSnapshotListener { snapshot, error ->
                        value = error?.let { exc ->
                            NoteResult.Error(exc)
                        } ?: let {
                            snapshot?.let {
                                val notes = it.documents.map { doc ->
                                    doc.toObject(Note::class.java)
                                }
                                NoteResult.Success(notes)
                            }
                        }
                    }
        }
        catch (e : Throwable) {
            NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(id).get()
                    .addOnSuccessListener {
                        value = NoteResult.Success(it.toObject(Note::class.java))
                    }
                    .addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        }
        catch (e : Throwable) {
            NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        Timber.d("Note $note is saved to firestore")
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener {
                        Timber.e("Error while saving note $note with message: ${it.message}")
                        value = NoteResult.Error(it)
                    }
        }
        catch (e : Throwable) {
            NoteResult.Error(e)
        }
    }

    override fun deleteNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(note.id).delete()
                    .addOnSuccessListener {
                        Timber.d("Note $note is deleted from firestore")
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener {
                        Timber.e("Error while deleting note $note with message: ${it.message}")
                        value = NoteResult.Error(it)
                    }
        }
        catch (e : Throwable) {
            NoteResult.Error(e)
        }
    }

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = fireStoreUser?.let {
            User(it.displayName ?: "", it.email ?: "", it.phoneNumber ?: "", it.photoUrl?.toString() ?: "")
        }
    }

    private fun getUserNotesCollection() = fireStoreUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

}