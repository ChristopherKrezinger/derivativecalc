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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.CardColors
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krezinger.derivativecalc.ui.theme.DerivativecalcTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DerivativecalcTheme(dynamicColor = false) {
                MainScreenWithDrawer()
            }
        }
    }
}

val MenuList: Array<String> = arrayOf(
    "History",
    "About Me"
)

@Composable
fun ClickableButton(title: String,
                    color: Color = Color.Transparent,
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit ){
    /* somehow needs to be more scalable for enter button */
    Card(colors = CardDefaults.cardColors(containerColor = color))
     {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick}
        ) {
            Text(modifier = modifier,text = title, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }

     }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListInMenu() {
    ModalDrawerSheet( modifier = Modifier
        .offset(y= TopAppBarDefaults.TopAppBarExpandedHeight +
                WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        drawerContainerColor = MaterialTheme.colorScheme.onPrimary,
        windowInsets = WindowInsets(0)
    ) {
        MenuList.forEachIndexed { index, itemText ->
            ClickableButton(
                title = itemText,
                modifier = Modifier.padding(30.dp)
            ) {
                //needs UIlogik for About me and History
            }
        }

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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AppPreview() {
    DerivativecalcTheme(dynamicColor = false) {
        MainScreenWithDrawer()

    }
}