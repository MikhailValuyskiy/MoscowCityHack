package ru.androidheroes.starthubapp.ui.networking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.networking_fragment.*
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.data.MockRepository
import ru.androidheroes.starthubapp.ui.event_details.PersonItem
import ru.androidheroes.starthubapp.ui.feed.EventDetails
import ru.androidheroes.starthubapp.ui.feed.ExpertArticleItem
import ru.androidheroes.starthubapp.ui.feed.FeedFragment
import ru.androidheroes.starthubapp.ui.feed.MainCardContainer
import ru.androidheroes.starthubapp.ui.feed.Person
import ru.androidheroes.starthubapp.ui.feed.ProjectItem

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NetworkingFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        return inflater.inflate(R.layout.networking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Добавляем recyclerView
        networking_recycler_view.layoutManager = LinearLayoutManager(context)
        networking_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val nearYouEvents = listOf(
            MainCardContainer(
                R.string.projects,
                MockRepository.getProjects()
                    .map {
                        ProjectItem(it) { project ->
                            openProjectDetails(it)
                        }
                    }.toList()
            )
        )

        adapter.apply { addAll(nearYouEvents) }

        var contentList: List<Item>? = null

        contentList = listOf(
            MainCardContainer(
                R.string.mentors,
                MockRepository.getPeople().map {
                    PersonItem(it) {
                        openPersonDetails(it)
                    }
                }.toList()
            )
        )


        networking_recycler_view.adapter = adapter.apply { addAll(contentList) }

        val newContentList = listOf(
            MainCardContainer(
                R.string.experts,
                MockRepository.getEvents().shuffled()
                    .map { it -> it.apply { isInFavorite = true } }
                    .map {
                        ExpertArticleItem(it) { movie ->
                        }
                    }.toList()
            )
        )

        adapter.apply { addAll(newContentList) }
    }

    private fun openPersonDetails(p: Person) {
        val bundle = Bundle()
        findNavController().navigate(R.id.stories_fragment, bundle, options)
    }

    private fun openProjectDetails(event: EventDetails) {
        val bundle = Bundle()
        bundle.putString(FeedFragment.KEY_TITLE, event.title)
        bundle.putInt(FeedFragment.KEY_ID, event.id)
        findNavController().navigate(R.id.project_details_fragment, bundle, options)
    }


    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NetworkingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
