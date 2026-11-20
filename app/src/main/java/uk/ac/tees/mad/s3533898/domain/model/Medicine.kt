package uk.ac.tees.mad.s3533898.domain.model

import uk.ac.tees.mad.s3533898.data.local.entity.MedicineEntity

/** Domain representation of a medicine, decoupled from the Room entity. */
data class Medicine(
    val id: Long = 0,
    val name: String,
    val dosage: String = "",
    val form: String = "",
    val notes: String = "",
    val genericName: String? = null,
    val brandName: String? = null,
    val rxcui: String? = null,
    val fdaPurpose: String? = null,
    val fdaWarnings: String? = null,
    val fdaInteractions: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)

fun MedicineEntity.toDomain() = Medicine(
    id, name, dosage, form, notes, genericName, brandName, rxcui,
    fdaPurpose, fdaWarnings, fdaInteractions, addedAt
)

fun Medicine.toEntity() = MedicineEntity(
    id, name, dosage, form, notes, genericName, brandName, rxcui,
    fdaPurpose, fdaWarnings, fdaInteractions, addedAt
)
