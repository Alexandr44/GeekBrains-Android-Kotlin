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
import io.mockk.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTestMockK {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockFireAuth = mockk<FirebaseAuth>()
    private val mockFireStore = mockk<FirebaseFirestore>()

    private val mockUsersCollection = mockk<CollectionReference>()
    private val mockUserDocument = mockk<DocumentReference>()
    private val mockResultCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf (
            Note("id1"),
            Note("id2"),
            Note("id3")
    )

    private val provider : FireStoreProvider = FireStoreProvider(mockFireAuth, mockFireStore)

    @Before
    fun setup() {
        clearMocks(mockUsersCollection, mockUserDocument, mockResultCollection, mockDocument1, mockDocument2, mockDocument3)

        every { mockFireAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "id"

        every { mockFireStore.collection(any()) } returns mockUsersCollection
        every { mockUsersCollection.document(any()) } returns mockUserDocument
        every { mockUserDocument.collection(any()) } returns mockResultCollection
        
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        runBlocking {
            every { mockFireAuth.currentUser } returns null
            val result = (provider.subscribeToAllNotes().receive() as? NoteResult.Error)?.error
            assertTrue(result is NoAuthException)
        }
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        runBlocking {
            val mockSnapshot = mockk<QuerySnapshot>()
            val slot = slot<EventListener<QuerySnapshot>>()

            every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
            every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

            val deferred = async {
                (provider.subscribeToAllNotes().receive() as? NoteResult.Success<List<Note>>)?.data
            }
            delay(100)

            slot.captured.onEvent(mockSnapshot, null)
            val result = deferred.await()

            assertEquals(testNotes, result)
        }
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        runBlocking {
            val testError = mockk<FirebaseFirestoreException>()
            val slot = slot<EventListener<QuerySnapshot>>()

            every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

            val deferred = async {
                (provider.subscribeToAllNotes().receive() as? NoteResult.Error)?.error
            }
            delay(100)

            slot.captured.onEvent(null, testError)
            val result = deferred.await()

            assertEquals(testError, result)
        }
    }

    @Test
    fun `saveNote calls document set`() {
        runBlocking {
            val mockDocumentReference = mockk<DocumentReference>()
            every {mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

            val slot = slot<OnSuccessListener<in Void>>()
            val mockTask = mockk<Task<Void>>()
            every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } returns mockTask
            every { mockDocumentReference.set(testNotes[0]) } returns mockTask

            launch {
                provider.saveNote(testNotes[0])
            }
            delay(100)

            slot.captured.onSuccess(null)
            verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
        }
    }

    @Test
    fun `saveNote returns note`() {
        runBlocking {
            val mockDocumentReference = mockk<DocumentReference>()
            val slot = slot<OnSuccessListener<in Void>>()

            val mockTask = mockk<Task<Void>>()
            every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } returns mockTask
            every { mockDocumentReference.set(testNotes[0]) } returns mockTask
            every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

            val deferred = async {
                provider.saveNote(testNotes[0]) as? Note
            }
            delay(100)

            slot.captured.onSuccess(null)
            val result = deferred.await()

            assertNotNull(result)
            assertEquals(testNotes[0], result)
        }
    }

    @Test
    fun `deleteNote calls document delete`() {
        runBlocking {
            val mockDocumentReference = mockk<DocumentReference>()
            every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

            val slot = slot<OnSuccessListener<in Void>>()
            val mockTask = mockk<Task<Void>>()
            every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } returns mockTask
            every { mockDocumentReference.delete() } returns mockTask

            launch {
                provider.deleteNote(testNotes[0])
            }
            delay(100)

            slot.captured.onSuccess(null)
            verify(exactly = 1) { mockDocumentReference.delete() }
        }
    }

}

