package uk.ac.tees.mad.s3533898.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.s3533898.data.local.dao.MedicineDao
import uk.ac.tees.mad.s3533898.data.local.entity.MedicineEntity

@Database(
    entities = [MedicineEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MediMateDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao

    companion object {
        const val NAME = "medimate.db"
    }
}
