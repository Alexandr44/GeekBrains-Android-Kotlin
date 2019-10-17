package com.alex44.kotlincourse.model.providers

import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.dtos.User
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(private val firebaseAuth : FirebaseAuth, private val store : FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes_collection"
        private const val USERS_COLLECTION = "users_collection"
    }

    private val fireStoreUser
        get() = firebaseAuth.currentUser


    @ExperimentalCoroutinesApi
    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> = Channel<NoteResult>(CONFLATED).apply {
        var registration: ListenerRegistration? = null

        try {
            registration = getUserNotesCollection()
                    .addSnapshotListener { snapshot, error ->
                        val value = error?.let { exc ->
                            NoteResult.Error(exc)
                        } ?: let {
                            snapshot?.let {
                                val notes = it.documents.map { doc ->
                                    doc.toObject(Note::class.java)
                                }
                                NoteResult.Success(notes)
                            }
                        }
                        value?.let { offer(it) }
                    }
        }
        catch (e : Throwable) {
            offer(NoteResult.Error(e))
        }

        invokeOnClose { registration?.remove() }
    }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine { continuation ->
        try {
            getUserNotesCollection().document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(it.toObject(Note::class.java)!!)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine {continuation ->
        try {
            getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        Timber.d("Note $note is saved to firestore")
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        Timber.e("Error while saving note $note with message: ${it.message}")
                        continuation.resumeWithException(it)
                    }
        }
        catch (e : Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(note: Note): Note = suspendCoroutine {continuation ->
        try {
            getUserNotesCollection().document(note.id).delete()
                    .addOnSuccessListener {
                        Timber.d("Note $note is deleted from firestore")
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        Timber.e("Error while deleting note $note with message: ${it.message}")
                        continuation.resumeWithException(it)
                    }
        }
        catch (e : Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine {continuation ->
        continuation.resume( fireStoreUser?.let {
            User(it.displayName ?: "", it.email ?: "", it.phoneNumber ?: "", it.photoUrl?.toString() ?: "")
        })
    }

    private fun getUserNotesCollection() = fireStoreUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

}