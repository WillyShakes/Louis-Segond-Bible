package org.redesnac.lsgbible.presentation

sealed class Screen(val route:String){
    object BooksScreen:Screen("books")
    object VersesScreen:Screen("verses")
    object FavoriteVersesScreen:Screen("favorite_verses")
}
