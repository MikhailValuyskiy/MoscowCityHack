package ru.androidheroes.starthubapp.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.androidheroes.starthubapp.ui.watchlist.*

class ProfileAdapter(fragment: Fragment, private val itemsCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                RegistrationFragment.newInstance("", "")
            }
            1 -> {
                MyProjectsList.newInstance("", "")
            }
            2 -> {
                MyTeamFragment.newInstance("", "")
            }
            3 -> {
                TransactionFragment.newInstance("", "")
            }
            4 -> {
                AnalyticsFragment.newInstance("", "")
            }
            else -> WatchlistFragment.newInstance()
        }
    }
}
