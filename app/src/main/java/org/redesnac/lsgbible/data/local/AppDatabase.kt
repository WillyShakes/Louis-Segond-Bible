package org.redesnac.lsgbible.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import org.redesnac.lsgbible.BuildConfig
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto


@Database(entities = [VerseDto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getVerseDao(): VerseDao

    companion object {
        private const val DATABASE_NAME = "LsgB_database"

        fun create(
            application: Application,
            workManager: WorkManager,
            gson: Gson,
        ): AppDatabase {
            val builder = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            )

            builder.addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val entitiesAsJson = gson.toJson(
                        listOf(
                            VerseDto(
                                book = "Luc",
                                chapter = "6",
                                verse = "38",
                                text = "Donnez, et il vous sera donné: on versera dans votre sein une bonne mesure, serrée, secouée et qui déborde; car on vous mesurera avec la mesure dont vous vous serez servis."
                            ),
                            VerseDto(
                                book = "Matthieu",
                                chapter = "6",
                                verse = "38",
                                text = "Cherchez premièrement le royaume et la justice de Dieu; et toutes ces choses vous seront données par-dessus."),
                            VerseDto(
                                book = "Osée",
                                chapter = "4",
                                verse = "6",
                                text = "Mon peuple est détruit, parce qu'il lui manque la connaissance. Puisque tu as rejeté la connaissance, Je te rejetterai, et tu seras dépouillé de mon sacerdoce; Puisque tu as oublié la loi de ton Dieu, J'oublierai aussi tes enfants."),
                        )
                    )

                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                            .setInputData(workDataOf(InitializeDatabaseWorker.KEY_INPUT_DATA to entitiesAsJson))
                            .build()
                    )

                }
            })

            if (BuildConfig.DEBUG) {
                builder.fallbackToDestructiveMigration()
            }

            return builder.build()
        }
    }
}