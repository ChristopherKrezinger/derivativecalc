package com.krezinger.derivativecalc

import android.R
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.krezinger.derivativecalc.ui.theme.DerivativecalcTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DerivativecalcTheme{
                ScreenWithDrawer()
            }
        }
    }
}

sealed class Screen {
    object Home: Screen()
    object History: Screen()
    object AboutMe: Screen()
}

private const val AboutMeText: String = "Who ever did it to this page, first of all thank you for trying my app. " +
        "I am currently pursuing a bachelor degree in computer science. To further develop my skills and " +
        "to learn Kotlin, I made this simple app with Android Studio."



/**
 * creates clickable button
 * @param [title] text in the Button
 * @param [padding] size of text and button
 * @param [color] color of the button, default transparent
 * @param [modifier] modifies the apperence of outer parent composable
 * @param [buttonForDrawer] default true, if needed for drawer
 * @param [onClick] lambda function for onclickevent
 */
@Composable
fun ClickableButton(title: String,
                    padding: Dp = 30.dp,
                    color: Color = Color.Transparent,
                    modifier: Modifier = Modifier,
                    buttonForDrawer: Boolean = true,
                    onClick: () -> Unit ){

    Card(colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
    ){
        Box(modifier =
            if(buttonForDrawer) {
                Modifier.fillMaxWidth()
                    .clickable{onClick()}
            }
            else {
                Modifier.clickable {onClick()}
            }
        ) {
            Text(modifier = Modifier.padding(padding),
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListInMenu(drawerState: DrawerState,
               scope: CoroutineScope,
               onScreenSelected: (Screen) -> Unit) {
    val menuItems = mapOf(
        "Home" to Screen.Home,
        "About me" to Screen.AboutMe,
        "History" to Screen.History
    )
    ModalDrawerSheet(
        modifier = Modifier
            .offset(
                y = TopAppBarDefaults.TopAppBarExpandedHeight +
                        WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
            ),
        drawerContainerColor = MaterialTheme.colorScheme.onPrimary,
        windowInsets = WindowInsets(0)
    ) {
        menuItems.forEach { (title, screenObject) ->
            ClickableButton(
                title = title,
                onClick = {
                    onScreenSelected(screenObject)
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    }
}




@Composable
fun HomeScreen(){
    val cardShape = CardDefaults.shape
    val borderWidth = 1.dp
    val shadowElevation = 8.dp
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
        ){
        ClickableButton(
            title = "GO",
            modifier = Modifier
                .shadow(
                    elevation = shadowElevation,
                    shape = cardShape)
                .border(
                    width = borderWidth,
                    shape = cardShape,
                color = MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.onPrimary,
            buttonForDrawer = false
        ) {TODO() }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMeScreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center

    ){
        TextField(
            readOnly = true,
            value = AboutMeText,
            onValueChange = {}
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String = "", onMenuClick: () -> Unit){
    TopAppBar(title = { Text(title) },
              colors = TopAppBarDefaults.topAppBarColors(
                  containerColor = MaterialTheme.colorScheme.onPrimary
              ),
              navigationIcon = {
                IconButton(onClick = onMenuClick){
                    Icon(Icons.Default.Menu, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
              }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenWithDrawer(){
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen: Screen by remember { mutableStateOf(Screen.Home) }

    ModalNavigationDrawer(
        drawerContent = { ListInMenu(
            drawerState = drawerState,
            scope = scope,
            onScreenSelected = { screenThatWasSelected ->
                currentScreen = screenThatWasSelected
            }
        ) },
        drawerState = drawerState
    ){
        Scaffold(topBar = {
            TopBar {
                scope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }
            }
        },
            content = { innerPadding ->
                when(currentScreen) {
                    Screen.AboutMe -> AboutMeScreen()
                    Screen.History -> TODO()
                    Screen.Home -> HomeScreen()
                }
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AppPreview() {
    DerivativecalcTheme(dynamicColor = false) {
        ScreenWithDrawer()

    }
}