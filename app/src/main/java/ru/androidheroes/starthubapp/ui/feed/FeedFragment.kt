package ru.androidheroes.starthubapp.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.data.MockRepository
import ru.androidheroes.starthubapp.ui.afterTextChanged
import timber.log.Timber

class FeedFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var viewModel: FeedViewModel

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Добавляем recyclerView
        events_recycler_view.layoutManager = LinearLayoutManager(context)
        events_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }

        var eventsList: List<Item>? = null
        if (viewModel.isBannerShow) {
            eventsList = listOf(

                MainCardContainer(
                    R.string.recommended,
                    MockRepository.getEvents().map {
                        EventItem(it) { movie ->
                            openEventDetails(
                                movie
                            )
                        }
                    }.toList()
                )
            )
            viewModel.isBannerShow = false
        } else {
            eventsList = listOf(
                MainCardContainer(
                    R.string.recommended,
                    MockRepository.getEvents().map {
                        EventItem(it) { movie ->
                            openEventDetails(
                                movie
                            )
                        }
                    }.toList()
                )
            )
        }

        events_recycler_view.adapter = adapter.apply { addAll(eventsList) }

        val nearYouEvents = listOf(
            MainCardContainer(
                R.string.near_you,
                MockRepository.getNearEvents()
                    .map {
                        NearEventItem(it) { movie ->
                            openEventDetails(movie)
                        }
                    }.toList()
            )
        )

        adapter.apply { addAll(nearYouEvents) }

        val newEventsList = listOf(
            MainCardContainer(
                R.string.upcoming,
                MockRepository.getEvents().shuffled()
                    .map { it -> it.apply { isInFavorite = true } }
                    .map {
                        EventItem(it) { movie ->
                            openEventDetails(movie)
                        }
                    }.toList()
            )
        )

        adapter.apply { addAll(newEventsList) }

        val myEvents = listOf(
            MainCardContainer(
                R.string.my_events,
                MockRepository.getMyEvents().shuffled()
                    .map {
                        EventItem(it) { movie ->
                            openEventDetails(movie)
                        }
                    }.toList()
            )
        )

        adapter.apply { addAll(myEvents) }
    }

    private fun openEventDetails(event: EventDetails) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, event.title)
        bundle.putInt(KEY_ID, event.id)
        findNavController().navigate(R.id.event_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        adapter.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_ID = "id"
        const val KEY_URL = "payment_url"
        const val KEY_SEARCH = "search"
    }
}
