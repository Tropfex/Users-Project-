import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.UserRepositoryImpl
import presentation.MainScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen(
            repository = UserRepositoryImpl(),
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
