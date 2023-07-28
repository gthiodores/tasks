package sample.gthio.tasks.ui.route.addgroup

import androidx.compose.ui.graphics.Color
import sample.gthio.tasks.domain.model.DomainGroup

data class AddGroupUiState(
    val groups: List<DomainGroup> = emptyList(),
    val availableColors: List<Color> = emptyList(),
    val title: String = "",
    val selectedGroupColor: Color? = null,
    val shouldNavigateBack: Boolean = false,
)

data class AddGroupInputState(
    val title: String = "",
    val selectedGroupColor: Color? = null,
    val shouldNavigateBack: Boolean = false
)