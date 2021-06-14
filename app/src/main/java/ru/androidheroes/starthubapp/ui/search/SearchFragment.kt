package ru.androidheroes.starthubapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.data.MockRepository
import ru.androidheroes.starthubapp.ui.feed.EventDetails
import ru.androidheroes.starthubapp.ui.feed.EventItem
import ru.androidheroes.starthubapp.ui.feed.FeedFragment
import ru.androidheroes.starthubapp.ui.feed.FeedFragment.Companion.KEY_ID
import ru.androidheroes.starthubapp.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidheroes.starthubapp.ui.feed.FeedFragment.Companion.KEY_TITLE
import ru.androidheroes.starthubapp.ui.feed.MainCardContainer
import ru.androidheroes.uikit.search.afterTextChanged
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        search_toolbar.setText(searchTerm)


        var eventsList: List<Item>? = null
        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > FeedFragment.MIN_LENGTH) {
                eventsList = listOf(
                    MainCardContainer(
                        R.string.events_title,
                        MockRepository.findEvents(searchTerm!!)
                            .map {
                                EventItem(it) { movie ->
                                    openEventDetails(
                                        movie
                                    )
                                }
                            }.toList()
                    )
                )

                eventsList?.let {
                    events_recycler_view.adapter = adapter.apply { addAll(it) }
                }

                val pro = listOf(
                    MainCardContainer(
                        R.string.projects,
                        MockRepository.findEvents(searchTerm!!)
                            .map {
                                EventItem(it) { movie ->
                                    openEventDetails(
                                        movie
                                    )
                                }
                            }.toList()
                    )
                )

                pro?.let {
                    events_recycler_view.adapter = adapter.apply { addAll(it) }
                }
            }


        }
    }

    private fun openEventDetails(event: EventDetails) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, event.title)
        bundle.putInt(KEY_ID, event.id)
        findNavController().navigate(R.id.event_details_fragment, bundle)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
