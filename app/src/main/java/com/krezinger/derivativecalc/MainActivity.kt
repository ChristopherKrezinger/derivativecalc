package com.krezinger.derivativecalc

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krezinger.derivativecalc.ui.theme.DerivativecalcTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DerivativecalcTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

val MenuList: Array<String> = arrayOf(
    "History",
    "About Me"
)

val TopBarHeight: Int = 64



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ListInMenu() {
    ModalDrawerSheet( modifier = Modifier
        .offset(y= TopBarHeight.dp),
        drawerContainerColor = MaterialTheme.colorScheme.onPrimary
    )
            {
        MenuList.forEachIndexed{
            index, itemText -> Text(
                text = itemText,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenWithDrawer() {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = { ListInMenu() },
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
        content = { MainContainer() }
        )
    }
}

@Composable
fun MainContainer(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary))
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    DerivativecalcTheme(dynamicColor = false) {
        MainScreenWithDrawer()

    }
}