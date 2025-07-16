package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Category
import com.wesleyaldrich.pancook.model.Comment
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Instruction
import com.wesleyaldrich.pancook.model.NutritionFact
import com.wesleyaldrich.pancook.model.Recent
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.components.RecipeReusableCard
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import kotlinx.coroutines.launch

val allCategories = listOf(
    Category("Rice", R.drawable.rice),
    Category("Pasta", R.drawable.pasta),
    Category("Potato", R.drawable.potato),
    Category("Chicken", R.drawable.chicken),
    Category("Egg", R.drawable.egg),
    Category("Cheese", R.drawable.cheese),
    Category("Bread", R.drawable.bread),
    Category("Beef", R.drawable.beef)
)

val recentViewedItems = listOf(
    Recent("Ayam Geprek", R.drawable.ayam_geprek,6), // ID disesuaikan dengan resep
    Recent("Nasi Bakar", R.drawable.nasi_bakar,5),
    Recent("Ketoprak", R.drawable.ketoprak,4),
    Recent("Nasi Goreng", R.drawable.nasi_goreng,7),
    Recent("Rawon", R.drawable.rawon,8)
)

val recipes = listOf(
    Recipe(
        id = 4,
        title = "Delicious Salad",
        description = "A refreshing mix of garden greens.",
        image = R.drawable.ketoprak,
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Fresh Lettuce", "Vegetables", 1.0f, "head"),
            Ingredient(R.drawable.ingredient_tomato, "Cherry Tomatoes", "Vegetables", 150.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 0.5f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Red Onion", "Vegetables", 0.25f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Feta Cheese", "Dairy", 50.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Olive Oil", "Condiments", 2.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Lemon Juice", "Condiments", 1.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Egg", "Seasoning", 2.05f, "pcs")
        ),
        steps = listOf(
            Instruction(1, "Wash and dry the lettuce. Tear into bite-sized pieces and place in a large salad bowl."),
            Instruction(2, "Halve the cherry tomatoes. Dice the cucumber and finely slice the red onion."),
            Instruction(3, "Add the tomatoes, cucumber, and red onion to the salad bowl with the lettuce."),
            Instruction(4, "Crumble the feta cheese over the vegetables."),
            Instruction(5, "Boil 2 large eggs for 8-10 minutes for a hard-boiled consistency. Let them cool, peel, and quarter them.", timerSeconds = 480),
            Instruction(6, "In a small bowl, whisk together the olive oil, lemon juice, salt, and black pepper to make the dressing."),
            Instruction(7, "Pour the dressing over the salad. Toss gently to combine all ingredients evenly."),
            Instruction(8, "Add the quartered boiled eggs to the salad."),
            Instruction(9, "Serve immediately as a refreshing side or light meal."),
            Instruction(10, "Enjoy your delicious salad!")
        ),
        servings = 2,
        duration = "15 min",
        upvoteCount = 1234,
        recipeMaker = "by Chef Ana",
        nutritionFacts = listOf( // Dummy Nutrition Facts
            NutritionFact("Calories", "250 kcal"),
            NutritionFact("Protein", "10g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "20g")
        ),
        comments = listOf( // Dummy Comments
            Comment("John Doe", "This is a delicious salad, easy to make!"),
            Comment("Jane Smith", "Loved the addition of feta cheese, great recipe!", isUpvote = true)
        )
    ),
    Recipe(
        id = 5,
        image = R.drawable.nasi_bakar,
        title = "Spicy Noodles",
        description = "Quick and flavorful, with a kick.",
        steps = listOf(
            Instruction(1, "Boil noodles according to package instructions. Drain and set aside."),
            Instruction(2, "In a wok or large pan, heat a tablespoon of oil over medium-high heat."),
            Instruction(3, "Add minced garlic and ginger, stir-fry until fragrant (about 30 seconds)."),
            Instruction(4, "Add sliced chili peppers (adjust to taste) and your choice of vegetables (e.g., bell peppers, carrots, snap peas). Stir-fry for 2-3 minutes until tender-crisp."),
            Instruction(5, "Push vegetables to one side, add a protein like chicken or tofu if desired, cook until browned."),
            Instruction(6, "Pour in soy sauce, oyster sauce (optional), and a dash of sesame oil. Stir to combine with vegetables and protein."),
            Instruction(7, "Add the cooked noodles to the wok. Toss everything together until the noodles are well coated with the sauce."),
            Instruction(8, "Garnish with chopped green onions or cilantro and serve hot.")
        ),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Instant Noodles", "Grains", 100.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0f, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0f, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Chili Peppers", "Vegetables", 1.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Mixed Vegetables", "Vegetables", 150.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 2.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5f, "tsp")
        ),
        servings = 2,
        duration = "20 min",
        upvoteCount = 567,
        recipeMaker = "by Cook Ben",
        nutritionFacts = listOf(
            NutritionFact("Calories", "350 kcal"),
            NutritionFact("Protein", "12g"),
            NutritionFact("Fat", "10g"),
            NutritionFact("Carbs", "50g")
        ),
        comments = listOf(
            Comment("Mark T.", "Super spicy, just how I like it!"),
            Comment("Chef Emily", "Quick dinner solution, love it!", isUpvote = true)
        )
    ),
    Recipe(
        id = 6,
        image = R.drawable.ayam_geprek,
        title = "Chicken Stir-fry",
        description = "Healthy and quick weeknight dinner.",
        steps = listOf(
            Instruction(1, "Slice chicken breast into thin strips. Marinate with soy sauce and cornstarch for 10 minutes."),
            Instruction(2, "Chop desired vegetables (broccoli, bell peppers, snap peas, carrots) into bite-sized pieces."),
            Instruction(3, "Heat oil in a large skillet or wok over high heat. Add chicken and stir-fry until browned and cooked through. Remove chicken and set aside."),
            Instruction(4, "Add chopped vegetables to the same skillet. Stir-fry for 3-5 minutes until tender-crisp."),
            Instruction(5, "Return chicken to the skillet. Pour in stir-fry sauce (mix soy sauce, ginger, garlic, honey, and sesame oil)."),
            Instruction(6, "Toss everything together until coated and heated through. Serve immediately with rice.")
        ),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Meat", 300.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Broccoli", "Vegetables", 150.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 3.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0f, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0f, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Honey", "Sweeteners", 1.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5f, "tsp")
        ),
        servings = 2,
        duration = "30 min",
        upvoteCount = 890,
        recipeMaker = "by Foodie Clara",
        nutritionFacts = listOf(
            NutritionFact("Calories", "400 kcal"),
            NutritionFact("Protein", "30g"),
            NutritionFact("Fat", "18g"),
            NutritionFact("Carbs", "25g")
        ),
        comments = listOf(
            Comment("David S.", "My go-to weeknight recipe!"),
            Comment("Sara L.", "Healthy and delicious. Perfect combo.", isUpvote = true)
        )
    ),
    Recipe(
        id = 7,
        image = R.drawable.nasi_goreng,
        title = "Vegetable Curry",
        description = "Aromatic and hearty vegetarian dish.",
        steps = listOf(
            Instruction(1, "Heat oil in a large pot or Dutch oven over medium heat. Add chopped onion and cook until softened."),
            Instruction(2, "Stir in garlic, ginger, and curry powder. Cook for 1 minute until fragrant."),
            Instruction(3, "Add chopped vegetables (potatoes, carrots, bell peppers) and cook for 5 minutes."),
            Instruction(4, "Pour in vegetable broth and coconut milk. Bring to a simmer."),
            Instruction(5, "Cover and cook for 15-20 minutes, or until vegetables are tender."),
            Instruction(6, "Stir in spinach and cook until wilted. Season with salt and pepper to taste."),
            Instruction(7, "Serve hot with rice or naan bread.")
        ),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0f, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0f, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Curry Powder", "Spices", 2.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0f, "medium"),
            Ingredient(R.drawable.ingredient_tomato, "Carrots", "Vegetables", 2.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Vegetable Broth", "Liquids", 400.0f, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Coconut Milk", "Dairy", 400.0f, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Spinach", "Vegetables", 100.0f, "g")
        ),
        servings = 2,
        duration = "45 min",
        upvoteCount = 123,
        recipeMaker = "by Gourmet David",
        nutritionFacts = listOf(
            NutritionFact("Calories", "300 kcal"),
            NutritionFact("Protein", "8g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "35g")
        ),
        comments = listOf(
            Comment("Priya R.", "So flavorful and comforting!"),
            Comment("Vegan Chef", "A staple in my kitchen, absolutely love it.", isUpvote = true)
        )
    ),
    Recipe(
        id = 8,
        image = R.drawable.salad,
        title = "Creamy Pasta",
        description = "Rich and comforting, a family favorite.",
        steps = listOf(
            Instruction(1, "Cook pasta according to package directions. Reserve 1 cup pasta water before draining."),
            Instruction(2, "In a large skillet, melt butter over medium heat. Add minced garlic and cook until fragrant (1 minute)."),
            Instruction(3, "Stir in heavy cream and Parmesan cheese. Cook, stirring, until cheese is melted and sauce thickens slightly."),
            Instruction(4, "Add the cooked pasta to the skillet. Toss to coat with the sauce."),
            Instruction(5, "If sauce is too thick, add reserved pasta water, a little at a time, until desired consistency is reached."),
            Instruction(6, "Season with salt and black pepper. Garnish with fresh parsley or more Parmesan.")
        ),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Pasta", "Grains", 200.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0f, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Heavy Cream", "Dairy", 200.0f, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Parmesan Cheese", "Dairy", 50.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp")
        ),
        servings = 2,
        duration = "25 min",
        upvoteCount = 456,
        recipeMaker = "by Baker Emily",
        nutritionFacts = listOf(
            NutritionFact("Calories", "550 kcal"),
            NutritionFact("Protein", "15g"),
            NutritionFact("Fat", "30g"),
            NutritionFact("Carbs", "50g")
        ),
        comments = listOf(
            Comment("Tom H.", "My kids devour this every time!"),
            Comment("RecipeFan", "Creamy perfection!", isUpvote = true)
        )
    ),
    Recipe(
        id = 103,
        title = "French Toast",
        description = "A sweet and savory breakfast classic.",
        image = R.drawable.hash_brown,
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Bread", "Bakery", 4.0f, "slices"),
            Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Milk", "Dairy", 100.0f, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 1.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 0.5f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Cinnamon", "Spices", 0.25f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 1.0f, "tbsp")
        ),
        steps = listOf(
            Instruction(1, "In a shallow dish, whisk together eggs, milk, sugar, vanilla extract, and cinnamon until well combined."),
            Instruction(2, "Heat butter in a large non-stick skillet or griddle over medium heat."),
            Instruction(3, "Dip each slice of bread into the egg mixture, ensuring both sides are fully coated but not soggy."),
            Instruction(4, "Place bread slices on the hot skillet. Cook for 2-4 minutes per side, or until golden brown and cooked through."),
            Instruction(5, "Serve hot with your favorite toppings like syrup, fresh fruit, or powdered sugar.")
        ),
        servings = 1,
        duration = "30 min",
        upvoteCount = 300,
        recipeMaker = "by Chef Jane",
        nutritionFacts = listOf(
            NutritionFact("Calories", "350 kcal"),
            NutritionFact("Protein", "10g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "40g")
        ),
        comments = listOf(
            Comment("Brunch Fan", "Best French Toast ever, simple and delicious!"),
            Comment("Sweet Start", "A classic done right.", isUpvote = true)
        )
    ),
    Recipe(
        id = 101,
        title = "Hash Browns",
        description = "Classic crispy potato breakfast.",
        image = R.drawable.hash_brown,
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0f, "large"),
            Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0f, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp")
        ),
        steps = listOf(
            Instruction(1, "Peel and grate potatoes. Rinse grated potatoes thoroughly under cold water until water runs clear."),
            Instruction(2, "Squeeze out as much excess water as possible from the grated potatoes using a clean kitchen towel or paper towels. This is crucial for crispiness!"),
            Instruction(3, "Season the dried grated potatoes with salt and pepper."),
            Instruction(4, "Heat butter in a large non-stick skillet over medium heat until melted and slightly browned."),
            Instruction(5, "Press the grated potatoes into an even layer in the skillet. Cook for 5-7 minutes per side, pressing occasionally with a spatula, until golden brown and crispy."),
            Instruction(6, "Flip carefully and cook the other side until also golden and crispy."),
            Instruction(7, "Serve hot immediately, optionally with ketchup or a fried egg.")
        ),
        servings = 2,
        duration = "20 min",
        upvoteCount = 500,
        recipeMaker = "by Chef Emily",
        nutritionFacts = listOf(
            NutritionFact("Calories", "300 kcal"),
            NutritionFact("Protein", "5g"),
            NutritionFact("Fat", "18g"),
            NutritionFact("Carbs", "30g")
        ),
        comments = listOf(
            Comment("Breakfast King", "Crispy and delicious, a perfect breakfast side!"),
            Comment("Morning Person", "Can't start my day without these.", isUpvote = true)
        )
    ),
    Recipe(
        id = 102,
        title = "Fudgy Brownies",
        description = "Rich, decadent, and perfectly fudgy.",
        image = R.drawable.fudgy_brownies,
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Unsalted Butter", "Dairy", 100.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 200.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Unsweetened Cocoa Powder", "Baking", 50.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0f, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "All-Purpose Flour", "Baking", 60.0f, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 1.0f, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.25f, "tsp")
        ),
        steps = listOf(
            Instruction(1, "Preheat oven to 175°C (350°F). Grease and flour an 8x8 inch baking pan."),
            Instruction(2, "In a medium saucepan, melt butter over low heat. Remove from heat and stir in sugar until combined."),
            Instruction(3, "Whisk in cocoa powder. Add eggs one at a time, mixing well after each addition. Stir in vanilla extract."),
            Instruction(4, "Gradually add flour and salt, mixing until just combined. Do not overmix."),
            Instruction(5, "Pour batter into the prepared baking pan and spread evenly."),
            Instruction(6, "Bake for 20-25 minutes, or until a toothpick inserted into the center comes out with moist crumbs (not wet batter)."),
            Instruction(7, "Let cool completely in the pan on a wire rack before cutting into squares.")
        ),
        servings = 2,
        duration = "1 hour",
        upvoteCount = 750,
        recipeMaker = "by Baker John",
        nutritionFacts = listOf(
            NutritionFact("Calories", "450 kcal"),
            NutritionFact("Protein", "5g"),
            NutritionFact("Fat", "25g"),
            NutritionFact("Carbs", "60g")
        ),
        comments = listOf(
            Comment("Sweet Tooth", "The fudgiest brownies ever! A must-try."),
            Comment("Dessert Queen", "Perfect texture and rich chocolate flavor.", isUpvote = true)
        )
    )
)

@Composable
fun HomeScreen(onRecipeClick: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val displayedCategories = allCategories.take(7)
    val scrollState = rememberScrollState()

    val ayamGeprek = recipes.find { it.id == 6 }
    val nasiBakar = recipes.find { it.id == 5 }
    val rawon = recipes.find { it.id == 8 }
    val ketoprak = recipes.find { it.id == 4 }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(15.dp)
    ) {
        //SEARCH BAR HOME
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),

            shape = CircleShape,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // DAILY INSPIRATION
        Text(
            text = "Daily Cravings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins
        )

        SwipeableCardStack(
            recipes = recipes.take(6),
            modifier = Modifier.fillMaxSize(),
            onRecipeClick = onRecipeClick
        )

        // HEADER TITLE "MOST POPULAR"
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Most Popular",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins
        )


        // CONTENT MOST POPULAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp)

        ) {
            // Kiri
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (ayamGeprek != null) {
                    Box(modifier = Modifier.weight(0.58f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = R.drawable.ayam_geprek),
                            title = "Ayam Geprek",
                            description = "By Chef Pok",
                            duration = "30 min",
                            likeCount = 1500,
                            imageHeight = 210.dp,
                            onClick = { onRecipeClick(ayamGeprek.id) }
                        )
                    }
                }

                if (nasiBakar != null) {
                    Box(modifier = Modifier.weight(0.5f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = R.drawable.nasi_bakar),
                            title = "Nasi Bakar Cumi",
                            description = "By Chef Jusa",
                            duration = "15 min",
                            likeCount = 500,
                            imageHeight = 150.dp,
                            onClick = { onRecipeClick(nasiBakar.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Kanan
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (rawon != null) {
                    Box(modifier = Modifier.weight(0.41f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = R.drawable.rawon),
                            title = "Rawon khas Jawa Timur",
                            description = "By Juseyo",
                            duration = "15 min",
                            likeCount = 500,
                            imageHeight = 150.dp,
                            onClick = { onRecipeClick(rawon.id) }
                        )
                    }
                }

                if (ketoprak != null) {
                    Box(modifier = Modifier.weight(0.55f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = R.drawable.ketoprak),
                            title = "Ketoprak Bandung",
                            description = "By kai",
                            duration = "30 min",
                            likeCount = 1500,
                            imageHeight = 210.dp,
                            onClick = { onRecipeClick(ketoprak.id) }
                        )
                    }
                }
            }
        }

        //HEADER TITLE CATEGORY
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "Category",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )

            Text(
                text = "Show All",
                textDecoration = TextDecoration.Underline,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = poppins,
                color = Color.Gray,
                modifier = Modifier.clickable { showDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(displayedCategories) { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(category.name, fontSize = 12.sp, fontFamily = nunito, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // DETAIL CATEGORY DIALOG
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFF051A39),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {

                        // Tombol close
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "✕",
                                color = Color.White,
                                fontSize = 20.sp,
                                modifier = Modifier.clickable {
                                    showDialog = false

                                }

                            )

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title
                        Text(
                            text = "Search by Ingredients",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Search Bar
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search category") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = CircleShape,
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // Subtitle
                        Text(
                            text = "Suggested For You",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid kategori
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(allCategories) { category ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(Color.LightGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = category.imageRes),
                                            contentDescription = null,
                                            modifier = Modifier.size(80.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = category.name,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // HEADER RECENTS VIEW
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Recently Viewed",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins
        )

        Spacer(modifier = Modifier.height(8.dp))


        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(recentViewedItems) { recent ->
                Box(modifier = Modifier.clickable { onRecipeClick(recent.recipeId) }) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 160.dp, height = 110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = recent.imageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(recent.name, fontSize = 12.sp, fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recommend For You",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(8.dp))
        RecipeGridSection(recipes, onRecipeClick = onRecipeClick)
    }
}

@Composable
fun RecipeGridSection(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        recipes.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                rowItems.forEach { recipe ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    ) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = recipe.image),
                            title = recipe.title,
                            description = recipe.recipeMaker,
                            duration = recipe.duration,
                            likeCount = recipe.upvoteCount,
                            onClick = { onRecipeClick(recipe.id) }
                        )
                    }
                }

                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun SwipeableCardStack(
    recipes: List<Recipe>,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit
) {
    val cardStack = remember { mutableStateListOf<Recipe>().apply { addAll(recipes) } }
    val swipedCards = remember { mutableStateListOf<Recipe>() }

    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    var restoring by remember { mutableStateOf(false) }
    val restoreOffsetX = remember { Animatable(0f) }

    Box(modifier = modifier.padding(start = 20.dp)) {

        if (restoring && swipedCards.isNotEmpty()) {
            val recipe = swipedCards.last()

            LaunchedEffect(recipe) {
                restoreOffsetX.snapTo(-1000f)
                restoreOffsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 400)
                )
                swipedCards.removeLast()
                cardStack.add(recipe)
                restoring = false
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .graphicsLayer {
                        scaleX = 1f
                        scaleY = 1f
                        shadowElevation = 0f
                    }
                    .offset { IntOffset(restoreOffsetX.value.toInt(), 0) }
                    .zIndex(cardStack.size + 1f)
            ) {
                RecipeReusableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    imagePainter = painterResource(id = recipe.image),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    likeCount = recipe.upvoteCount,
                    imageHeight = 230.dp,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }

        cardStack.forEachIndexed { index, recipe ->
            val isTop = index == cardStack.lastIndex
            val offsetX = remember { Animatable(0f) }

            val cardOffset = with(density) { (cardStack.size - 1 - index) * 16.dp.toPx() }
            val cardScale = 1f - (cardStack.size - 1 - index) * 0.02f

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .graphicsLayer {
                        translationX = if (isTop) offsetX.value else cardOffset
                        scaleX = cardScale
                        scaleY = cardScale
                        shadowElevation = 0f
                    }
                    .zIndex(index.toFloat())
                    .pointerInput(isTop) {
                        if (isTop) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    coroutineScope.launch {
                                        val newX = offsetX.value + dragAmount.x
                                        if (newX <= 0f) {
                                            offsetX.snapTo(newX)
                                        } else if (dragAmount.x > 0 && swipedCards.isNotEmpty() && !restoring) {
                                            restoring = true
                                        }
                                    }
                                },
                                onDragEnd = {
                                    coroutineScope.launch {
                                        if (offsetX.value < -300f && cardStack.size > 1) {
                                            offsetX.animateTo(-1000f, tween(300))
                                            swipedCards.add(cardStack.removeLast())
                                            offsetX.snapTo(0f) // Reset offset for the new top card
                                        } else {
                                            offsetX.animateTo(0f, tween(300))
                                        }
                                    }
                                }
                            )
                        }
                    }
                    .clickable(enabled = isTop) { onRecipeClick(recipe.id) }
            ) {
                RecipeReusableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    imagePainter = painterResource(id = recipe.image),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    likeCount = recipe.upvoteCount,
                    imageHeight = 230.dp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomepageScreenPreview() {
    PancookTheme {
        HomeScreen(onRecipeClick = {})
    }
}
