package com.wan.noveldownloader.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wan.noveldownloader.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DownloadedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class DownloadedFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_downloaded, container, false)
    }

}