package com.venu.moviesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.venu.moviesearchapp.components.SearchBar
import com.venu.moviesearchapp.ui.theme.MovieSearchAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.venu.moviesearchapp.components.MovieTile
import kotlinx.coroutines.launch

//https://www.omdbapi.com/?s=Jailer&apikey=dada2190&type=movie

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieSearchAppTheme {
                Greeting()
            }
        }
    }
}


@Composable
fun Greeting() {
    val mainViewModel: MovieViewModel = viewModel()
    val movieDetails by mainViewModel.movieDetails.observeAsState()
    val movieDetailsError by mainViewModel.movieDetailsError.observeAsState()
    val scope = rememberCoroutineScope()

    Surface {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                text = mainViewModel.movieSearchName,
                onTextChange = { mainViewModel.movieSearchName = it },
                onSearch = {
                    if (mainViewModel.validate()) {
                        scope.launch {
                            mainViewModel.getMovieDetails()
                        }
                    }
                },
                mainViewModel.movieSearchNameError,
                mainViewModel.isMovieSearchNameError
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (mainViewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                movieDetails?.let {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // Specify the number of columns
                        modifier = Modifier.padding(6.dp) // Add any desired padding or modifiers
                    ) {
                        items(movieDetails!!.size) {
                            MovieTile(movieDetails!![it])
                        }
                    }
                } ?: Text(
                    text = "No Movie Data Available",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp, modifier = Modifier.fillMaxWidth()
                )
            }




            Spacer(modifier = Modifier.height(16.dp))
            movieDetailsError?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp, modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieSearchAppTheme {
        Greeting()
    }
}