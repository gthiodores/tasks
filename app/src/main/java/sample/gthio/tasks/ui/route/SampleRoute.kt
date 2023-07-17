import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.theme.surfaceGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleRoute() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskAppBar(
                title = { Text(text = "This is an appbar") }
            )
        },
        containerColor = surfaceGray,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(200) {
                Text(text = "Sample Route")
            }
        }
    }
}