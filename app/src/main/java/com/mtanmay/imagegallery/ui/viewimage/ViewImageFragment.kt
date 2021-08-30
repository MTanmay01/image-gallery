package com.mtanmay.imagegallery.ui.viewimage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mtanmay.imagegallery.R
import com.mtanmay.imagegallery.databinding.ViewImageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewImageFragment: Fragment(R.layout.view_image_fragment) {

    private val arguments by navArgs<ViewImageFragmentArgs>()

    private var _binding: ViewImageFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = ViewImageFragmentBinding.bind(view)

        binding.apply {

            val image = arguments.image

            Glide.with(this@ViewImageFragment)
                .load(image.url_s)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageFullscreen)
        }
    }
}