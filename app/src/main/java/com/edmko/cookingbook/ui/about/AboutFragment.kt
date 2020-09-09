package com.edmko.cookingbook.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edmko.cookingbook.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        policyWeb.loadUrl("https://rawcdn.githack.com/Edmko/CookingBook/e474f89a01eb54dc4c7cdd0fc610441c722e21d1/Privacy Policy.htm")
        super.onActivityCreated(savedInstanceState)
    }
}