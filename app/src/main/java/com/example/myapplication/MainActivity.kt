package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.persistableBundleOf
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
 import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            appNavigation()
        }
    }
}

@Composable
fun LayoutMain(navController: NavController?) {
    var listaPersonagem by remember { mutableStateOf(listOf<Personagem>()) }
    var nome by remember { mutableStateOf(value="") }
    var especie by remember { mutableStateOf(value="") }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {

        Text(text = "Cadastre seu Personagem: ", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = nome, onValueChange = {
            nome = it
        }, label = {Text(text = "Nome: ")})

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = especie, onValueChange = {
            especie = it
        }, label = {Text(text = "Especie: ")})

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            listaPersonagem += Personagem(nome, especie)
        }) {
            Text(text = "Criar Personagem")
        }

        LazyColumn {
            items(listaPersonagem){
                personagem ->
                    Text(text = "${personagem.nome}",
                        modifier = Modifier.clickable {
                            // Serializando o objeto contato em JSON
                            val personagemJson = Gson().toJson(personagem)
                            // Navegando para Tela2, passando o JSON como parâmetro
                            navController?.navigate("details/$personagemJson")
                        })

            }
        }
    }
}


@Composable
fun Detalhes(navController: NavController?, personagem: Personagem) {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "DADOS DO PERSONAGEM ${personagem.nome}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Nome: ${personagem.nome}")
        Text(text = "Especie: ${personagem.especie}")

        Button(onClick = {
            navController?.popBackStack() // Volta para Tela1
        }) {
            Text("Voltar")
        }
    }

}

@Composable
fun appNavigation() {
    val navController = rememberNavController()

    NavHost(navController=navController, startDestination="layout") {
        composable("layout") {LayoutMain(navController)}
        composable("details/{personagemJson}") {
            backStackEntry ->
            val personagemJson = backStackEntry.arguments?.getString("personagemJson")
            val personagem = Gson().fromJson(personagemJson, Personagem::class.java)
            Detalhes(navController, personagem)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LayoutMain(navController=null)
}