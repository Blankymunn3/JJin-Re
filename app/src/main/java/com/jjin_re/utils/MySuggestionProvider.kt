package com.jjin_re.utils

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.jjin_re.utils.MySuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}
