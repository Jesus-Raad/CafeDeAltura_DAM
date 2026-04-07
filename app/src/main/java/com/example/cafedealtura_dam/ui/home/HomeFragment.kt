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
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Products_coffe
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.example.cafedealtura_dam.utils.SessionManager
import androidx.navigation.fragment.findNavController
import android.view.inputmethod.EditorInfo


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
        val cardRecommended1 = view.findViewById<View>(R.id.cardRecommended1)
        val cardRecommended2 = view.findViewById<View>(R.id.cardRecommended2)
        val etSearch = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etSearch)

        ApiService.Get.getProducts(
            context = requireContext(),
            onResult = { products ->
                ProductsRepository.setProducts(products)

                val recommended = products.shuffled().take(2)

                if (recommended.isNotEmpty()) {
                    bindRecommendedProduct(
                        product = recommended[0],
                        imageView = imgRec1,
                        nameView = tvRecName1,
                        originView = tvRecOrigin1,
                        metaView = tvRecMeta1,
                        priceView = tvRecPrice1
                    )
                }

                if (recommended.size > 1) {
                    bindRecommendedProduct(
                        product = recommended[1],
                        imageView = imgRec2,
                        nameView = tvRecName2,
                        originView = tvRecOrigin2,
                        metaView = tvRecMeta2,
                        priceView = tvRecPrice2
                    )
                }
            },
            onError = { error ->
                println("ERROR API: $error")
            }
        )
        cardRecommended1.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", recommended[0].name)
                putString("origin", recommended[0].origin)
                putDouble("price", recommended[0].price)
                putDouble("rating", recommended[0].rating)
                putString("description", recommended[0].description)
                putString("image", recommended[0].imageUrl)
            }

            findNavController().navigate(R.id.productDetailFragment, bundle)
        }

        cardRecommended2.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", recommended[1].name)
                putString("origin", recommended[1].origin)
                putDouble("price", recommended[1].price)
                putDouble("rating", recommended[1].rating)
                putString("description", recommended[1].description)
                putString("image", recommended[1].imageUrl)
            }

            findNavController().navigate(R.id.productDetailFragment, bundle)
        }

        val imgOriginCostaRica = view.findViewById<ImageView>(R.id.imgOriginCostaRica)
        val imgOriginColombia = view.findViewById<ImageView>(R.id.imgOriginColombia)
        val imgOriginEtiopia = view.findViewById<ImageView>(R.id.imgOriginEtiopia)
        val imgOriginKenia = view.findViewById<ImageView>(R.id.imgOriginKenia)
        val imgOriginLaos = view.findViewById<ImageView>(R.id.imgOriginLaos)
        val cardCategoryGrano = view.findViewById<View>(R.id.cardCategoryGrano)
        val cardCategoryMolido = view.findViewById<View>(R.id.cardCategoryMolido)
        val cardCategoryEspecial = view.findViewById<View>(R.id.cardCategoryEspecial)

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

        imgOriginCostaRica.setOnClickListener {
            val bundle = Bundle().apply {
                putString("origin", "Costa Rica")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        imgOriginColombia.setOnClickListener {
            val bundle = Bundle().apply {
                putString("origin", "Colombia")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        imgOriginEtiopia.setOnClickListener {
            val bundle = Bundle().apply {
                putString("origin", "Etiopía")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        imgOriginKenia.setOnClickListener {
            val bundle = Bundle().apply {
                putString("origin", "Kenia")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        imgOriginLaos.setOnClickListener {
            val bundle = Bundle().apply {
                putString("origin", "Laos")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        cardCategoryGrano.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "En grano")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        cardCategoryMolido.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "Molido")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }

        cardCategoryEspecial.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "Especial")
            }
            findNavController().navigate(R.id.filteredProductsFragment, bundle)
        }
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = etSearch.text?.toString()?.trim().orEmpty()

                if (query.isNotEmpty()) {
                    val bundle = Bundle().apply {
                        putString("query", query)
                    }
                    findNavController().navigate(R.id.filteredProductsFragment, bundle)
                }

                true
            } else {
                false
            }
        }


        return view
    }

    private fun bindRecommendedProduct(
        product: Products_coffe,
        imageView: ImageView,
        nameView: TextView,
        originView: TextView,
        metaView: TextView,
        priceView: TextView
    )
    {
        Glide.with(requireContext())
            .load(product.img_url)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imageView)

        nameView.text = product.brand
        originView.text = product.origin
        metaView.text = product.available.toString()
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