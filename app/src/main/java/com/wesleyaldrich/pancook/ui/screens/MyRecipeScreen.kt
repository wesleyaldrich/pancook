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
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.model.Instruction
import com.wesleyaldrich.pancook.model.NutritionFact // Import NutritionFact
import com.wesleyaldrich.pancook.model.Comment // Import Comment

// Global/Shared data source (for demonstration; use ViewModel/Repository in real app)
// This list is initialized once when the application starts.
val allRecipes = mutableStateListOf<Recipe>().apply {
    addAll(
        listOf(
            Recipe(
                id = 4,
                title = "Delicious Salad",
                description = "A refreshing mix of garden greens.",
                image = R.drawable.salad,
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
                image = R.drawable.salad,
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
                image = R.drawable.salad,
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
                image = R.drawable.salad,
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
                id = 9,
                image = R.drawable.salad,
                title = "Grilled Fish",
                description = "Light and healthy, perfect for summer.",
                steps = listOf(
                    Instruction(1, "Pat fish fillets dry with paper towels. Season both sides with salt, pepper, and lemon zest."),
                    Instruction(2, "Preheat grill to medium-high heat. Lightly oil the grill grates."),
                    Instruction(3, "Grill fish for 3-5 minutes per side, depending on thickness, until opaque and flakes easily with a fork."),
                    Instruction(4, "Squeeze fresh lemon juice over the grilled fish before serving."),
                    Instruction(5, "Serve with a side of steamed vegetables or a fresh salad.")
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
            ),
            Recipe(
                id = 11,
                image = R.drawable.salad,
                title = "Beef Stew",
                description = "Hearty and comforting, ideal for cold days.",
                steps = listOf(
                    Instruction(1, "Pat beef chunks dry. Season with salt and pepper."),
                    Instruction(2, "Heat oil in a large Dutch oven over medium-high heat. Brown beef in batches, then set aside."),
                    Instruction(3, "Add chopped onion, carrots, and celery to the pot. Cook until softened (5-7 minutes)."),
                    Instruction(4, "Stir in minced garlic and cook for 1 minute until fragrant."),
                    Instruction(5, "Return beef to the pot. Add beef broth, diced tomatoes, herbs (thyme, bay leaf), and potatoes."),
                    Instruction(6, "Bring to a simmer, then reduce heat to low, cover, and cook for 1.5-2 hours, or until beef is very tender."),
                    Instruction(7, "Thicken with a cornstarch slurry if desired. Serve hot with crusty bread.")
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
                )
            ),
            Recipe(
                id = 12,
                image = R.drawable.salad,
                title = "Tomato Soup",
                description = "Classic comfort, warm and delicious.",
                steps = listOf(
                    Instruction(1, "Melt butter in a pot over medium heat. Add chopped onion and cook until softened (5 minutes)."),
                    Instruction(2, "Stir in minced garlic and cook for 1 minute until fragrant."),
                    Instruction(3, "Add crushed tomatoes, vegetable broth, and basil. Bring to a simmer."),
                    Instruction(4, "Reduce heat, cover, and cook for 15-20 minutes to allow flavors to meld."),
                    Instruction(5, "Carefully blend the soup with an immersion blender until smooth, or transfer to a regular blender."),
                    Instruction(6, "Stir in a splash of cream or milk (optional) for richness. Season with salt and pepper to taste."),
                    Instruction(7, "Serve hot with croutons or grilled cheese.")
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
                )
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
                    Instruction(1, "Cook spaghetti according to package directions. Drain and set aside."),
                    Instruction(2, "In a large skillet or pot, brown ground beef over medium heat. Drain excess fat."),
                    Instruction(3, "Add chopped onion and minced garlic to the beef. Cook until onion is softened and transparent."),
                    Instruction(4, "Stir in tomato sauce and bring to a simmer. Reduce heat and let it simmer for at least 15 minutes to meld flavors (longer for richer taste)."),
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
                image = R.drawable.salad,
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Lettuce", "Vegetables", 1.0f, "head"),
                    Ingredient(R.drawable.ingredient_tomato, "Tomato", "Vegetables", 2.0f, "pieces"),
                    Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 1.0f, "piece"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 2.0f, "tbsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Feta cheese", "Dairy", 50.0f, "g")
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
                image = R.drawable.fudgy_brownies,
                ingredients = listOf(
                    Ingredient(R.drawable.ingredient_tomato, "Chicken Breast", "Meat", 200.0f, "g"),
                    Ingredient(R.drawable.ingredient_tomato, "Salt", "Spices", 1.0f, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Pepper", "Spices", 1.0f, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Rosemary", "Herbs", 1.0f, "tsp"),
                    Ingredient(R.drawable.ingredient_tomato, "Olive oil", "Condiments", 1.0f, "tbsp")
                ),
                steps = listOf(
                    Instruction(1, "Pat chicken breasts dry with paper towels. Season generously with salt, pepper, and rosemary on both sides."),
                    Instruction(2, "Heat olive oil in a grill pan or outdoor grill over medium-high heat."),
                    Instruction(3, "Place chicken breasts on the hot grill. Cook for about 6-8 minutes per side, or until internal temperature reaches 165°F (74°C) and juices run clear."),
                    Instruction(4, "Remove from grill and let rest for 5 minutes before slicing or serving. This helps keep the chicken juicy.")
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
            )
        )
    )
}

@Composable
fun MyRecipeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(allRecipes) { recipe ->
                ReusableCard(
                    imagePainter = painterResource(id = recipe.image),
                    title = recipe.title,
                    description = recipe.recipeMaker,
                    duration = recipe.duration,
                    upvoteCount = recipe.upvoteCount,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) // Pass only ID
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