package com.krezinger.derivativecalc

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.krezinger.derivativecalc.ui.theme.DerivativecalcTheme

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Maincontainer(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String = "" ){
    TopAppBar(title = { Text(title) },
              colors = TopAppBarDefaults.topAppBarColors(
                  containerColor = MaterialTheme.colorScheme.onPrimary
              ),
              navigationIcon = {
                IconButton(onClick = {Unit}) {
                    Icon(Icons.Default.Menu, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimaryContainer)}})
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun GreetingPreview() {
    DerivativecalcTheme(dynamicColor = false) {
        Scaffold(
            topBar = { TopBar() },
            content = { Maincontainer() }
        )
    }
}