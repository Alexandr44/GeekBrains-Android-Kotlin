package com.alex44.kotlincourse.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.providers.FireStoreProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockFireAuth = mock<FirebaseAuth>()
    private val mockFireStore = mock<FirebaseFirestore>()

    private val mockUsersCollection = mock<CollectionReference>()
    private val mockUserDocument = mock<DocumentReference>()
    private val mockResultCollection = mock<CollectionReference>()
    private val mockUser = mock<FirebaseUser>()

    private val mockDocument1 = mock<DocumentSnapshot>()
    private val mockDocument2 = mock<DocumentSnapshot>()
    private val mockDocument3 = mock<DocumentSnapshot>()

    private val testNotes = listOf (
            Note("id1"),
            Note("id2"),
            Note("id3")
    )

    private val provider : FireStoreProvider = FireStoreProvider(mockFireAuth, mockFireStore)

    @Before
    fun setup() {
        reset(mockUsersCollection, mockUserDocument, mockResultCollection, mockDocument1, mockDocument2, mockDocument3)

        whenever(mockFireAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("id")

        whenever(mockFireStore.collection(any())).thenReturn(mockUsersCollection)
        whenever(mockUsersCollection.document(any())).thenReturn(mockUserDocument)
        whenever(mockUserDocument.collection(any())).thenReturn(mockResultCollection)

        whenever(mockDocument1.toObject(Note::class.java)).thenReturn(testNotes[0])
        whenever(mockDocument2.toObject(Note::class.java)).thenReturn(testNotes[1])
        whenever(mockDocument3.toObject(Note::class.java)).thenReturn(testNotes[2])
    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        runBlocking {
            whenever(mockFireAuth.currentUser).thenReturn(null)
            val result = (provider.subscribeToAllNotes().receive() as? NoteResult.Error)?.error
            assertTrue(result is NoAuthException)
        }
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        runBlocking {
            val mockSnapshot = mock<QuerySnapshot>()
            val captor = argumentCaptor<EventListener<QuerySnapshot>>()

            whenever(mockSnapshot.documents).thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
            whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())

            val deferred = async {
                (provider.subscribeToAllNotes().receive() as? NoteResult.Success<List<Note>>)?.data
            }
            delay(100)

            captor.firstValue.onEvent(mockSnapshot, null)
            val result = deferred.await()

            assertEquals(testNotes, result)
        }
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        runBlocking {
            val testError = mock<FirebaseFirestoreException>()
            val captor = argumentCaptor<EventListener<QuerySnapshot>>()

            whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())

            var deferred = async {
                (provider.subscribeToAllNotes().receive() as? NoteResult.Error)?.error
            }
            delay(100)

            captor.firstValue.onEvent(null, testError)
            val result = deferred.await()

            assertEquals(testError, result)
        }
    }

    @Test
    fun `saveNote calls document set`() {
        runBlocking {
            val mockDocumentReference = mock<DocumentReference>()
            whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)

            val captor = argumentCaptor<OnSuccessListener<in Void>>()
            val mockTask = mock<Task<Void>>()
            whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
            whenever(mockDocumentReference.set(testNotes[0])).thenReturn(mockTask)

            launch {
                provider.saveNote(testNotes[0])
            }
            delay(100)

            captor.firstValue.onSuccess(null)
            verify(mockDocumentReference, times(1)).set(testNotes[0])
        }
    }

    @Test
    fun `saveNote returns note`() {
        runBlocking {
            val mockDocumentReference = mock<DocumentReference>()
            val captor = argumentCaptor<OnSuccessListener<in Void>>()

            val mockTask = mock<Task<Void>>()
            whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
            whenever(mockDocumentReference.set(testNotes[0])).thenReturn(mockTask)
            whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)

            val deferred = async {
                provider.saveNote(testNotes[0]) as? Note
            }
            delay(100)

            captor.firstValue.onSuccess(null)
            val result = deferred.await()

            assertNotNull(result)
            assertEquals(testNotes[0], result)
        }
    }

    @Test
    fun `deleteNote calls document delete`() {
        runBlocking {
            val mockDocumentReference = mock<DocumentReference>()
            whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)

            val captor = argumentCaptor<OnSuccessListener<in Void>>()
            val mockTask = mock<Task<Void>>()
            whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
            whenever(mockDocumentReference.delete()).thenReturn(mockTask)

            launch {
                provider.deleteNote(testNotes[0])
            }
            delay(100)

            captor.firstValue.onSuccess(null)
            verify(mockDocumentReference, times(1)).delete()
        }
    }

}

