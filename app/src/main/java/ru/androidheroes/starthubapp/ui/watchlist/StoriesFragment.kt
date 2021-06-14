package ru.androidheroes.starthubapp.ui.watchlist

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teresaholfeld.stories.StoriesProgressView
import kotlinx.android.synthetic.main.fragment_stories.*
import ru.androidheroes.starthubapp.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class StoriesFragment : Fragment() , StoriesProgressView.StoriesListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var storiesProgressView: StoriesProgressView? = null
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storiesProgressView?.setStoriesCount(2) // <- set stories
        storiesProgressView?.setStoryDuration(500) // <- set a story duration
        storiesProgressView?.setStoriesListener(this) // <- set listener
        counter = 2
        storiesProgressView?.startStories(counter) // <- start progress

        story1.setOnClickListener {
            story2.visibility = View.VISIBLE
        }

        story2.setOnClickListener {
            story3.visibility = View.VISIBLE
            message_button.visibility = View.VISIBLE
        }
    }

    override fun onNext() {
        Toast.makeText(requireContext(), "onNext", Toast.LENGTH_SHORT).show()
    }

    override fun onPrev() {
        // Called when skipping to the previous screen
        Toast.makeText(requireContext(), "onPrev", Toast.LENGTH_SHORT).show()
    }

    override fun onComplete() {
        Toast.makeText(requireContext(), "onComplete", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        // Very important !
        storiesProgressView?.destroy()
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoriesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}