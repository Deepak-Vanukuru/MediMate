package uk.ac.tees.mad.s3533898.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A medicine the user takes. FDA-sourced fields (filled from Sprint 3) are
 * nullable so the schema is stable from Sprint 2 onward — no migration needed.
 */
@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: String = "",
    val form: String = "",
    val notes: String = "",
    // Populated from openFDA / RxNorm in Sprint 3
    val genericName: String? = null,
    val brandName: String? = null,
    val rxcui: String? = null,
    val fdaPurpose: String? = null,
    val fdaWarnings: String? = null,
    val fdaInteractions: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)
