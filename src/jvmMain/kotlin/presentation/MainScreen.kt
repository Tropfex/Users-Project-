package presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import domain.models.User
import domain.repository.UserRepository
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.core.utils.URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.batikSvgDecoder
import io.kamel.image.config.resourcesFetcher
import presentation.components.CommonDialog
import presentation.components.CommonTextButton
import presentation.components.CommonTextField

@Composable
fun MainScreen(
    repository: UserRepository,
    modifier: Modifier = Modifier
) {
    var users by remember { mutableStateOf(repository.getUsers()) }

    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }

    var userId by remember { mutableStateOf<Long?>(null) }

    var nameToEdit by remember { mutableStateOf("") }
    var usernameToEdit by remember { mutableStateOf("") }
    var emailToEdit by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                userId = null

                nameToEdit = ""
                usernameToEdit = ""
                emailToEdit = ""

                dialogTitle = "Add user"
                isDialogVisible = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add user")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Text(
                text = "Users",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Divider(Modifier.padding(vertical = 12.dp))

            val desktopConfig = KamelConfig {
                takeFrom(KamelConfig.Default)
                resourcesFetcher()
                batikSvgDecoder()
            }

            CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
                KamelImage(
                    resource = asyncPainterResource("drawables/image.png"),
                    contentDescription = "Welcome image",
                    contentAlignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(100.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                items(users) { user ->
                    UserItem(
                        model = user,
                        onEditClick = {
                            userId = user.id

                            nameToEdit = user.name
                            usernameToEdit = user.username
                            emailToEdit = user.email

                            dialogTitle = "Edit user"
                            isDialogVisible = true
                        },
                        onDeleteClick = {
                            users = users.filter { it.id != user.id }
                        }
                    )
                }
            }
        }
    }

    if (isDialogVisible) {
        UserInfoDialog(
            title = dialogTitle,
            initialName = nameToEdit,
            initialUsername = usernameToEdit,
            initialEmail = emailToEdit,
            onDismissRequest = {
                dialogTitle = ""
                isDialogVisible = false
            },
            onSaveClick = { name, username, email ->
                if (userId == null) {
                    val newUser = User(name = name, username = username, email = email)

                    users = users + newUser
                } else {
                    users = users.map {
                        if (it.id == userId) {
                            User(id = it.id, name = name, username = username, email = email)
                        } else {
                            it
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun UserItem(
    model: User,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier.padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Image(
        imageVector = Icons.Rounded.Face,
        contentDescription = "avatar",
        modifier = Modifier.size(48.dp),
        alignment = Alignment.Center
    )

    Spacer(Modifier.width(4.dp))

    Column(
        modifier = Modifier
    ) {
        Text(
            text = model.name,
            style = MaterialTheme.typography.h6
        )

        Text(
            text = "Username:@" + model.username,
            style = MaterialTheme.typography.caption
        )

        Text(
            text = "Email: " + model.email,
            style = MaterialTheme.typography.caption
        )
    }

    Spacer(Modifier.weight(1f))

    Icon(
        imageVector = Icons.Rounded.Edit,
        contentDescription = "Edit",
        tint = Color.DarkGray,
        modifier = Modifier
            .size(36.dp)
            .clickable { onEditClick() }
            .padding(8.dp)
    )

    Icon(
        imageVector = Icons.Rounded.Delete,
        contentDescription = "Delete",
        tint = Color.Red,
        modifier = Modifier
            .size(36.dp)
            .clickable { onDeleteClick() }
            .padding(8.dp)
    )
}

@Composable
private fun UserInfoDialog(
    title: String,
    initialName: String,
    initialUsername: String,
    initialEmail: String,
    onDismissRequest: () -> Unit,
    onSaveClick: (name: String, username: String, email: String) -> Unit
) = CommonDialog(
    title = title,
    onDismissRequest = onDismissRequest
) {
    var name by remember { mutableStateOf(initialName) }
    var username by remember { mutableStateOf(initialUsername) }
    var email by remember { mutableStateOf(initialEmail) }

    var isNameError by remember { mutableStateOf(false) }
    var isUsernameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    CommonTextField(
        value = name,
        onValueChange = {
            name = it
            isNameError = false
        },
        label = "Name",
        isError = isNameError,
        modifier = Modifier.fillMaxWidth()
    )

    CommonTextField(
        value = username,
        onValueChange = {
            username = it
            isUsernameError = false
        },
        label = "Username",
        isError = isUsernameError,
        modifier = Modifier.fillMaxWidth()
    )

    CommonTextField(
        value = email,
        onValueChange = {
            email = it
            isEmailError = false
        },
        label = "Email",
        isError = isEmailError,
        modifier = Modifier.fillMaxWidth()
    )

    Row {
        CommonTextButton(title = "Cancel", color = Color.Black) { onDismissRequest() }
        CommonTextButton(title = "Save", color = Color.Black) {
            if (name.isBlank()) isNameError = true
            if (username.isBlank()) isUsernameError = true
            if (email.isBlank()) isEmailError = true
            if (isNameError || isUsernameError || isEmailError) return@CommonTextButton

            onSaveClick(name, username, email)
            onDismissRequest()
        }
    }
}