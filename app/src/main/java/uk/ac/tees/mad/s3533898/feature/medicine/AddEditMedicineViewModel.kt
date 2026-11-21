package uk.ac.tees.mad.s3533898.feature.medicine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3533898.core.navigation.Screen
import uk.ac.tees.mad.s3533898.data.repository.MedicineRepository
import uk.ac.tees.mad.s3533898.domain.model.Medicine
import javax.inject.Inject

data class MedicineFormState(
    val id: Long = 0L,
    val name: String = "",
    val dosage: String = "",
    val form: String = "",
    val notes: String = "",
    val isEditing: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false,
    val deleted: Boolean = false
)

/**
 * Add a new medicine or edit/delete an existing one (stories B1 and B3). When
 * opened with a medicine id it loads that record for editing; otherwise it
 * starts a blank add form. All writes go through the repository to the
 * encrypted Room database.
 */
@HiltViewModel
class AddEditMedicineViewModel @Inject constructor(
    private val repository: MedicineRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val medicineId: Long =
        savedStateHandle.get<String>(Screen.MedicineForm.ARG_ID)?.toLongOrNull() ?: 0L

    private val _state = MutableStateFlow(MedicineFormState())
    val state = _state.asStateFlow()

    private var loaded: Medicine? = null

    init {
        if (medicineId > 0) {
            viewModelScope.launch {
                repository.getMedicine(medicineId)?.let { med ->
                    loaded = med
                    _state.update {
                        it.copy(
                            id = med.id, name = med.name, dosage = med.dosage,
                            form = med.form, notes = med.notes, isEditing = true
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v, error = null) }
    fun onDosageChange(v: String) = _state.update { it.copy(dosage = v) }
    fun onFormChange(v: String) = _state.update { it.copy(form = v) }
    fun onNotesChange(v: String) = _state.update { it.copy(notes = v) }

    fun save() {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(error = "Please enter a medicine name") }
            return
        }
        viewModelScope.launch {
            val toSave = (loaded ?: Medicine(name = "")).copy(
                id = if (s.id > 0) s.id else 0,
                name = s.name.trim(),
                dosage = s.dosage.trim(),
                form = s.form.trim(),
                notes = s.notes.trim()
            )
            repository.addOrUpdate(toSave)
            _state.update { it.copy(saved = true) }
        }
    }

    fun delete() {
        val current = loaded ?: return
        viewModelScope.launch {
            repository.delete(current)
            _state.update { it.copy(deleted = true) }
        }
    }
}
