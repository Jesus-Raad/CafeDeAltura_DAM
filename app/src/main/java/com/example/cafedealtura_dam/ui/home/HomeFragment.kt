package com.example.cafedealtura_dam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.ui.products.ProductData
import com.example.cafedealtura_dam.ui.products.ProductUiModel
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.example.cafedealtura_dam.utils.SessionManager

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

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
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.applyTopInsets()

        val sessionManager = SessionManager(requireContext())
        val nombre = sessionManager.getUserName()

        val tvSaludo = view.findViewById<TextView>(R.id.tvSaludo)
        tvSaludo.text = "Hola, $nombre ☕"

        val recommended = ProductData.products.shuffled().take(2)

        val imgRec1 = view.findViewById<ImageView>(R.id.imgRec1)
        val tvRecName1 = view.findViewById<TextView>(R.id.tvRecName1)
        val tvRecOrigin1 = view.findViewById<TextView>(R.id.tvRecOrigin1)
        val tvRecMeta1 = view.findViewById<TextView>(R.id.tvRecMeta1)
        val tvRecPrice1 = view.findViewById<TextView>(R.id.tvRecPrice1)

        val imgRec2 = view.findViewById<ImageView>(R.id.imgRec2)
        val tvRecName2 = view.findViewById<TextView>(R.id.tvRecName2)
        val tvRecOrigin2 = view.findViewById<TextView>(R.id.tvRecOrigin2)
        val tvRecMeta2 = view.findViewById<TextView>(R.id.tvRecMeta2)
        val tvRecPrice2 = view.findViewById<TextView>(R.id.tvRecPrice2)

        bindRecommendedProduct(
            product = recommended[0],
            imageView = imgRec1,
            nameView = tvRecName1,
            originView = tvRecOrigin1,
            metaView = tvRecMeta1,
            priceView = tvRecPrice1
        )

        bindRecommendedProduct(
            product = recommended[1],
            imageView = imgRec2,
            nameView = tvRecName2,
            originView = tvRecOrigin2,
            metaView = tvRecMeta2,
            priceView = tvRecPrice2
        )

        val imgOriginCostaRica = view.findViewById<ImageView>(R.id.imgOriginCostaRica)
        val imgOriginColombia = view.findViewById<ImageView>(R.id.imgOriginColombia)
        val imgOriginEtiopia = view.findViewById<ImageView>(R.id.imgOriginEtiopia)
        val imgOriginKenia = view.findViewById<ImageView>(R.id.imgOriginKenia)
        val imgOriginLaos = view.findViewById<ImageView>(R.id.imgOriginLaos)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_costarica_urioc7.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginCostaRica)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_colombia_omygqz.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginColombia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_etiopia_ayx2cd.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginEtiopia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_kenia_lshnti.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginKenia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_laos_g9zymj.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginLaos)

        return view
    }

    private fun bindRecommendedProduct(
        product: ProductUiModel,
        imageView: ImageView,
        nameView: TextView,
        originView: TextView,
        metaView: TextView,
        priceView: TextView
    ) {
        Glide.with(requireContext())
            .load(product.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imageView)

        nameView.text = product.name
        originView.text = product.origin
        metaView.text = product.rating.toString()
        priceView.text = String.format("$%.2f", product.price)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}