package uk.ac.tees.mad.s3533898.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.s3533898.data.local.entity.MedicineEntity

/**
 * Room access for medicines. Sprint 2 (first half) covers insert and the
 * observe/read queries; update and delete arrive with story B3.
 */
@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    fun observeById(id: Long): Flow<MedicineEntity?>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getById(id: Long): MedicineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity): Long
}
