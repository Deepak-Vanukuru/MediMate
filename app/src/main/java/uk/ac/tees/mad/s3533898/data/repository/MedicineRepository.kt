package uk.ac.tees.mad.s3533898.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.s3533898.data.local.dao.MedicineDao
import uk.ac.tees.mad.s3533898.domain.model.Medicine
import uk.ac.tees.mad.s3533898.domain.model.toDomain
import uk.ac.tees.mad.s3533898.domain.model.toEntity
import javax.inject.Inject
import javax.inject.Singleton

/** Single entry point for medicine data — Room is the offline source of truth. */
@Singleton
class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao
) {
    fun observeMedicines(): Flow<List<Medicine>> =
        medicineDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeMedicine(id: Long): Flow<Medicine?> =
        medicineDao.observeById(id).map { it?.toDomain() }

    suspend fun getMedicine(id: Long): Medicine? = medicineDao.getById(id)?.toDomain()

    suspend fun addOrUpdate(medicine: Medicine): Long =
        if (medicine.id == 0L) medicineDao.insert(medicine.toEntity())
        else { medicineDao.update(medicine.toEntity()); medicine.id }

    suspend fun delete(medicine: Medicine) = medicineDao.delete(medicine.toEntity())
}
