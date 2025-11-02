package com.example.recipes.presentation.screen

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import coil.compose.AsyncImage
import com.example.recipes.data.remote.model.Meal

object recipeCard {

    @Composable
    fun RecipeCard(
        meal: Meal,
        onMealClick: (Meal) -> Unit,
        onToggleFavorite: (String) -> Unit
    ){
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .clickable{onMealClick(meal)},
            elevation = CardDefaults.cardElevation(4.dp)
        )
        {
            Column {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    {
                        Text(
                            text = meal.strMeal,
                            style = MaterialTheme.typography.h6,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${meal.strCategory} * ${meal.strArea}",
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = {onToggleFavorite(meal.idMeal)},
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                           imageVector = if (meal.isFavorite){
                               Icons.Filled.Favorite
                           }
                            else{
                                Icons.Outlined.Favorite
                           },
                            contentDescription = "Избранное",
                            tint = if(meal.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}