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
// import com.wesleyaldrich.pancook.ui.screens.allRecipes // Assuming allRecipes is a global mutable list
import com.wesleyaldrich.pancook.ui.navigation.Screen // Import Screen for navigation
import androidx.navigation.NavController // Import NavController
import androidx.navigation.compose.rememberNavController

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
    Recent("Ayam Geprek", R.drawable.ayam_geprek, 6), // Use base ayam_geprek.jpg
    Recent("Nasi Bakar", R.drawable.nasi_bakar, 5), // Use base nasi_bakar.jpg
    Recent("Ketoprak", R.drawable.ketoprak, 4), // Use base ketoprak.jpg
    Recent("Nasi Goreng", R.drawable.nasi_goreng, 7), // Use base nasi_goreng.jpg
    Recent("Rawon", R.drawable.rawon, 8) // Use base rawon.jpg
)

// Define specific Recipe objects for the "Most Popular" section
val mostPopularRecipes = listOf(
    Recipe(
        id = 1,
        title = "Spaghetti Bolognese",
        description = "A classic Italian pasta dish with rich meat sauce.",
        // Corrected image references based on your provided file
        images = listOf(R.drawable.spaghetti_bolognese, R.drawable.spaghetti_bolognese_2, R.drawable.spaghetti_bolognese_3, R.drawable.spaghetti_bolognese_4),
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
        )
    ),
    Recipe(
        id = 2,
        title = "Fresh Garden Salad (Grocery)",
        description = "A light and healthy salad perfect for any meal.",
        // Corrected image references based on your provided file
        images = listOf(R.drawable.fresh_garden_salad, R.drawable.fresh_garden_salad_2, R.drawable.fresh_garden_salad_3, R.drawable.fresh_garden_salad_4),
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
        )
    ),
    Recipe(
        id = 3,
        title = "Grilled Chicken",
        description = "Simple, juicy grilled chicken breast.",
        // Corrected image references based on your provided file
        images = listOf(R.drawable.grilled_chicken, R.drawable.grilled_chicken_2, R.drawable.grilled_chicken_3, R.drawable.grilled_chicken_4),
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
        )
    ),
    Recipe(
        id = 9, // Unique ID for Ketoprak Bandung
        images = listOf(R.drawable.grilled_fish, R.drawable.grilled_fish_2, R.drawable.grilled_fish_3),
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
        )
    )
)


// Your existing global recipes list (ensure it includes all recipes for DetailRecipeScreen)
val recipes = listOf(
    // Added 103 for French Toast which is in the video's daily cravings
    Recipe(
        id = 103,
        title = "French Toast",
        description = "A sweet and savory breakfast classic.",
        images = listOf(R.drawable.french_toast, R.drawable.french_toast_2, R.drawable.french_toast_3, R.drawable.french_toast_4),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Bread", "Bakery", 4.0, "slices"),
            Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0, "pcs"),
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
        comments = listOf(
            Comment("Brunch Fan", "Best French Toast ever, simple and delicious!")
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "350 kcal"),
            NutritionFact("Protein", "10g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "40g")
        )
    ),
    Recipe(
        id = 4,
        title = "Delicious Salad",
        description = "A refreshing mix of garden greens.",
        images = listOf(R.drawable.salad, R.drawable.salad_2, R.drawable.salad_3, R.drawable.salad_4, R.drawable.salad_5),
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
            Ingredient(R.drawable.ingredient_tomato, "Egg", "Seasoning", 2.05, "pcs")
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
        comments = listOf(
            Comment("John Doe", "This is a delicious salad, easy to make!"),
            Comment("Jane Smith", "Loved the addition of feta cheese, great recipe!", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "250 kcal"),
            NutritionFact("Protein", "10g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "20g")
        )
    ),
    Recipe(
        id = 5,
        images = listOf(R.drawable.spicy_noodle, R.drawable.spicy_noodle_2, R.drawable.spicy_noodle_3, R.drawable.spicy_noodle_4),
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
            Ingredient(R.drawable.ingredient_tomato, "Instant Noodles", "Grains", 100.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Chili Peppers", "Vegetables", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Mixed Vegetables", "Vegetables", 150.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 2.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5, "tsp")
        ),
        servings = 2,
        duration = "20 min",
        upvoteCount = 567,
        recipeMaker = "by Cook Ben",
        comments = listOf(
            Comment("Mark T.", "Super spicy, just how I like it!"),
            Comment("Chef Emily", "Quick dinner solution, love it!", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "350 kcal"),
            NutritionFact("Protein", "12g"),
            NutritionFact("Fat", "10g"),
            NutritionFact("Carbs", "50g")
        )
    ),
    Recipe(
        id = 6,
        images = listOf(R.drawable.chicken_stir_fry, R.drawable.chicken_stir_fry_2, R.drawable.chicken_stir_fry_3, R.drawable.chicken_stir_fry_4),
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
            Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Meat", 300.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Broccoli", "Vegetables", 150.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Soy Sauce", "Condiments", 3.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Honey", "Sweeteners", 1.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sesame Oil", "Condiments", 0.5, "tsp")
        ),
        servings = 2,
        duration = "30 min",
        upvoteCount = 890,
        recipeMaker = "by Foodie Clara",
        comments = listOf(
            Comment("David S.", "My go-to weeknight recipe!"),
            Comment("Sara L.", "Healthy and delicious. Perfect combo.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "400 kcal"),
            NutritionFact("Protein", "30g"),
            NutritionFact("Fat", "18g"),
            NutritionFact("Carbs", "25g")
        )
    ),
    Recipe(
        id = 7,
        images = listOf(R.drawable.vegetable_curry, R.drawable.vegetable_curry_2, R.drawable.vegetable_curry_3, R.drawable.vegetable_curry_4),
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
            Ingredient(R.drawable.ingredient_tomato, "Onion", "Vegetables", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Curry Powder", "Spices", 2.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0, "medium"),
            Ingredient(R.drawable.ingredient_tomato, "Carrots", "Vegetables", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Bell Peppers", "Vegetables", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Vegetable Broth", "Liquids", 400.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Coconut Milk", "Dairy", 400.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Spinach", "Vegetables", 100.0, "g")
        ),
        servings = 2,
        duration = "45 min",
        upvoteCount = 123,
        recipeMaker = "by Gourmet David",
        comments = listOf(
            Comment("Priya R.", "So flavorful and comforting!"),
            Comment("Vegan Chef", "A staple in my kitchen, absolutely love it.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "300 kcal"),
            NutritionFact("Protein", "8g"),
            NutritionFact("Fat", "15g"),
            NutritionFact("Carbs", "35g")
        )
    ),
    Recipe(
        id = 8,
        images = listOf(R.drawable.creamy_pasta, R.drawable.creamy_pasta_2, R.drawable.creamy_pasta_3, R.drawable.creamy_pasta_4),
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
            Ingredient(R.drawable.ingredient_tomato, "Pasta", "Grains", 200.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Heavy Cream", "Dairy", 200.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Parmesan Cheese", "Dairy", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25, "tsp")
        ),
        servings = 2,
        duration = "25 min",
        upvoteCount = 456,
        recipeMaker = "by Baker Emily",
        comments = listOf(
            Comment("Tom H.", "My kids devour this every time!"),
            Comment("RecipeFan", "Creamy perfection!", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "550 kcal"),
            NutritionFact("Protein", "15g"),
            NutritionFact("Fat", "30g"),
            NutritionFact("Carbs", "50g")
        )
    ),
    Recipe(
        id = 101,
        title = "Hash Browns",
        description = "Classic crispy potato breakfast.",
        images = listOf(R.drawable.hash_brown, R.drawable.hash_brown_2, R.drawable.hash_brown_3, R.drawable.hash_brown_4, R.drawable.hash_brown_5),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0, "large"),
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
        comments = listOf(
            Comment("Breakfast King", "Crispy and delicious, a perfect breakfast side!"),
            Comment("Morning Person", "Can't start my day without these.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "300 kcal"),
            NutritionFact("Protein", "5g"),
            NutritionFact("Fat", "18g"),
            NutritionFact("Carbs", "30g")
        )
    ),
    Recipe(
        id = 102,
        title = "Fudgy Brownies",
        description = "Rich, decadent, and perfectly fudgy.",
        images = listOf(R.drawable.fudgy_brownies, R.drawable.fudgy_brownies_2, R.drawable.fudgy_brownies_3, R.drawable.fudgy_brownies_4),
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Unsalted Butter", "Dairy", 100.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 200.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Unsweetened Cocoa Powder", "Baking", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "All-Purpose Flour", "Baking", 60.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 1.0, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.25, "tsp")
        ),
        steps = listOf(
            Instruction(1, "Preheat oven to 175°C (350°F). Grease and flour an 8x8 inch baking pan."),
            Instruction(2, "In a medium saucepan, melt butter over low heat. Remove from heat and stir in sugar until combined."),
            Instruction(3, "Whisk in cocoa Powder. Add eggs one at a time, mixing well after each addition. Stir in vanilla extract."),
            Instruction(4, "Gradually add flour and salt, mixing until just combined. Do not overmix."),
            Instruction(5, "Pour batter into the prepared baking pan and spread evenly."),
            Instruction(6, "Bake for 20-25 minutes, or until a toothpick inserted into the center comes out with moist crumbs (not wet batter)."),
            Instruction(7, "Let cool completely in the pan on a wire rack before cutting into squares.")
        ),
        servings = 2,
        duration = "1 hour",
        upvoteCount = 750,
        recipeMaker = "by Baker John",
        comments = listOf(
            Comment("Sweet Tooth", "The fudgiest brownies ever! A must-try."),
            Comment("Dessert Queen", "Perfect texture and rich chocolate flavor.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "450 kcal"),
            NutritionFact("Protein", "5g"),
            NutritionFact("Fat", "25g"),
            NutritionFact("Carbs", "60g")
        )
    )
)

// The allRecipes list should ideally be a single, comprehensive source for all recipes in your app.
// It should contain *all* recipes, including those for "Most Popular" and other sections.
val allAppRecipes = recipes + mostPopularRecipes // Combine all recipes into one global list

@Composable
fun HomeScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val displayedCategories = allCategories.take(7)
    val scrollState = rememberScrollState()

    // Find the specific recipes for the "Most Popular" section from the mostPopularRecipes list
    val ayamGeprekRecipe = mostPopularRecipes.find { it.id == 1 } // Find by its unique ID
    val rawonRecipe = mostPopularRecipes.find { it.id == 2 } // Find by its unique ID
    val nasiBakarRecipe = mostPopularRecipes.find { it.id == 3 } // Find by its unique ID
    val ketoprakRecipe = mostPopularRecipes.find { it.id == 9 } // Find by its unique ID


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

        // Pass a subset of local 'recipes' to SwipeableCardStack
        SwipeableCardStack(
            recipes = recipes.take(6),
            modifier = Modifier.fillMaxSize(),
            navController = navController // Pass navController directly
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
                if (ayamGeprekRecipe != null) { // Use the specific recipe object
                    Box(modifier = Modifier.weight(0.58f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = ayamGeprekRecipe.images.first()),
                            title = ayamGeprekRecipe.title,
                            description = ayamGeprekRecipe.recipeMaker,
                            duration = ayamGeprekRecipe.duration,
                            likeCount = ayamGeprekRecipe.upvoteCount,
                            imageHeight = 210.dp,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(ayamGeprekRecipe.id)) },
                            isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == ayamGeprekRecipe.id }),
                            onBookmarkClick = {
                                val canonicalRecipe = allAppRecipes.find { it.id == ayamGeprekRecipe.id }
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
                        )
                    }
                }

                if (nasiBakarRecipe != null) { // Use the specific recipe object
                    Box(modifier = Modifier.weight(0.5f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = nasiBakarRecipe.images.first()),
                            title = nasiBakarRecipe.title,
                            description = nasiBakarRecipe.recipeMaker,
                            duration = nasiBakarRecipe.duration,
                            likeCount = nasiBakarRecipe.upvoteCount,
                            imageHeight = 150.dp,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(nasiBakarRecipe.id)) },
                            isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == nasiBakarRecipe.id }),
                            onBookmarkClick = {
                                val canonicalRecipe = allAppRecipes.find { it.id == nasiBakarRecipe.id }
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
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
                if (rawonRecipe != null) { // Use the specific recipe object
                    Box(modifier = Modifier.weight(0.41f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = rawonRecipe.images.first()),
                            title = rawonRecipe.title,
                            description = rawonRecipe.recipeMaker,
                            duration = rawonRecipe.duration,
                            likeCount = rawonRecipe.upvoteCount,
                            imageHeight = 150.dp,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(rawonRecipe.id)) },
                            isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == rawonRecipe.id }),
                            onBookmarkClick = {
                                val canonicalRecipe = allAppRecipes.find { it.id == rawonRecipe.id }
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
                        )
                    }
                }

                if (ketoprakRecipe != null) { // Use the specific recipe object
                    Box(modifier = Modifier.weight(0.55f)) {
                        RecipeReusableCard(
                            imagePainter = painterResource(id = ketoprakRecipe.images.first()),
                            title = ketoprakRecipe.title,
                            description = ketoprakRecipe.recipeMaker,
                            duration = ketoprakRecipe.duration,
                            likeCount = ketoprakRecipe.upvoteCount,
                            imageHeight = 210.dp,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(ketoprakRecipe.id)) },
                            isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == ketoprakRecipe.id }),
                            onBookmarkClick = {
                                val canonicalRecipe = allAppRecipes.find { it.id == ketoprakRecipe.id }
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
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
                            .background(Color.LightGray)
                            .clickable {
                                navController.navigate(Screen.Category.createRoute(category.name))
                            },
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
                                            .background(Color.LightGray)
                                            .clickable {
                                                navController.navigate(Screen.Category.createRoute(category.name))
                                            },
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
                val recipe = allAppRecipes.find { it.id == recent.recipeId } // Changed to allAppRecipes
                if (recipe != null) {
                    Box(modifier = Modifier.clickable { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) }) {
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
                                    painter = painterResource(id = recipe.images.first()),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(recipe.title, fontSize = 12.sp, fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                        }
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
        RecipeGridSection(recipes, navController = navController)
    }
}

@Composable
fun RecipeGridSection(recipes: List<Recipe>, navController: NavController) {
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
                            imagePainter = painterResource(id = recipe.images.first()),
                            title = recipe.title,
                            description = recipe.recipeMaker,
                            duration = recipe.duration,
                            likeCount = recipe.upvoteCount,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) },
                            isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == recipe.id }), // Changed to allAppRecipes
                            onBookmarkClick = {
                                val canonicalRecipe = allAppRecipes.find { it.id == recipe.id } // Changed to allAppRecipes
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
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
    navController: NavController
) {
    val cardStack = remember { mutableStateListOf<Recipe>().apply { addAll(recipes) } }
    val swipedCards = remember { mutableStateListOf<Recipe>() }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    var restoring by remember { mutableStateOf(false) }
    val restoreOffsetX = remember { Animatable(0f) }

    Box(modifier = modifier.padding(start = 20.dp)) {

        // Handle restoring card
        if (restoring && swipedCards.isNotEmpty()) {
            val recipe = swipedCards.last()

            LaunchedEffect(recipe) {
                restoreOffsetX.snapTo(-1000f)
                restoreOffsetX.animateTo(0f, tween(400))
                swipedCards.removeLast()
                cardStack.add(recipe)
                restoring = false
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .offset { IntOffset(restoreOffsetX.value.toInt(), 0) }
                    .graphicsLayer {
                        translationX = restoreOffsetX.value
                        scaleX = 1f
                        scaleY = 1f
                        shadowElevation = 0f
                    }
                    .zIndex(cardStack.size + 1f)
            ) {
                RecipeReusableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    imagePainter = painterResource(id = recipe.images.first()),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    likeCount = recipe.upvoteCount,
                    imageHeight = 230.dp,
                    onClick = { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) },
                    isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == recipe.id }),
                    onBookmarkClick = {
                        allAppRecipes.find { it.id == recipe.id }?.let { canonical ->
                            if (bookmarkedRecipes.contains(canonical)) bookmarkedRecipes.remove(canonical)
                            else bookmarkedRecipes.add(canonical)
                        }
                    }
                )
            }
        }

        // Swipeable cards
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
                                            offsetX.snapTo(0f)
                                        } else {
                                            offsetX.animateTo(0f, tween(300))
                                        }
                                    }
                                }
                            )
                        }
                    }
                    .clickable(enabled = isTop) {
                        navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                    }
            ) {
                RecipeReusableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    imagePainter = painterResource(id = recipe.images.first()),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    likeCount = recipe.upvoteCount,
                    imageHeight = 230.dp,
                    onClick = { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) },
                    isBookmarked = bookmarkedRecipes.contains(allAppRecipes.find { it.id == recipe.id }),
                    onBookmarkClick = {
                        allAppRecipes.find { it.id == recipe.id }?.let { canonical ->
                            if (bookmarkedRecipes.contains(canonical)) bookmarkedRecipes.remove(canonical)
                            else bookmarkedRecipes.add(canonical)
                        }
                    }
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomepageScreenPreview() {
    PancookTheme {
        HomeScreen(navController = rememberNavController())
    }
}