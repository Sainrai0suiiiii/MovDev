package com.example.c36b.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.c36b.view.pages.HomeScreen
import com.example.c36b.view.pages.SearchScreen
import com.example.c36b.view.pages.BookmarkScreen
import com.example.c36b.view.pages.ProfileScreen

// Book Review Navigation ViewModel
class BookReviewNavigationViewModel : ViewModel() {
    private val _bookmarkCount = MutableStateFlow(0)
    val bookmarkCount: StateFlow<Int> = _bookmarkCount.asStateFlow()

    private val _favoritesCount = MutableStateFlow(0)
    val favoritesCount: StateFlow<Int> = _favoritesCount.asStateFlow()

    private val _totalReviews = MutableStateFlow(0)
    val totalReviews: StateFlow<Int> = _totalReviews.asStateFlow()

    fun addToBookmarks(itemCount: Int = 1) {
        _bookmarkCount.value += itemCount
    }

    fun removeFromBookmarks(itemCount: Int = 1) {
        _bookmarkCount.value = maxOf(0, _bookmarkCount.value - itemCount)
    }

    fun updateBookmarkCount(count: Int) {
        _bookmarkCount.value = maxOf(0, count)
    }

    fun addToFavorites(itemCount: Int = 1) {
        _favoritesCount.value += itemCount
    }

    fun removeFromFavorites(itemCount: Int = 1) {
        _favoritesCount.value = maxOf(0, _favoritesCount.value - itemCount)
    }

    fun updateFavoritesCount(count: Int) {
        _favoritesCount.value = maxOf(0, count)
    }

    fun updateTotalReviews(count: Int) {
        _totalReviews.value = count
    }

    fun clearBookmarks() {
        _bookmarkCount.value = 0
    }
}

// Enhanced navigation item for book review app
data class BookReviewNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNotification: Boolean = false,
    val badgeCount: Int? = null
)

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Black.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color.Black.toArgb())
        )
        setContent {
            BookReviewNavigationBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookReviewNavigationBody(
    viewModel: BookReviewNavigationViewModel = viewModel()
) {
    val hapticFeedback = LocalHapticFeedback.current
    val bookmarkCount by viewModel.bookmarkCount.collectAsState()
    val favoritesCount by viewModel.favoritesCount.collectAsState()
    val totalReviews by viewModel.totalReviews.collectAsState()

    val bookReviewNavItems = listOf(
        BookReviewNavItem(
            route = "home",
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BookReviewNavItem(
            route = "search",
            label = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search
        ),
        BookReviewNavItem(
            route = "bookmarks",
            label = "Bookmarks",
            selectedIcon = Icons.Filled.FavoriteBorder,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            badgeCount = if (bookmarkCount > 0) bookmarkCount else null
        ),
        BookReviewNavItem(
            route = "favorites",
            label = "Favorites",
            selectedIcon = Icons.Filled.FavoriteBorder,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            badgeCount = if (favoritesCount > 0) favoritesCount else null
        ),
        BookReviewNavItem(
            route = "profile",
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bookReviewNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null && item.badgeCount > 0) {
                                        Badge {
                                            Text(
                                                text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString(),
                                                modifier = Modifier.semantics {
                                                    contentDescription = "${item.badgeCount} items in ${item.label}"
                                                }
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedIndex == index) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = "${item.label} tab"
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.label,
                                modifier = Modifier.semantics {
                                    contentDescription = "${item.label} navigation tab"
                                }
                            )
                        },
                        selected = selectedIndex == index,
                        onClick = {
                            if (selectedIndex != index) {
                                selectedIndex = index
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            // Smooth animated transitions between book review screens
            AnimatedContent(
                targetState = selectedIndex,
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300)
                    ).togetherWith(
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300)
                        )
                    )
                },
                label = "BookReviewNavigationAnimation"
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> HomeScreen(
                        onNavigateToSearch = { selectedIndex = 1 },
                        onNavigateToBookmarks = { selectedIndex = 2 },
                        onAddToBookmarks = { itemCount ->
                            viewModel.addToBookmarks(itemCount)
                        },
                        onAddToFavorites = { itemCount ->
                            viewModel.addToFavorites(itemCount)
                        },
                        bookmarkCount = bookmarkCount,
                        favoritesCount = favoritesCount
                    )
                    1 -> SearchScreen(
                        onNavigateToBookmarks = { selectedIndex = 2 },
                        onNavigateToHome = { selectedIndex = 0 },
                        onAddToBookmarks = { itemCount ->
                            viewModel.addToBookmarks(itemCount)
                        },
                        onAddToFavorites = { itemCount ->
                            viewModel.addToFavorites(itemCount)
                        },
                        bookmarkCount = bookmarkCount,
                        favoritesCount = favoritesCount
                    )
                    2 -> BookmarkScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                    3 -> ProfileScreen(
                        onNavigateToHome = { selectedIndex = 0 },
                        onNavigateToBookmarks = { selectedIndex = 2 },
                        onNavigateToFavorites = { selectedIndex = 3 },
                        bookmarkCount = bookmarkCount,
                        favoritesCount = favoritesCount,
                        totalReviews = totalReviews
                    )
                }
            }
        }
    }
}

// Enhanced book review screen composables with proper callbacks
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToBookmarks: () -> Unit = {},
    onAddToBookmarks: (Int) -> Unit = {},
    onAddToFavorites: (Int) -> Unit = {},
    bookmarkCount: Int = 0,
    favoritesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // Your existing HomeScreen implementation
    // Show featured book reviews, categories, trending books, etc.
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Book Reviews - Home Screen\nBookmarks: $bookmarkCount items\nFavorites: $favoritesCount items")
    }
}

@Composable
fun SearchScreen(
    onNavigateToBookmarks: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onAddToBookmarks: (Int) -> Unit = {},
    onAddToFavorites: (Int) -> Unit = {},
    bookmarkCount: Int = 0,
    favoritesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // Your existing SearchScreen implementation
    // Search books, filter by genre, author, rating, etc.
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Search Books\nBookmarks: $bookmarkCount items\nFavorites: $favoritesCount items")
    }
}

@Composable
fun BookmarkScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onUpdateBookmarkCount: (Int) -> Unit = {},
    onClearBookmarks: () -> Unit = {},
    bookmarkCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // Your existing BookmarksScreen implementation
    // Show bookmarked reviews, remove bookmarks
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Bookmarked Reviews\n$bookmarkCount bookmarks")
    }
}

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBookmarks: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    bookmarkCount: Int = 0,
    favoritesCount: Int = 0,
    totalReviews: Int = 0,
    modifier: Modifier = Modifier
) {
    // Your existing ProfileScreen implementation
    // User profile, review history, settings, preferences
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("User Profile\nBookmarks: $bookmarkCount items\nFavorites: $favoritesCount\nTotal Reviews: $totalReviews")
    }
}

@Preview(showBackground = true)
@Composable
fun BookReviewNavPreview() {
    BookReviewNavigationBody()
}

@Preview(showBackground = true, name = "Book Review Navigation Dark")
@Composable
fun BookReviewNavPreviewDark() {
    BookReviewNavigationBody()
}