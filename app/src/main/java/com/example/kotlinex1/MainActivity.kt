package com.example.kotlinex1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinex1.databinding.ActivityMainBinding
import com.example.kotlinex1.databinding.DialogConfirmationBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSeekBar()
        setupReserveButton()
        setupVeganFilter()
        setupTimeSpinner()
    }

    private fun setupSeekBar() {
        binding.tvSeatsLabel.text = getString(R.string.seats_label, binding.sbSeats.progress)
        binding.sbSeats.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvSeatsLabel.text = getString(R.string.seats_label, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupVeganFilter() {
        binding.cbVegan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llBurger.visibility = View.GONE
                binding.llPizza.visibility = View.GONE
                binding.llBowl.visibility = View.VISIBLE
                binding.llSalad.visibility = View.VISIBLE
            } else {
                binding.llBurger.visibility = View.VISIBLE
                binding.llPizza.visibility = View.VISIBLE
                binding.llBowl.visibility = View.VISIBLE
                binding.llSalad.visibility = View.VISIBLE
            }
        }
    }

    private fun setupTimeSpinner() {
        val times = mutableListOf<String>()
        
        for (hour in 10..21) {
            for (minute in listOf(0, 15, 30, 45)) {
                times.add(String.format("%02d:%02d", hour, minute))
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTime.adapter = adapter
    }

    private fun setupReserveButton() {
        binding.btnReserve.setOnClickListener {
            val nameInput = binding.etName.text.toString()
            if (nameInput.isBlank()) {
                binding.etName.error = "Name is required"
                return@setOnClickListener
            }

            val scaleX = ObjectAnimator.ofFloat(it, "scaleX", 1f, 1.2f, 1f)
            val scaleY = ObjectAnimator.ofFloat(it, "scaleY", 1f, 1.2f, 1f)
            val alpha = ObjectAnimator.ofFloat(it, "alpha", 1f, 0.5f, 1f)

            AnimatorSet().apply {
                playTogether(scaleX, scaleY, alpha)
                duration = 300
                start()
            }

            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val dialogBinding = DialogConfirmationBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        val name = binding.etName.text.toString()
        val seats = binding.sbSeats.progress
        val payment = binding.spPayment.selectedItem.toString()
        val time = binding.spTime.selectedItem.toString()
        
        val day = binding.dpReservation.dayOfMonth
        val month = binding.dpReservation.month + 1
        val year = binding.dpReservation.year
        val date = String.format("%02d/%02d/%04d", day, month, year)
        
        val isVegan = if (binding.cbVegan.isChecked) getString(R.string.yes) else getString(R.string.no)

        dialogBinding.tvSummaryName.text = getString(R.string.summary_name, name)
        dialogBinding.tvSummarySeats.text = getString(R.string.summary_seats, seats)
        dialogBinding.tvSummaryPayment.text = getString(R.string.summary_payment, payment)
        dialogBinding.tvSummaryTime.text = getString(R.string.summary_time, time)
        dialogBinding.tvSummaryDate.text = getString(R.string.summary_date, date)
        dialogBinding.tvSummaryVegan.text = getString(R.string.summary_vegan, isVegan)

        dialogBinding.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}