package com.example.c36b.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.c36b.repository.ProductRepository
import com.example.c36b.repository.ProductRepositoryImpl
import com.example.c36b.viewmodel.ProductViewModel

class DashboardActitivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@Composable
fun DashboardBody() {
    val context = LocalContext.current
    val activity = context as? Activity

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    val products = viewModel.allProducts.
                    observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.getAllProduct()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddProductActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },

        ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(products.value.size) { index ->
                val eachProduct = products.value[index]
                Card (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column {
                        Text("${eachProduct?.productName}")
                        Text("${eachProduct?.productDesc}")
                        Text("${eachProduct?.productDesc}")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun previewDashboard() {
    DashboardBody()
}