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
import com.wesleyaldrich.pancook.ui.screens.bookmarkedRecipes
// REMOVED THE CONFLICTING IMPORT AS WE'RE DEFINING A NEW GLOBAL HERE
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
        id = 1, // Unique ID for Ayam Geprek
        title = "Ayam Geprek",
        description = "Crispy fried chicken with fiery sambal, a beloved Indonesian dish.",
        images = listOf(R.drawable.ayam_geprek, R.drawable.chicken_stir_fry_2, R.drawable.chicken_stir_fry_3), // Example additional images
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Chicken Drumsticks/Thighs", "Meat", 500.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "All-Purpose Flour", "Grains", 150.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Cornstarch", "Grains", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Egg", "Dairy", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 3.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Red Chilies", "Vegetables", 10.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Bird's Eye Chilies", "Vegetables", 5.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Shallots", "Vegetables", 3.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Cooking Oil", "Fats", 500.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 1.0, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sugar", "Sweeteners", 0.5, "tsp")
        ),
        steps = listOf(
            Instruction(1, "Prepare the chicken: Marinate chicken pieces with salt and pepper for 15 minutes. In a bowl, mix flour and cornstarch. In another bowl, beat the egg."),
            Instruction(2, "Coat the chicken: Dip each chicken piece into the beaten egg, then roll it in the flour mixture, ensuring it's fully coated. Repeat for a thicker crust."),
            Instruction(3, "Fry the chicken: Heat cooking oil in a deep pan. Fry the coated chicken pieces until golden brown and crispy, about 8-10 minutes. Drain excess oil."),
            Instruction(4, "Prepare the sambal: Roughly chop garlic, red chilies, bird's eye chilies, and shallots. You can also lightly fry them for a minute if desired."),
            Instruction(5, "Pound the sambal: Place the chopped ingredients in a mortar. Add salt and sugar. Pound until desired consistency is reached. Add a tablespoon of hot oil from frying the chicken."),
            Instruction(6, "Smash the chicken: Place a piece of fried chicken on the sambal in the mortar. Gently smash it using the pestle to combine it with the sambal."),
            Instruction(7, "Serve immediately with warm rice and fresh vegetables.")
        ),
        servings = 2,
        duration = "30 min",
        upvoteCount = 1000,
        recipeMaker = "By Chef Pok",
        comments = listOf(
            Comment("Spicy Lover", "This Ayam Geprek is incredibly authentic and delicious!"),
            Comment("Foodie Adventurer", "The sambal has the perfect kick, absolutely love it.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "480 kcal"),
            NutritionFact("Protein", "35g"),
            NutritionFact("Fat", "30g"),
            NutritionFact("Carbs", "20g")
        )
    ),
    Recipe(
        id = 2, // Unique ID for Rawon
        title = "Rawon Khas Jawa Timur",
        description = "A rich, aromatic black beef soup from East Java, known for its unique 'keluak' nut flavor.",
        images = listOf(R.drawable.rawon), // Example additional images
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Beef (shank/brisket)", "Meat", 500.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Kluwek (Black Nut)", "Spices", 4.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Shallots", "Vegetables", 8.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 4.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Red Chili", "Vegetables", 3.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Turmeric", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Ginger", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Lemongrass", "Spices", 1.0, "stalk"),
            Ingredient(R.drawable.ingredient_tomato, "Galangal", "Spices", 1.0, "inch"),
            Ingredient(R.drawable.ingredient_tomato, "Bay Leaves", "Spices", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Lime Leaves", "Spices", 3.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Cooking Oil", "Fats", 2.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Water", "Liquids", 1.5, "L"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 1.0, "tsp")
        ),
        steps = listOf(
            Instruction(1, "Prepare Kluwek: Crack open kluwek nuts, scoop out the black paste, and soak in hot water for 15 minutes. Mash into a paste."),
            Instruction(2, "Prepare Beef: Cut beef into cubes. Boil beef until tender, discard water (or reserve for broth), and set aside."),
            Instruction(3, "Grind Spices: Blend or grind shallots, garlic, red chili, turmeric, ginger, and galangal into a smooth paste."),
            Instruction(4, "Sauté Spices: Heat oil in a pot. Sauté the ground spice paste until fragrant. Add lemongrass (bruised), bay leaves, and lime leaves."),
            Instruction(5, "Add Kluwek: Stir in the kluwek paste and cook for another 2-3 minutes."),
            Instruction(6, "Combine: Add the boiled beef and stir to coat with the spices. Pour in water and bring to a boil."),
            Instruction(7, "Simmer: Reduce heat, cover, and simmer for at least 1 hour, or until flavors meld and beef is very tender. Season with salt to taste."),
            Instruction(8, "Serve hot with rice, bean sprouts, salted egg, and a sprinkle of fried shallots.")
        ),
        servings = 4,
        duration = "1 hour 30 min", // Adjusted for realistic cooking time
        upvoteCount = 500,
        recipeMaker = "By Juseyo",
        comments = listOf(
            Comment("Indonesian Foodie", "The authentic taste of East Java, truly amazing!"),
            Comment("Soup Lover", "So rich and comforting, perfect on a rainy day.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "400 kcal"),
            NutritionFact("Protein", "25g"),
            NutritionFact("Fat", "28g"),
            NutritionFact("Carbs", "15g")
        )
    ),
    Recipe(
        id = 3, // Unique ID for Nasi Bakar Cumi
        title = "Nasi Bakar Cumi",
        description = "Fragrant grilled rice with savory squid, wrapped in aromatic banana leaves.",
        images = listOf(R.drawable.nasi_bakar), // Example additional images
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Cooked Rice", "Grains", 3.0, "cups"),
            Ingredient(R.drawable.ingredient_tomato, "Squid (cleaned, cut)", "Seafood", 250.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Coconut Milk", "Dairy", 100.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Shallots", "Vegetables", 4.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Red Chilies", "Vegetables", 3.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Lemongrass (bruised)", "Spices", 1.0, "stalk"),
            Ingredient(R.drawable.ingredient_tomato, "Kaffir Lime Leaves", "Spices", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Bay Leaves", "Spices", 1.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Cooking Oil", "Fats", 1.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sugar", "Sweeteners", 0.25, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Banana Leaves", "Other", 4.0, "sheets")
        ),
        steps = listOf(
            Instruction(1, "Prepare Banana Leaves: Briefly wilt banana leaves over a flame or steam them to make them pliable."),
            Instruction(2, "Sauté Spices: Grind or finely chop shallots, garlic, and red chilies. Sauté in oil until fragrant. Add lemongrass, kaffir lime leaves, and bay leaf."),
            Instruction(3, "Cook Squid: Add cleaned and cut squid to the pan. Cook until it changes color and is tender. Season with salt and sugar."),
            Instruction(4, "Mix with Rice: In a large bowl, combine the cooked rice with the sautéed squid mixture and coconut milk. Mix well."),
            Instruction(5, "Wrap Rice: Take a piece of banana leaf. Place a portion of the rice mixture in the center. Fold the leaf to form a packet and secure with toothpicks."),
            Instruction(6, "Grill: Grill the banana leaf packets over charcoal or on a griddle until the leaves are slightly charred and fragrant, about 5-7 minutes per side."),
            Instruction(7, "Serve hot directly from the banana leaf.")
        ),
        servings = 2,
        duration = "45 min", // Adjusted for realistic cooking time
        upvoteCount = 500,
        recipeMaker = "By Chef Jusa",
        comments = listOf(
            Comment("Traditional Taste", "The aroma of the banana leaf makes this so special!"),
            Comment("Seafood Lover", "Delicious and unique, the squid is perfectly cooked.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "520 kcal"),
            NutritionFact("Protein", "18g"),
            NutritionFact("Fat", "20g"),
            NutritionFact("Carbs", "65g")
        )
    ),
    Recipe(
        id = 9, // Unique ID for Ketoprak Bandung
        title = "Ketoprak Bandung",
        description = "A popular Indonesian vegetarian dish with rice vermicelli, tofu, and rich peanut sauce.",
        images = listOf(R.drawable.ketoprak), // Example additional images
        ingredients = listOf(
            Ingredient(R.drawable.ingredient_tomato, "Rice Vermicelli (bihun)", "Grains", 100.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Fried Tofu (cut)", "Protein", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Lontong or Rice Cake (cubed)", "Grains", 100.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Cabbage (shredded)", "Vegetables", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Bean Sprouts", "Vegetables", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Cucumber (sliced)", "Vegetables", 0.5, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Fried Peanuts", "Nuts", 50.0, "g"),
            Ingredient(R.drawable.ingredient_tomato, "Garlic", "Spices", 2.0, "cloves"),
            Ingredient(R.drawable.ingredient_tomato, "Red Chilies", "Vegetables", 2.0, "pcs"),
            Ingredient(R.drawable.ingredient_tomato, "Tamarind Paste", "Condiments", 1.0, "tsp"),
            Ingredient(R.drawable.ingredient_tomato, "Palm Sugar", "Sweeteners", 1.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Sweet Soy Sauce (Kecap Manis)", "Condiments", 2.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Water", "Liquids", 100.0, "ml"),
            Ingredient(R.drawable.ingredient_tomato, "Fried Shallots", "Garnish", 1.0, "tbsp"),
            Ingredient(R.drawable.ingredient_tomato, "Emping/Kerupuk (crackers)", "Other", 1.0, "portion")
        ),
        steps = listOf(
            Instruction(1, "Prepare Vermicelli: Soak rice vermicelli in hot water until soft. Drain and set aside."),
            Instruction(2, "Blanch Vegetables: Quickly blanch cabbage and bean sprouts in hot water until tender-crisp. Drain and set aside."),
            Instruction(3, "Prepare Peanut Sauce: In a mortar, pound fried peanuts, garlic, red chilies, tamarind paste, and palm sugar until smooth. Gradually add water to achieve desired consistency."),
            Instruction(4, "Assemble: Arrange rice vermicelli, fried tofu, lontong (or rice cake), blanched cabbage, bean sprouts, and cucumber slices on a plate."),
            Instruction(5, "Pour Sauce: Drizzle the prepared peanut sauce generously over all ingredients. Add sweet soy sauce to taste."),
            Instruction(6, "Garnish: Sprinkle with fried shallots and serve with emping or kerupuk (crackers).")
        ),
        servings = 2,
        duration = "30 min",
        upvoteCount = 1500,
        recipeMaker = "By Kai",
        comments = listOf(
            Comment("Vegetarian Delight", "So flavorful and filling, a great vegetarian option!"),
            Comment("Indonesian Street Food", "Authentic Ketoprak taste, reminds me of home.", isUpvote = true)
        ),
        nutritionFacts = listOf(
            NutritionFact("Calories", "450 kcal"),
            NutritionFact("Protein", "15g"),
            NutritionFact("Fat", "25g"),
            NutritionFact("Carbs", "45g")
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
            recipes = recipes.take(6), // Using local 'recipes' list
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
                        translationX = 1f
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
                    imagePainter = painterResource(id = recipe.images.first()),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    likeCount = recipe.upvoteCount,
                    imageHeight = 230.dp,
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
                    .clickable(enabled = isTop) { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) }
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
    }
}


@Preview(showBackground = true)
@Composable
fun HomepageScreenPreview() {
    PancookTheme {
        HomeScreen(navController = rememberNavController())
    }
}