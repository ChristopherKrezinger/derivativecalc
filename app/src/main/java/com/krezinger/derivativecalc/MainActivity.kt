package com.krezinger.derivativecalc

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
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

private const val AboutMeText: String = "Who ever did it to this page, first of all thank you for trying my app." +
        "I am currently pursuing a bachelor degree in computer science. To further develop my skills and " +
        "to learn Kotlin, I made this simple app with Android Studio."

private val StandardPadding: Dp = 30.dp

@Composable
fun ClickableButton(title: String,
                    color: Color = Color.Transparent,
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit ){
    /* somehow needs to be more scalable for "enter" button */
    Card(colors = CardDefaults.cardColors(containerColor = color))
     {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
        ) {
            Text(modifier = modifier,text = title, color = MaterialTheme.colorScheme.onPrimaryContainer)
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
                modifier = Modifier.padding(StandardPadding),
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
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMeScreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center

    ){
        Text(text = AboutMeText,
            modifier = Modifier.padding(StandardPadding))
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppPreview() {
    DerivativecalcTheme(dynamicColor = false) {
        ScreenWithDrawer()

    }
}