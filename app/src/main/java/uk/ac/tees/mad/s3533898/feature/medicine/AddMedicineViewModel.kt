package uk.ac.tees.mad.s3533898.feature.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3533898.data.repository.MedicineRepository
import uk.ac.tees.mad.s3533898.domain.model.Medicine
import javax.inject.Inject

data class AddMedicineState(
    val name: String = "",
    val dosage: String = "",
    val form: String = "",
    val notes: String = "",
    val error: String? = null,
    val saved: Boolean = false
)

/**
 * Add a new medicine (story B1). Validates the name and persists to the
 * encrypted Room database through the repository. Edit and delete (B3) are
 * added in the second half of Sprint 2.
 */
@HiltViewModel
class AddMedicineViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedicineState())
    val state = _state.asStateFlow()

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
            repository.add(
                Medicine(
                    name = s.name.trim(),
                    dosage = s.dosage.trim(),
                    form = s.form.trim(),
                    notes = s.notes.trim()
                )
            )
            _state.update { it.copy(saved = true) }
        }
    }
}
