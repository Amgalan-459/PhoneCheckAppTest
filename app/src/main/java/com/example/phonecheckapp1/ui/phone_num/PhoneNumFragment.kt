package com.example.phonecheckapp1.ui.phone_num

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.phonecheckapp1.R
import com.example.phonecheckapp1.databinding.FragmentPhoneNumberBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.launch
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhoneNumFragment : Fragment() {

    private var _binding: FragmentPhoneNumberBinding? = null

    private val binding get() = _binding!!
    private val viewModel: PhoneNumViewModel by viewModel()

    private lateinit var bottomSheetBeh: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.showMore.paintFlags = binding.showMore.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.showMore.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { loading ->
                    binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
                }
            }
        }

        setupBottomSheet()
        setUpClickerListeners()

        return root
    }

    private fun setupBottomSheet() {
        bottomSheetBeh = BottomSheetBehavior.from(binding.bottomSheet).apply {
            isHideable = true
            state = STATE_HIDDEN
            isDraggable = true
            isFitToContents = false
            expandedOffset = 100
            halfExpandedRatio = 0.4f

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_EXPANDED -> {
                            animateOverlay(show = true)
                            binding.showMore.text = "Скрыть"
                        }
                        STATE_HIDDEN -> {
                            animateOverlay(show = false)
                            binding.showMore.text = "Показать больше"
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        }

                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun animateOverlay(show: Boolean) {
        val startAlpha = if (show) -1f else 1f
        val endAlpha = if (show) 1f else -1f

        if (show) binding.overlayView.visibility = View.VISIBLE

        val animator = ObjectAnimator.ofFloat(binding.overlayView, "alpha", startAlpha,
            endAlpha).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
        }

        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!show) {
                    binding.overlayView.visibility = View.GONE
                }
            }
        })

        animator.start()
    }

    private fun setUpClickerListeners() {
        binding.fetchButton.setOnClickListener {
            if (binding.phoneInput.text.toString().trim() == ""){
                Toast.makeText(requireContext(), "Введите номер", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.showMore.visibility = View.VISIBLE

            lifecycleScope.launch {
                val error = viewModel.fetchDataPhoneInf(binding.phoneInput.text.toString().trim())
                if (error == "") {
                    if (viewModel.phoneInfo.value != null){
                        val info = viewModel.phoneInfo.value!!
                        binding.phoneInf.text = info.phone
                        binding.carrier.text = info.carrier

                        Glide.with(this@PhoneNumFragment)
                            .load("https://flagsapi.com/" +
                                    "${info.country_code}/flat/64.png")
                            .placeholder(R.drawable.flag_placeholder)
                            .error(R.drawable.flag_error)
                            .into(binding.countryImage)

                        Glide.with(this@PhoneNumFragment)
                            .load("https://flagsapi.com/" +
                                    "${info.country_code}/flat/64.png")
                            .placeholder(R.drawable.flag_placeholder)
                            .error(R.drawable.flag_error)
                            .into(binding.countryFlag)

                        binding.phoneNumber.text = StringBuilder("Номер: ")
                            .append(info.international_number)
                        binding.phoneLocal.text = StringBuilder("Локальный: ")
                            .append(info.local_number)
                        binding.phoneE164.text = StringBuilder("E164: ")
                            .append(info.e164)
                        binding.phoneRaw.text = StringBuilder("Оригинал: ")
                            .append(info.phone)
                        binding.carrierMore.text = StringBuilder("Оператор: ")
                            .append(info.carrier)
                        binding.region.text = StringBuilder("Регион: ")
                            .append(info.phone_region)
                        binding.phoneType.text = StringBuilder("Тип: ")
                            .append(info.phone_type)
                        binding.phoneValid.text = StringBuilder("Валидный: ")
                            .append(if (info.phone_valid) "Да" else "Нет")
                        binding.country.text = StringBuilder("Страна: ")
                            .append(info.country)
                        binding.countryCode.text = StringBuilder("Код страны: ")
                            .append(info.country_code)
                        binding.countryPrefix.text = StringBuilder("Префикс: +")
                            .append(info.country_prefix)
                    }
                } else {
                    Toast.makeText(requireContext(), "Ошибка запроса", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.showMore.setOnClickListener {
            bottomSheetBeh.state = STATE_EXPANDED
        }

        binding.overlayView.setOnClickListener {
            bottomSheetBeh.state = STATE_HIDDEN
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}