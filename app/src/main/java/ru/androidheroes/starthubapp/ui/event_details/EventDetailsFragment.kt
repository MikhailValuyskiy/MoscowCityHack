package ru.androidheroes.starthubapp.ui.event_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.event_details_fragment.buy_button
import kotlinx.android.synthetic.main.event_details_fragment.description_tv
import kotlinx.android.synthetic.main.event_details_fragment.event_address_value
import kotlinx.android.synthetic.main.event_details_fragment.event_category_value
import kotlinx.android.synthetic.main.event_details_fragment.event_date_value
import kotlinx.android.synthetic.main.event_details_fragment.image_detail_preview
import kotlinx.android.synthetic.main.event_details_fragment.people_recycler_view
import kotlinx.android.synthetic.main.event_details_fragment.title_tv
import kotlinx.android.synthetic.main.fragment_project_details.*
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.data.MockRepository
import ru.androidheroes.starthubapp.ui.feed.FeedFragment.Companion.KEY_ID
import ru.androidheroes.starthubapp.ui.feed.FeedFragment.Companion.KEY_URL
import ru.androidheroes.starthubapp.ui.feed.NotificationHelper
import ru.androidheroes.starthubapp.ui.feed.Person
import ru.androidheroes.starthubapp.ui.feed.WebViewActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyViewModelFactory(context: Context) :
    ViewModelProvider.Factory {
    private val mContext: Context
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(mContext) as T
    }

    init {
        mContext = context
    }
}

class MainActivityViewModel(val context: Context) : ViewModel() {

    val statusLiveData = MutableLiveData<String>()


    fun showNotification(billUrl: String) {

        val args = Bundle()
        args.putString(KEY_URL, billUrl);

        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtras(args)

        NotificationHelper.createSampleDataNotification(
            context, "Новое мероприятие!", "Подробное описание", "", true,
            intent
        )
    }


}

class EventDetailsFragment : Fragment() {

    lateinit var mainActivityViewModel: MainActivityViewModel

    private var param1: String? = null
    private var param2: String? = null

    private var eventId: Int? = null


    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            eventId = it.getInt(KEY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.event_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewModelFactory = MyViewModelFactory(requireContext())
        mainActivityViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(MainActivityViewModel::class.java)

        val stateObserver = Observer<String> { newState ->

        }

        val event = MockRepository.getEvents().find { it.id == eventId }

        title_tv.text = event?.title
        description_tv.text = event?.desc
        event_address_value.text = event?.address
        event_category_value.text = event?.categoryName
        event_date_value.text = event?.date

        mainActivityViewModel.statusLiveData.observe(requireActivity(), stateObserver)

        val actors = listOf<Person>()

        val actorList = MockRepository.getPeople().shuffled().map {
            PersonItem(it) {

            }
        }.toList()
        people_recycler_view.adapter = adapter.apply { addAll(actorList) }

        Picasso.get()
            .load(event?.imageUrl)
            .into(image_detail_preview)


        val snackbar = Snackbar.make(
            view,
            "Заявка на участие отправлена!",
            Snackbar.LENGTH_LONG
        );

        buy_button.setOnClickListener {
            snackbar.show()
        }
    }

    override fun onResume() {
        super.onResume()
    }


    private fun openWebView(url: String) {

        val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        ContextCompat.startActivity(requireContext(), browserIntent, null)
    }

    fun saveKey(key: String, value: String) {
        val sharedPref = activity?.getSharedPreferences(
            "my_preffs", Context.MODE_PRIVATE
        )
        with(sharedPref!!.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getKey(key: String): String? {
        val sharedPref = activity?.getSharedPreferences(
            "my_preffs", Context.MODE_PRIVATE
        )

        return sharedPref?.getString(key, "empty")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EventDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}
