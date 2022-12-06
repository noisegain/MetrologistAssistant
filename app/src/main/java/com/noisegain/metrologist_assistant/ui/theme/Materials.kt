package com.noisegain.metrologist_assistant.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SexyTextField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    init: String = "",
    icon: ImageVector? = null,
    onChange: (String) -> Unit = {},
) {
    val value = remember {
        mutableStateOf(init)
    }
    TextField(
        value = value.value,
        onValueChange = {
            value.value = it
            onChange(it)
        },
        trailingIcon = {
            if (icon != null) Icon(
                icon,
                "icon",
                modifier = Modifier.size(26.dp)
            )
        },
        placeholder = { Text(placeholder) },
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large).border(2.dp, color = MaterialTheme.colors.Background2, MaterialTheme.shapes.large),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.TFBackground)
    )
}

@Composable
fun SexyButton(
    modifier: Modifier = Modifier,
    name: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    content: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = YellowPrimary)
        //.background(Color.Yellow)
    ) {
        if (content != null) {
            content()
        } else {
            Row {
                if (icon != null) {
                    Icon(
                        icon,
                        "icon",
                        modifier = Modifier.size(56.dp)
                    )
                }
                if (name != null) {
                    Text(name, color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .size(72.dp),
        onClick = onClick
    ) {
        Icon(
            icon, null,
        )
    }
}

data class ChipItem(
    val name: String,
    val img: Int,
    val isSelected: Boolean = false
)

@Composable
fun Chip(item: ChipItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (item.isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    ) {
        val chipModifier =
            if (!item.isSelected) Modifier.border(
                1.dp,
                MaterialTheme.colors.onBackground,
                MaterialTheme.shapes.medium
            ) else Modifier
        Row(
            chipModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = item.img),
                null,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                item.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}
