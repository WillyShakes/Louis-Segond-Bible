package org.redesnac.lsgbible


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.redesnac.lsgbible.data.local.AppDatabase
import org.redesnac.lsgbible.data.local.VerseDao
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import java.io.IOException
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var verseDao: VerseDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).allowMainThreadQueries().build()
        verseDao = db.getVerseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun writeVerseAndReadInList() = runBlocking {
        val verse =  VerseDto(
            book = "Luc",
            chapter = "6",
            verse = "38",
            text = "Donnez, et il vous sera donné: on versera dans votre sein une bonne mesure, serrée, secouée et qui déborde; car on vous mesurera avec la mesure dont vous vous serez servis."
        )
        verseDao.insert(verse)
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            verseDao.getAllVerses().collectLatest {
                    verses ->
                assertThat(verses).contains(verse)
                latch.countDown()
            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}