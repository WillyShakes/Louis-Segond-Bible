package org.redesnac.lsgbible.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.redesnac.lsgbible.presentation.book.BooksScreen
import org.redesnac.lsgbible.presentation.favorite_verse.list.FavoriteVersesScreen
import org.redesnac.lsgbible.presentation.theme.LSGBibleTheme
import org.redesnac.lsgbible.presentation.verse.VersesScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LSGBibleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.BooksScreen.route ) {
                        composable(route = Screen.BooksScreen.route){
                            BooksScreen(navController = navController)
                        }
                        composable(
                            route = Screen.VersesScreen.route + "?bookName={bookName}",
                            arguments = listOf(
                                navArgument(
                                    name = "bookName"
                                ){
                                    type = NavType.StringType
                                }
                            )
                        ){
                            VersesScreen()
                        }
                        composable(route = Screen.FavoriteVersesScreen.route){
                            FavoriteVersesScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LSGBibleTheme {
        Greeting("Android")
    }
}