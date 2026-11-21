package uk.ac.tees.mad.s3533898.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import uk.ac.tees.mad.s3533898.core.security.DatabaseKeyProvider
import uk.ac.tees.mad.s3533898.data.local.MediMateDatabase
import uk.ac.tees.mad.s3533898.data.local.dao.MedicineDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keyProvider: DatabaseKeyProvider
    ): MediMateDatabase {
        // Encryption at rest: the Room database is backed by SQLCipher using a
        // random passphrase kept in the Keystore-protected secure prefs.
        System.loadLibrary("sqlcipher")
        val factory = SupportOpenHelperFactory(keyProvider.getOrCreatePassphrase())
        return Room.databaseBuilder(context, MediMateDatabase::class.java, MediMateDatabase.NAME)
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMedicineDao(db: MediMateDatabase): MedicineDao = db.medicineDao()
}
