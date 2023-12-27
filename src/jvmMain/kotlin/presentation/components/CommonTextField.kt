package presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) = TextField(
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    label = {
        Text(text = label)
    },
    shape = RoundedCornerShape(12.dp),
    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
    isError = isError,
    modifier = modifier
)