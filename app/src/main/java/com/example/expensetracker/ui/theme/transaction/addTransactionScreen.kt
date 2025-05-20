package com.example.expensetracker.ui.theme.transaction

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.firebase.AuthViewModel
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField
import com.example.expensetracker.ui.theme.stats.StatsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddTransactionScreen(navController: NavController, viewModel: StatsViewModel) {
    val context = LocalContext.current
    val stripePattern = Color(0xFFFFEEEE)
    val scrollState = rememberScrollState()


    // Now we can safely access uid
    val transactionTypes = listOf("Income", "Expense")
    var selectedTransactionType by remember { mutableStateOf(transactionTypes[0]) }
    var expandedTransactionType by remember { mutableStateOf(false) }

    // Category options
    val incomeCategories = listOf("Salary", "Freelance", "Gift", "Investment", "Other")
    val expenseCategories = listOf("Food", "Transportation", "Housing", "Entertainment", "Shopping", "Utilities", "Other")
    val categories = if (selectedTransactionType == "Income") incomeCategories else expenseCategories
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var expandedCategory by remember { mutableStateOf(false) }

    // Transaction details
    var valueText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // Main content with animated entrance
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(stripePattern)
                .verticalScroll(scrollState)
        ) {
            // Top App Bar with elevation animation
            var appBarElevated by remember { mutableStateOf(false) }
            LaunchedEffect(scrollState) {
                snapshotFlow { scrollState.value }
                    .collect { appBarElevated = it > 0 }
            }

            AnimatedElevationAppBar(
                elevated = appBarElevated,
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.scaleInOnPressAnimation()
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }

            )

            // Form content with padding animations
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Transaction Type Dropdown with animation
                AnimatedContent(
                    targetState = selectedTransactionType,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    }
                ) { targetType ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(8.dp), spotColor = Color(0xFF4A6DF0).copy(alpha = 0.3f))
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedTransactionType,
                            onExpandedChange = { expandedTransactionType = !expandedTransactionType }
                        ) {
                            OutlinedTextField(
                                value = targetType,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTransactionType)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF4A6DF0),
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                focusedTextColor  =  MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor  = MaterialTheme.colorScheme.onSurface
                            ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedTransactionType,
                                onDismissRequest = { expandedTransactionType = false }
                            ) {
                                transactionTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            selectedTransactionType = type
                                            selectedCategory = if (type == "Income") incomeCategories[0] else expenseCategories[0]
                                            expandedTransactionType = false
                                        },
                                        modifier = Modifier.animateEnterExit(
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Value TextField with floating label animation
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    OutlinedTextField(
                        value = valueText,
                        onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) valueText = it },
                        label = { Text("Amount") },
                        placeholder = { Text("0.00") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Text(
                                text = "$",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF4A6DF0),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Category Dropdown with animation
                AnimatedContent(
                    targetState = Pair(selectedTransactionType, selectedCategory),
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { (_, targetCategory) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(8.dp), spotColor = Color(0xFF4A6DF0).copy(alpha = 0.3f))
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedCategory,
                            onExpandedChange = { expandedCategory = !expandedCategory }
                        ) {
                            OutlinedTextField(
                                value = targetCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF4A6DF0),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        focusedTextColor  =  MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor  = MaterialTheme.colorScheme.onSurface
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedCategory,
                                onDismissRequest = { expandedCategory = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expandedCategory = false
                                        },
                                        modifier = Modifier.animateEnterExit(
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Description TextField
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = { descriptionText = it },
                        label = { Text("Description (Optional)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF4A6DF0),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button with loading animation
                PrimaryButton(
                    text = "Add Transaction",
                    onClick = {
                        // Validate input
                        if (valueText.isBlank()) {
                            Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }

                        val amount = valueText.toFloatOrNull() ?: run {
                            Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }

                        // Create transaction object
                        val transaction = Transaction(
                            amount = amount,
                            type = if (selectedTransactionType == "Income") "income" else "expense",
                            category = selectedCategory,
                            date = System.currentTimeMillis(),
                            description = descriptionText
                        )

                        // Save to database using ViewModel
                        viewModel.addTransaction(transaction)

                        Toast.makeText(context, "Transaction added successfully", Toast.LENGTH_SHORT).show()

                        // Clear fields
                        valueText = ""
                        descriptionText = ""

                        // Navigate back
                        navController.popBackStack()
                    },
                    enabled = valueText.isNotBlank() && !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scaleInOnPressAnimation(),

                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Add Transaction",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// Helper composable for animated elevation app bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedElevationAppBar(
    elevated: Boolean,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val elevation by animateDpAsState(
        targetValue = if (elevated) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 150)
    )

    // Animate between surface and surface color (can customize these)
    val containerColor by animateColorAsState(
        targetValue = if (elevated) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 150)
    )

    Surface(
        color = containerColor,
        shadowElevation = elevation,
        tonalElevation = elevation,
        modifier = modifier
    ) {
        TopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
// Helper modifier for scale animation on press
fun Modifier.scaleInOnPressAnimation(): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(100)
    )

    this
        .graphicsLayer(scaleX = scale, scaleY = scale)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                }
            )
        }
}
