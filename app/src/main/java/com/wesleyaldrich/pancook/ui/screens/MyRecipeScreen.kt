package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.components.ReusableCard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen

@Composable
fun MyRecipeScreen(navController: NavController) {
    val recipes = remember { mutableStateListOf<Recipe>() }

    LaunchedEffect(Unit) {
        recipes.addAll(
            listOf(
                Recipe(
                    id = 4,
                    title = "Delicious Salad",
                    description = "A refreshing mix of garden greens.",
                    image = R.drawable.salad,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Fresh Lettuce", "Vegetables", 1.0f, "pcs"),
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
                        "Wash and dry the lettuce. Tear into bite-sized pieces and place in a large salad bowl.",
                        "Halve the cherry tomatoes. Dice the cucumber and finely slice the red onion.",
                        "Add the tomatoes, cucumber, and red onion to the salad bowl with the lettuce.",
                        "Crumble the feta cheese over the vegetables.",
                        "Boil 2 large eggs for 8-10 minutes for a hard-boiled consistency. Let them cool, peel, and quarter them.", // NEW STEP
                        "In a small bowl, whisk together the olive oil, lemon juice, salt, and black pepper to make the dressing.",
                        "Pour the dressing over the salad. Toss gently to combine all ingredients evenly.",
                        "Add the quartered boiled eggs to the salad.", // Updated to include eggs
                        "Serve immediately as a refreshing side or light meal.",
                        "Enjoy your delicious salad!"
                    ),
                    servings = 2,
                    duration = "15 min",
                    upvoteCount = 1234,
                    recipeMaker = "by Chef Ana"
                ),
                Recipe(
                    id = 5,
                    image = R.drawable.salad,
                    title = "Spicy Noodles",
                    description = "Quick and flavorful, with a kick.",
                    steps = listOf(
                        "Boil noodles according to package instructions. Drain and set aside.",
                        "In a wok or large pan, heat a tablespoon of oil over medium-high heat.",
                        "Add minced garlic and ginger, stir-fry until fragrant (about 30 seconds).",
                        "Add sliced chili peppers (adjust to taste) and your choice of vegetables (e.g., bell peppers, carrots, snap peas). Stir-fry for 2-3 minutes until tender-crisp.",
                        "Push vegetables to one side, add a protein like chicken or tofu if desired, cook until browned.",
                        "Pour in soy sauce, oyster sauce (optional), and a dash of sesame oil. Stir to combine with vegetables and protein.",
                        "Add the cooked noodles to the wok. Toss everything together until the noodles are well coated with the sauce.",
                        "Garnish with chopped green onions or cilantro and serve hot."
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
                    recipeMaker = "by Cook Ben"
                ),
                Recipe(
                    id = 6,
                    image = R.drawable.salad,
                    title = "Chicken Stir-fry",
                    description = "Healthy and quick weeknight dinner.",
                    steps = listOf(
                        "Slice chicken breast into thin strips. Marinate with soy sauce and cornstarch for 10 minutes.",
                        "Chop desired vegetables (broccoli, bell peppers, snap peas, carrots) into bite-sized pieces.",
                        "Heat oil in a large skillet or wok over high heat. Add chicken and stir-fry until browned and cooked through. Remove chicken and set aside.",
                        "Add chopped vegetables to the same skillet. Stir-fry for 3-5 minutes until tender-crisp.",
                        "Return chicken to the skillet. Pour in stir-fry sauce (mix soy sauce, ginger, garlic, honey, and sesame oil).",
                        "Toss everything together until coated and heated through. Serve immediately with rice."
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
                    recipeMaker = "by Foodie Clara"
                ),
                Recipe(
                    id = 7,
                    image = R.drawable.salad,
                    title = "Vegetable Curry",
                    description = "Aromatic and hearty vegetarian dish.",
                    steps = listOf(
                        "Heat oil in a large pot or Dutch oven over medium heat. Add chopped onion and cook until softened.",
                        "Stir in garlic, ginger, and curry powder. Cook for 1 minute until fragrant.",
                        "Add chopped vegetables (potatoes, carrots, bell peppers) and cook for 5 minutes.",
                        "Pour in vegetable broth and coconut milk. Bring to a simmer.",
                        "Cover and cook for 15-20 minutes, or until vegetables are tender.",
                        "Stir in spinach and cook until wilted. Season with salt and pepper to taste.",
                        "Serve hot with rice or naan bread."
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
                    recipeMaker = "by Gourmet David"
                ),
                Recipe(
                    id = 8,
                    image = R.drawable.salad,
                    title = "Creamy Pasta",
                    description = "Rich and comforting, a family favorite.",
                    steps = listOf(
                        "Cook pasta according to package directions. Reserve 1 cup pasta water before draining.",
                        "In a large skillet, melt butter over medium heat. Add minced garlic and cook until fragrant (1 minute).",
                        "Stir in heavy cream and Parmesan cheese. Cook, stirring, until cheese is melted and sauce thickens slightly.",
                        "Add the cooked pasta to the skillet. Toss to coat with the sauce.",
                        "If sauce is too thick, add reserved pasta water, a little at a time, until desired consistency is reached.",
                        "Season with salt and black pepper. Garnish with fresh parsley or more Parmesan."
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
                    recipeMaker = "by Baker Emily"
                ),
                Recipe(
                    id = 9,
                    image = R.drawable.salad,
                    title = "Grilled Fish",
                    description = "Light and healthy, perfect for summer.",
                    steps = listOf(
                        "Pat fish fillets dry with paper towels. Season both sides with salt, pepper, and lemon zest.",
                        "Preheat grill to medium-high heat. Lightly oil the grill grates.",
                        "Grill fish for 3-5 minutes per side, depending on thickness, until opaque and flakes easily with a fork.",
                        "Squeeze fresh lemon juice over the grilled fish before serving.",
                        "Serve with a side of steamed vegetables or a fresh salad."
                    ),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Fish Fillets", "Seafood", 2.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Lemon", "Fruit", 1.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Olive Oil", "Condiments", 1.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp")
                    ),
                    servings = 2,
                    duration = "35 min",
                    upvoteCount = 789,
                    recipeMaker = "by Mark's Kitchen"
                ),
                Recipe(
                    id = 11,
                    image = R.drawable.salad,
                    title = "Beef Stew",
                    description = "Hearty and comforting, ideal for cold days.",
                    steps = listOf(
                        "Pat beef chunks dry. Season with salt and pepper.",
                        "Heat oil in a large Dutch oven over medium-high heat. Brown beef in batches, then set aside.",
                        "Add chopped onion, carrots, and celery to the pot. Cook until softened (5-7 minutes).",
                        "Stir in minced garlic and cook for 1 minute until fragrant.",
                        "Return beef to the pot. Add beef broth, diced tomatoes, herbs (thyme, bay leaf), and potatoes.",
                        "Bring to a simmer, then reduce heat to low, cover, and cook for 1.5-2 hours, or until beef is very tender.",
                        "Thicken with a cornstarch slurry if desired. Serve hot with crusty bread."
                    ),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Beef Chuck", "Meat", 500.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Carrots", "Vegetables", 2.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Celery", "Vegetables", 2.0f, "stalks"),
                        Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0f, "cloves"),
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 300.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Beef Broth", "Liquids", 750.0f, "ml"),
                        Ingredient(R.drawable.ingredient_tomato, "Diced Tomatoes", "Canned Goods", 400.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Thyme", "Herbs", 1.0f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Bay Leaf", "Herbs", 1.0f, "pcs")
                    ),
                    servings = 4,
                    duration = "60 min",
                    upvoteCount = 101,
                    recipeMaker = "by Sophia's Recipes"
                ),
                Recipe(
                    id = 12,
                    image = R.drawable.salad,
                    title = "Tomato Soup",
                    description = "Classic comfort, warm and delicious.",
                    steps = listOf(
                        "Melt butter in a pot over medium heat. Add chopped onion and cook until softened (5 minutes).",
                        "Stir in minced garlic and cook for 1 minute until fragrant.",
                        "Add crushed tomatoes, vegetable broth, and basil. Bring to a simmer.",
                        "Reduce heat, cover, and cook for 15-20 minutes to allow flavors to meld.",
                        "Carefully blend the soup with an immersion blender until smooth, or transfer to a regular blender.",
                        "Stir in a splash of cream or milk (optional) for richness. Season with salt and pepper to taste.",
                        "Serve hot with croutons or grilled cheese."
                    ),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0f, "cloves"),
                        Ingredient(R.drawable.ingredient_tomato, "Crushed Tomatoes", "Canned Goods", 800.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Vegetable Broth", "Liquids", 500.0f, "ml"),
                        Ingredient(R.drawable.ingredient_tomato, "Fresh Basil", "Herbs", 0.25f, "cup"),
                        Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 1.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Cream/Milk", "Dairy", 50.0f, "ml")
                    ),
                    servings = 2,
                    duration = "10 min",
                    upvoteCount = 202,
                    recipeMaker = "by Leo's Dishes"
                ),

                Recipe(
                    id = 1,
                    title = "Spaghetti Bolognese",
                    description = "A classic Italian pasta dish with rich meat sauce.",
                    image = R.drawable.hash_brown,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Spaghetti", "Pasta", 200.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Ground Beef", "Meat", 150.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Tomato Sauce", "Condiments", 100.0f, "ml"),
                        Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0f, "piece"),
                        Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0f, "cloves")
                    ),
                    steps = listOf(
                        "Cook spaghetti according to package directions. Drain and set aside.",
                        "In a large skillet or pot, brown ground beef over medium heat. Drain excess fat.",
                        "Add chopped onion and minced garlic to the beef. Cook until onion is softened and transparent.",
                        "Stir in tomato sauce and bring to a simmer. Reduce heat and let it simmer for at least 15 minutes to meld flavors (longer for richer taste).",
                        "Season the sauce with salt, pepper, and any other desired herbs (e.g., oregano, basil).",
                        "Serve the bolognese sauce over the cooked spaghetti. Garnish with Parmesan cheese if desired."
                    ),
                    servings = 2,
                    duration = "30 min",
                    upvoteCount = 124,
                    recipeMaker = "by Mia's Meals"
                ),
                Recipe(
                    id = 2,
                    title = "Fresh Garden Salad (Grocery)",
                    description = "A light and healthy salad perfect for any meal.",
                    image = R.drawable.salad,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Lettuce", "Vegetables", 1.0f, "head"),
                        Ingredient(R.drawable.ingredient_tomato, "Tomato", "Vegetables", 2.0f, "pieces"),
                        Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 1.0f, "piece"),
                        Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 2.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Feta cheese", "Dairy", 50.0f, "g")
                    ),
                    steps = listOf(
                        "Wash and pat dry all vegetables. Tear or chop lettuce into bite-sized pieces.",
                        "Slice tomatoes and cucumber into desired shapes.",
                        "Combine lettuce, tomatoes, and cucumber in a large salad bowl.",
                        "Crumble feta cheese over the vegetables.",
                        "Drizzle with olive oil. Add salt and pepper to taste.",
                        "Toss gently until all ingredients are well combined. Serve fresh."
                    ),
                    servings = 1,
                    duration = "15 min",
                    upvoteCount = 89,
                    recipeMaker = "by Jake's Plates"
                ),
                Recipe(
                    id = 3,
                    title = "Grilled Chicken",
                    description = "Simple, juicy grilled chicken breast.",
                    image = R.drawable.fudgy_brownies,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Meat", 200.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Salt", "Spices", 1.0f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Pepper", "Spices", 1.0f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Rosemary", "Herbs", 1.0f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 1.0f, "tbsp")
                    ),
                    steps = listOf(
                        "Pat chicken breasts dry with paper towels. Season generously with salt, pepper, and rosemary on both sides.",
                        "Heat olive oil in a grill pan or outdoor grill over medium-high heat.",
                        "Place chicken breasts on the hot grill. Cook for about 6-8 minutes per side, or until internal temperature reaches 165°F (74°C) and juices run clear.",
                        "Remove from grill and let rest for 5 minutes before slicing or serving. This helps keep the chicken juicy."
                    ),
                    servings = 2,
                    duration = "25 min",
                    upvoteCount = 176,
                    recipeMaker = "by Mia's Meals"
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
                        "Peel and grate potatoes. Rinse grated potatoes thoroughly under cold water until water runs clear.",
                        "Squeeze out as much excess water as possible from the grated potatoes using a clean kitchen towel or paper towels. This is crucial for crispiness!",
                        "Season the dried grated potatoes with salt and pepper.",
                        "Heat butter in a large non-stick skillet over medium heat until melted and slightly browned.",
                        "Press the grated potatoes into an even layer in the skillet. Cook for 5-7 minutes per side, pressing occasionally with a spatula, until golden brown and crispy.",
                        "Flip carefully and cook the other side until also golden and crispy.",
                        "Serve hot immediately, optionally with ketchup or a fried egg."
                    ),
                    servings = 2,
                    duration = "20 min",
                    upvoteCount = 500,
                    recipeMaker = "by Chef Emily"
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
                        "Preheat oven to 175°C (350°F). Grease and flour an 8x8 inch baking pan.",
                        "In a medium saucepan, melt butter over low heat. Remove from heat and stir in sugar until combined.",
                        "Whisk in cocoa powder. Add eggs one at a time, mixing well after each addition. Stir in vanilla extract.",
                        "Gradually add flour and salt, mixing until just combined. Do not overmix.",
                        "Pour batter into the prepared baking pan and spread evenly.",
                        "Bake for 20-25 minutes, or until a toothpick inserted into the center comes out with moist crumbs (not wet batter).",
                        "Let cool completely in the pan on a wire rack before cutting into squares."
                    ),
                    servings = 2,
                    duration = "1 hour",
                    upvoteCount = 750,
                    recipeMaker = "by Baker John"
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
                        "In a shallow dish, whisk together eggs, milk, sugar, vanilla extract, and cinnamon until well combined.",
                        "Heat butter in a large non-stick skillet or griddle over medium heat.",
                        "Dip each slice of bread into the egg mixture, ensuring both sides are fully coated but not soggy.",
                        "Place bread slices on the hot skillet. Cook for 2-4 minutes per side, or until golden brown and cooked through.",
                        "Serve hot with your favorite toppings like syrup, fresh fruit, or powdered sugar."
                    ),
                    servings = 1,
                    duration = "30 min",
                    upvoteCount = 300,
                    recipeMaker = "by Chef Jane"
                )
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->
                ReusableCard(
                    imagePainter = painterResource(id = recipe.image),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    upvoteCount = recipe.upvoteCount,
                    modifier = Modifier.clickable {
                        if (recipe.title == "Delicious Salad") {
                            navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                        }
                    }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 2100
)
@Composable
fun MyRecipeScreenPreview() {
    PancookTheme {
        MyRecipeScreen(navController = rememberNavController())
    }
}