// MyRecipeScreen.kt
package com.wesleyaldrich.pancook.ui.screens

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
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.model.Instruction
import com.wesleyaldrich.pancook.model.NutritionFact
import com.wesleyaldrich.pancook.model.Comment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import android.net.Uri // Important for video URIs


// --- START: GLOBAL RECIPE DATA ---
// Move allRecipes, bookmarkedRecipes, and upvotedRecipes back to top-level
// Note: For local video URIs (android.resource://), you cannot directly use `packageName` here.
// You would either hardcode the package name or use external video URLs.
// For the purpose of getting your app to compile and fix the immediate error,
// I'm keeping `videos = emptyList()` for the ones you provided previously,
// or if you had a local video example, you'd need a hardcoded packageName or external URL.

val allRecipes = mutableStateListOf<Recipe>().apply {
    addAll(
        listOf(
            Recipe(
                id = 4,
                title = "Delicious Salad",
                description = "A refreshing mix of garden greens.",
                images = listOf(R.drawable.salad, R.drawable.salad_2, R.drawable.salad_3, R.drawable.salad_4, R.drawable.salad_5),
                // --- FIX IS HERE ---
                videos = listOf(Uri.parse("android.resource://com.wesleyaldrich.pancook/${R.raw.salad_video}")),
                // -------------------
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Fresh Lettuce", "Vegetables", 1.0, "head"),
                    Ingredient(R.drawable.ingredient_tomato, "Cherry Tomatoes", "Vegetables", 150.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 0.5, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Red Onion", "Vegetables", 0.25, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Feta Cheese", "Dairy", 50.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive Oil", "Condiments", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Lemon Juice", "Condiments", 1.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Egg", "Egg", 2.05, "pcs")
                ),
                steps = listOf(
                    Instruction(1, "Wash and dry the lettuce. Tear into bite-sized pieces and place in a large salad bowl.",
                        images = listOf(R.drawable.salad_step_1_wash_lettuce)),
                    Instruction(2, "Halve the cherry tomatoes. Dice the cucumber and finely slice the red onion.",
                        images = listOf(R.drawable.salad_step_2_chop_veg)),
                    Instruction(3, "Add the tomatoes, cucumber, and red onion to the salad bowl with the lettuce.",
                        images = listOf(R.drawable.salad_step_3_add_to_bowl)),
                    Instruction(4, "Crumble the feta cheese over the vegetables.",
                        images = listOf(R.drawable.salad_step_4_feta)),
                    Instruction(5, "Boil 2 large eggs for 8-10 minutes for a hard-boiled consistency. Let them cool, peel, and quarter them.", timerSeconds = 480,
                        images = listOf(R.drawable.salad_step_5_eggs)),
                    Instruction(6, "In a small bowl, whisk together the olive oil, lemon juice, salt, and black pepper to make the dressing.",
                        images = listOf(R.drawable.salad_step_6_dressing)),
                    Instruction(7, "Pour the dressing over the salad. Toss gently to combine all ingredients evenly.",
                        images = listOf(R.drawable.salad_step_7_pour_dressing)),
                    Instruction(8, "Add the quartered boiled eggs to the salad.",
                        images = listOf(R.drawable.salad_step_8_eggs_added)),
                    Instruction(9, "Serve immediately as a refreshing side or light meal.",
                        images = listOf(R.drawable.salad))
                ),
                servings = 2,
                duration = "15 min",
                upvoteCount = 1234,
                recipeMaker = "by Chef Ana",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "250 kcal"),
                    NutritionFact("Protein", "10g"),
                    NutritionFact("Fat", "15g"),
                    NutritionFact("Carbs", "20g")
                ),
                comments = listOf(
                    Comment("John Doe", "This is a delicious salad, easy to make!"),
                    Comment("Jane Smith", "Loved the addition of feta cheese, great recipe!", isUpvote = true)
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 5,
                images = listOf(R.drawable.spicy_noodle, R.drawable.spicy_noodle_2, R.drawable.spicy_noodle_3, R.drawable.spicy_noodle_4),
                // For local video files in `res/raw`, you *must* hardcode the package name here
                // if `allRecipes` is a top-level property. Otherwise, you'd move `allRecipes`
                // inside a Composable to get `LocalContext.current.packageName`.
                // Example with hardcoded package name (replace 'com.wesleyaldrich.pancook' with your actual package)
                // If you don't have sample_video.mp4, keep emptyList() or use an external URL
                videos = emptyList(),
                title = "Spicy Noodles",
                description = "Quick and flavorful, with a kick.",
                steps = listOf(
                    Instruction(1, "Boil noodles according to package instructions. Drain and set aside.", timerSeconds = 180),
                    Instruction(2, "In a wok or large pan, heat a tablespoon of oil over medium-high heat."),
                    Instruction(3, "Add minced garlic and ginger, stir-fry until fragrant (about 30 seconds).", timerSeconds = 30),
                    Instruction(4, "Add sliced chili peppers (adjust to taste) and your choice of vegetables (e.g., bell peppers, carrots, snap peas). Stir-fry for 2-3 minutes until tender-crisp.", timerSeconds = 180),
                    Instruction(5, "Push vegetables to one side, add a protein like chicken or tofu if desired, cook until browned."),
                    Instruction(6, "Pour in soy sauce, oyster sauce (optional), and a dash of sesame oil. Stir to combine with vegetables and protein."),
                    Instruction(7, "Add the cooked noodles to the wok. Toss everything together until the noodles are well coated with the sauce."),
                    Instruction(8, "Garnish with chopped green onions or cilantro and serve hot.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Instant Noodles", "Grains", 100.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
                    Ingredient(R.drawable.ingredient_tomato, "Chili Peppers", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Mixed Vegetables", "Vegetables", 150.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Rice", "Rice", 1.0, "cup")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 6,
                images = listOf(R.drawable.chicken_stir_fry, R.drawable.chicken_stir_fry_2, R.drawable.chicken_stir_fry_3, R.drawable.chicken_stir_fry_4),
                videos = emptyList(),
                title = "Chicken Stir-fry",
                description = "Healthy and quick weeknight dinner.",
                steps = listOf(
                    Instruction(1, "Slice chicken breast into thin strips. Marinate with soy sauce and cornstarch for 10 minutes."),
                    Instruction(2, "Chop desired vegetables (broccoli, bell peppers, snap peas, carrots) into bite-sized pieces."),
                    Instruction(3, "Heat oil in a large skillet or wok over high heat. Add chicken and stir-fry until browned and cooked through. Remove chicken and set aside.", timerSeconds = 300),
                    Instruction(4, "Add chopped vegetables to the same skillet. Stir-fry for 3-5 minutes until tender-crisp.", timerSeconds = 300),
                    Instruction(5, "Push vegetables to one side, add a protein like chicken or tofu if desired, cook until browned."),
                    Instruction(6, "Pour in stir-fry sauce (mix soy sauce, ginger, garlic, honey, and sesame oil)."),
                    Instruction(7, "Toss everything together until coated and heated through. Serve immediately with rice.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Chicken", 300.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Broccoli", "Vegetables", 150.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 3.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Honey", "Sweeteners", 1.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Rice", "Rice", 1.0, "cup")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 7,
                images = listOf(R.drawable.vegetable_curry, R.drawable.vegetable_curry_2, R.drawable.vegetable_curry_3, R.drawable.vegetable_curry_4),
                videos = emptyList(),
                title = "Vegetable Curry",
                description = "Aromatic and hearty vegetarian dish.",
                steps = listOf(
                    Instruction(1, "Heat oil in a large pot or Dutch oven over medium heat. Add chopped onion and cook until softened.", timerSeconds = 300),
                    Instruction(2, "Stir in garlic, ginger, and curry powder. Cook for 1 minute until fragrant.", timerSeconds = 60),
                    Instruction(3, "Add chopped vegetables (potatoes, carrots, bell peppers) and cook for 5 minutes.", timerSeconds = 300),
                    Instruction(4, "Pour in vegetable broth and coconut milk. Bring to a simmer."),
                    Instruction(5, "Cover and cook for 15-20 minutes, or until vegetables are tender.", timerSeconds = 1200),
                    Instruction(6, "Stir in spinach and cook until wilted. Season with salt and pepper to taste."),
                    Instruction(7, "Serve hot with rice or naan bread.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
                    Ingredient(R.drawable.ingredient_tomato, "Curry Powder", "Spices", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Potato", 2.0, "medium"),
                    Ingredient(R.drawable.ingredient_tomato, "Carrots", "Vegetables", 2.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Vegetable Broth", "Other", 400.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Coconut Milk", "Dairy", 400.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Spinach", "Vegetables", 100.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Rice", "Rice", 1.0, "cup")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 8,
                images = listOf(R.drawable.creamy_pasta, R.drawable.creamy_pasta_2, R.drawable.creamy_pasta_3, R.drawable.creamy_pasta_4),
                videos = emptyList(),
                title = "Creamy Pasta",
                description = "Rich and comforting, a family favorite.",
                steps = listOf(
                    Instruction(1, "Cook pasta according to package directions. Reserve 1 cup pasta water before draining.", timerSeconds = 600),
                    Instruction(2, "In a large skillet, melt butter over medium heat. Add minced garlic and cook until fragrant (1 minute).", timerSeconds = 60),
                    Instruction(3, "Stir in heavy cream and Parmesan cheese. Cook, stirring, until cheese is melted and sauce thickens slightly.", timerSeconds = 300),
                    Instruction(4, "Add the cooked pasta to the skillet. Toss to coat with the sauce."),
                    Instruction(5, "If sauce is too thick, add reserved pasta water, a little at a time, until desired consistency is reached."),
                    Instruction(6, "Season with salt and black pepper. Garnish with fresh parsley or more Parmesan.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Pasta", "Pasta", 200.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Heavy Cream", "Dairy", 200.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Parmesan Cheese", "Cheese", 50.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25, "tsp")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 9,
                images = listOf(R.drawable.grilled_fish, R.drawable.grilled_fish_2, R.drawable.grilled_fish_3),
                videos = emptyList(),
                title = "Grilled Fish",
                description = "Light and healthy, perfect for summer.",
                steps = listOf(
                    Instruction(1, "Pat fish fillets dry with paper towels. Season both sides with salt, pepper, and lemon zest."),
                    Instruction(2, "Preheat grill to medium-high heat. Lightly oil the grill grates."),
                    Instruction(3, "Grill fish for 3-5 minutes per side, depending on thickness, until opaque and flakes easily with a fork.", timerSeconds = 300),
                    Instruction(4, "Squeeze fresh lemon juice over the grilled fish before serving."),
                    Instruction(5, "Serve with a side of steamed vegetables or a fresh salad.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Fish Fillets", "Seafood", 2.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Lemon", "Fruit", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive Oil", "Condiments", 1.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25, "tsp")
                ),
                servings = 2,
                duration = "35 min",
                upvoteCount = 789,
                recipeMaker = "by Mark's Kitchen",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "200 kcal"),
                    NutritionFact("Protein", "25g"),
                    NutritionFact("Fat", "10g"),
                    NutritionFact("Carbs", "5g")
                ),
                comments = listOf(
                    Comment("Fisherman Fred", "Simple and delicious, great for grilling season."),
                    Comment("Health Nut", "Light and healthy, exactly what I needed!", isUpvote = true)
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 11,
                images = listOf(R.drawable.beef_stew, R.drawable.beef_stew_2, R.drawable.beef_stew_3),
                videos = emptyList(),
                title = "Beef Stew",
                description = "Hearty and comforting, ideal for cold days.",
                steps = listOf(
                    Instruction(1, "Pat beef chunks dry. Season with salt and pepper."),
                    Instruction(2, "Heat oil in a large Dutch oven over medium-high heat. Brown beef in batches, then set aside.", timerSeconds = 600),
                    Instruction(3, "Add chopped onion, carrots, and celery to the pot. Cook until softened (5-7 minutes).", timerSeconds = 420),
                    Instruction(4, "Stir in minced garlic and cook for 1 minute until fragrant.", timerSeconds = 60),
                    Instruction(5, "Return beef to the pot. Add beef broth, diced tomatoes, herbs (thyme, bay leaf), and potatoes."),
                    Instruction(6, "Bring to a simmer, then reduce heat to low, cover, and cook for 1.5-2 hours, or until beef is very tender.", timerSeconds = 7200),
                    Instruction(7, "Thicken with a cornstarch slurry if desired. Serve hot with crusty bread.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Beef Chuck", "Beef", 500.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Carrots", "Vegetables", 2.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Celery", "Vegetables", 2.0, "stalks"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Potato", 300.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Beef Broth", "Other", 750.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Diced Tomatoes", "Vegetables", 400.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Thyme", "Herbs", 1.0, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Bay Leaf", "Herbs", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Bread", "Bread", 1.0, "slice")
                ),
                servings = 4,
                duration = "60 min",
                upvoteCount = 101,
                recipeMaker = "by Sophia's Recipes",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "450 kcal"),
                    NutritionFact("Protein", "35g"),
                    NutritionFact("Fat", "20g"),
                    NutritionFact("Carbs", "30g")
                ),
                comments = listOf(
                    Comment("Grandma's Boy", "Just like my grandma used to make! So hearty."),
                    Comment("Winter Warmer", "Perfect for a cold evening.", isUpvote = true)
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 12,
                images = listOf(R.drawable.tomato_soup, R.drawable.tomato_soup_2, R.drawable.tomato_soup_3, R.drawable.tomato_soup_4),
                videos = emptyList(),
                title = "Tomato Soup",
                description = "Classic comfort, warm and delicious.",
                steps = listOf(
                    Instruction(1, "Melt butter in a pot over medium heat. Add chopped onion and cook until softened (5 minutes).", timerSeconds = 300),
                    Instruction(2, "Stir in minced garlic and cook for 1 minute until fragrant.", timerSeconds = 60),
                    Instruction(3, "Add crushed tomatoes, vegetable broth, and basil. Bring to a simmer."),
                    Instruction(4, "Reduce heat, cover, and cook for 15-20 minutes to allow flavors to meld.", timerSeconds = 1200),
                    Instruction(5, "Carefully blend the soup with an immersion blender until smooth, or transfer to a regular blender."),
                    Instruction(6, "Stir in a splash of cream or milk (optional) for richness. Season with salt and pepper to taste."),
                    Instruction(7, "Serve hot with croutons or grilled cheese.")
                ),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Crushed Tomatoes", "Vegetables", 800.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Vegetable Broth", "Other", 500.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Fresh Basil", "Herbs", 0.25, "cup"),
                    Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 1.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Cream/Milk", "Dairy", 50.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Cheese", "Cheese", 20.0, "g")
                ),
                servings = 2,
                duration = "10 min",
                upvoteCount = 202,
                recipeMaker = "by Leo's Dishes",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "180 kcal"),
                    NutritionFact("Protein", "5g"),
                    NutritionFact("Fat", "8g"),
                    NutritionFact("Carbs", "20g")
                ),
                comments = listOf(
                    Comment("Soup Lover", "My favorite quick soup recipe!"),
                    Comment("Warm Belly", "Comfort in a bowl.", isUpvote = true)
                ),
                isUpvoted = false
            ),

            Recipe(
                id = 1,
                title = "Spaghetti Bolognese",
                description = "A classic Italian pasta dish with rich meat sauce.",
                images = listOf(R.drawable.spaghetti_bolognese, R.drawable.spaghetti_bolognese_2, R.drawable.spaghetti_bolognese_3, R.drawable.spaghetti_bolognese_4),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Spaghetti", "Pasta", 200.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Ground Beef", "Beef", 150.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Tomato Sauce", "Condiments", 100.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0, "piece"),
                    Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
                    Ingredient(R.drawable.ingredient_tomato, "Rice", "Rice", 1.0, "cup")
                ),
                steps = listOf(
                    Instruction(1, "Cook spaghetti according to package directions. Drain and set aside.", timerSeconds = 600),
                    Instruction(2, "In a large skillet or pot, brown ground beef over medium heat. Drain excess fat.", timerSeconds = 420),
                    Instruction(3, "Add chopped onion and minced garlic to the beef. Cook until onion is softened and transparent.", timerSeconds = 300),
                    Instruction(4, "Stir in tomato sauce and bring to a simmer. Reduce heat and let it simmer for at least 15 minutes to meld flavors (longer for richer taste).", timerSeconds = 900),
                    Instruction(5, "Season the sauce with salt, pepper, and any other desired herbs (e.g., oregano, basil)."),
                    Instruction(6, "Serve the bolognese sauce over the cooked spaghetti. Garnish with Parmesan cheese if desired.")
                ),
                servings = 2,
                duration = "30 min",
                upvoteCount = 124,
                recipeMaker = "by Mia's Meals",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "600 kcal"),
                    NutritionFact("Protein", "30g"),
                    NutritionFact("Fat", "25g"),
                    NutritionFact("Carbs", "50g")
                ),
                comments = listOf(
                    Comment("Pasta Lover", "Classic comfort food, can't go wrong with this!"),
                    Comment("Italian Grandma", "Delizioso! A perfect bolognese.", isUpvote = true)
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 2,
                title = "Fresh Garden Salad (Grocery)",
                description = "A light and healthy salad perfect for any meal.",
                images = listOf(R.drawable.fresh_garden_salad, R.drawable.fresh_garden_salad_2, R.drawable.fresh_garden_salad_3, R.drawable.fresh_garden_salad_4),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Lettuce", "Vegetables", 1.0, "head"),
                    Ingredient(R.drawable.ingredient_tomato, "Tomato", "Vegetables", 2.0, "pieces"),
                    Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 1.0, "piece"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Feta cheese", "Cheese", 50.0, "g")
                ),
                steps = listOf(
                    Instruction(1, "Wash and pat dry all vegetables. Tear or chop lettuce into bite-sized pieces."),
                    Instruction(2, "Slice tomatoes and cucumber into desired shapes."),
                    Instruction(3, "Combine lettuce, tomatoes, and cucumber in a large salad bowl."),
                    Instruction(4, "Crumble feta cheese over the vegetables."),
                    Instruction(5, "Drizzle with olive oil. Add salt and pepper to taste."),
                    Instruction(6, "Toss gently until all ingredients are well combined. Serve fresh.")
                ),
                servings = 1,
                duration = "15 min",
                upvoteCount = 89,
                recipeMaker = "by Jake's Plates",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "150 kcal"),
                    NutritionFact("Protein", "5g"),
                    NutritionFact("Fat", "10g"),
                    NutritionFact("Carbs", "10g")
                ),
                comments = listOf(
                    Comment("Salad Fan", "Fresh and light, great for lunch."),
                    Comment("Healthy Eater", "My daily dose of greens!", isUpvote = true)
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 3,
                title = "Grilled Chicken",
                description = "Simple, juicy grilled chicken breast.",
                images = listOf(R.drawable.grilled_chicken, R.drawable.grilled_chicken_2, R.drawable.grilled_chicken_3, R.drawable.grilled_chicken_4),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Chicken", 200.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 1.0, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Pepper", "Seasoning", 1.0, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Rosemary", "Herbs", 1.0, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 1.0, "tbsp")
                ),
                steps = listOf(
                    Instruction(1, "Pat chicken breasts dry with paper towels. Season generously with salt, pepper, and rosemary on both sides."),
                    Instruction(2, "Heat olive oil in a grill pan or outdoor grill over medium-high heat."),
                    Instruction(3, "Place chicken breasts on the hot grill. Cook for about 6-8 minutes per side, or until internal temperature reaches 165°F (74°C) and juices run clear.", timerSeconds = 480),
                    Instruction(4, "Remove from grill and let rest for 5 minutes before slicing or serving. This helps keep the chicken juicy.", timerSeconds = 300)
                ),
                servings = 2,
                duration = "25 min",
                upvoteCount = 176,
                recipeMaker = "by Mia's Meals",
                nutritionFacts = listOf(
                    NutritionFact("Calories", "280 kcal"),
                    NutritionFact("Protein", "40g"),
                    NutritionFact("Fat", "12g"),
                    NutritionFact("Carbs", "0g")
                ),
                comments = listOf(
                    Comment("Grill Master", "Perfectly grilled chicken every time!"),
                    Comment("Gym Buff", "High protein, low carb. Excellent!", isUpvote = true)
                ),
                isUpvoted = false
            ),

            Recipe(
                id = 101,
                title = "Hash Browns",
                description = "Classic crispy potato breakfast.",
                images = listOf(R.drawable.hash_brown, R.drawable.hash_brown_2, R.drawable.hash_brown_3, R.drawable.hash_brown_4, R.drawable.hash_brown_5),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Potato", 2.0, "large"),
                    Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25, "tsp")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 102,
                title = "Fudgy Brownies",
                description = "Rich, decadent, and perfectly fudgy.",
                images = listOf(R.drawable.fudgy_brownies, R.drawable.fudgy_brownies_2, R.drawable.fudgy_brownies_3, R.drawable.fudgy_brownies_4),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Unsalted Butter", "Dairy", 100.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 200.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Unsweetened Cocoa Powder", "Baking", 50.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Egg", 2.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "All-Purpose Flour", "Baking", 60.0, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 1.0, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.25, "tsp")
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
                ),
                isUpvoted = false
            ),
            Recipe(
                id = 103,
                title = "French Toast",
                description = "A sweet and savory breakfast classic.",
                images = listOf(R.drawable.french_toast, R.drawable.french_toast_2, R.drawable.french_toast_3, R.drawable.french_toast_4),
                videos = emptyList(),
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Bread", "Bread", 4.0, "slices"),
                    Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Egg", 2.0, "pcs"),
                    Ingredient(R.drawable.ingredient_tomato, "Milk", "Dairy", 100.0, "ml"),
                    Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 1.0, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 0.5, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Cinnamon", "Spices", 0.25, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 1.0, "tbsp")
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
                    Comment("Brunch Fan", "Best French Toast ever, simple and delicious!")
                ),
                isUpvoted = false
            )
        )
    )
}

// Global mutable list to store bookmarked recipes
val bookmarkedRecipes = mutableStateListOf<Recipe>().apply {
    val initialBookmarks = listOf(4, 6, 102, 5, 9)
    initialBookmarks.forEach { id ->
        allRecipes.find { it.id == id }?.let { add(it) }
    }
}

// GLOBAL mutable list to store upvoted recipes
val upvotedRecipes = mutableStateListOf<Recipe>().apply {
    // No initial upvotes for demonstration here, handled by `isUpvoted` in Recipe model
}


@Composable
fun MyRecipeScreen(navController: NavController) {
    // This LaunchedEffect will update the `isUpvoted` status of recipes in `allRecipes`
    // whenever `upvotedRecipes` changes, ensuring consistency.
    LaunchedEffect(upvotedRecipes.toList()) { // Use .toList() to trigger recomposition when content changes
        allRecipes.forEach { recipe ->
            recipe.isUpvoted = upvotedRecipes.contains(recipe)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(allRecipes) { recipe ->
                // Check the global upvotedRecipes list for the current upvote status
                val isUpvoted = upvotedRecipes.contains(recipe)
                val isBookmarked = bookmarkedRecipes.contains(recipe)


                ReusableCard(
                    imagePainter = painterResource(id = recipe.images.first()),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    upvoteCount = recipe.upvoteCount, // Now read directly from the recipe object
                    isBookmarked = isBookmarked,
                    onBookmarkClick = {
                        // Toggle bookmark status directly on the global list
                        if (bookmarkedRecipes.contains(recipe)) {
                            bookmarkedRecipes.remove(recipe)
                        } else {
                            bookmarkedRecipes.add(recipe)
                        }
                    },
                    onDeleteClick = {
                        allRecipes.remove(recipe)
                        bookmarkedRecipes.remove(recipe) // Ensure it's removed from bookmarks too
                        upvotedRecipes.remove(recipe) // Ensure it's removed from upvotes too
                    },
                    isUpvoted = isUpvoted, // Pass the status derived from the global list
                    onUpvoteClick = {
                        // Toggle upvote status directly on the global list
                        if (upvotedRecipes.contains(recipe)) {
                            upvotedRecipes.remove(recipe)
                            recipe.upvoteCount-- // Decrement count directly on the recipe object
                        } else {
                            upvotedRecipes.add(recipe)
                            recipe.upvoteCount++ // Increment count directly on the recipe object
                        }
                    },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
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