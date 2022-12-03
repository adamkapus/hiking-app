package com.adamkapus.hikingapp.ui.map

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adamkapus.hikingapp.databinding.FragmentFlowerImageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class FlowerImageFragment : AppCompatDialogFragment() {
    private lateinit var binding: FragmentFlowerImageBinding

    private var displayName: String? = null
    private var imageURI: String? = null

    val args: FlowerImageFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
        /*arguments?.let {
            displayName = it.getString(ARG_PARAM1)
            imageURI = it.getString(ARG_PARAM2)
        }*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = FragmentFlowerImageBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        displayName = args.flowerDisplayName
        imageURI = args.imageUrl


        binding.flowerImageName.text = displayName

        //Glide-al betöltjük a virág képét, a töltés közben egy progressbart mutatunk
        if (imageURI != null) {
            context?.let {
                binding.imgLoadProgressbar.visibility = View.VISIBLE
                Glide.with(it).load(imageURI).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imgLoadProgressbar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imgLoadProgressbar.visibility = View.GONE
                        return false
                    }
                }).into(binding.flowerImage)
            }
        }

        binding.flowerCloseBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}