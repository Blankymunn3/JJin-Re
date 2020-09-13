package io.kim_kong.jjin_re.features.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivitySearchBinding
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.MySuggestionProvider
import io.kim_kong.jjin_re.utils.Utils

class SearchActivity:BaseActivity() {
    val binding by binding<ActivitySearchBinding>(R.layout.activity_search)
    val viewModel by GetViewModel(SearchViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@SearchActivity)
        Utils.setIconTintDark(this@SearchActivity, true)
        binding.lifecycleOwner = this@SearchActivity
        binding.viewModel = viewModel

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY) ?.also { query ->
                SearchRecentSuggestions(this@SearchActivity, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }
}