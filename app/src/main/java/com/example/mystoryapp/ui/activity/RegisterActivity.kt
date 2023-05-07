package com.example.mystoryapp.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.R
import com.example.mystoryapp.data.network.Result
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.di.ViewModelFactory
import com.example.mystoryapp.ui.view.EditTextEmail
import com.example.mystoryapp.ui.view.EditTextName
import com.example.mystoryapp.ui.view.EditTextPassword
import com.example.mystoryapp.ui.view.MyButton
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var myButton: MyButton
    private lateinit var myEtEmail: EditTextEmail
    private lateinit var myEtPassword: EditTextPassword
    private lateinit var myEtName: EditTextName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        supportActionBar?.hide()
    }

    private fun setMyButtonEnabled() {
        myButton = binding.btnRegister
        myButton.isEnabled = !binding.edRegisterNama.text.isNullOrEmpty() && !binding.edRegisterEmail.text.isNullOrEmpty() && !binding.edRegisterPassword.text.isNullOrEmpty() && binding.edRegisterPassword.error.isNullOrEmpty()
    }

    private fun setupAction() {
        myEtName = binding.edRegisterNama
        myEtEmail = binding.edRegisterEmail
        myEtPassword = binding.edRegisterPassword

        binding.progressBar.visibility = View.GONE

        myEtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                setMyButtonEnabled()
            }
        })

        myEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                setMyButtonEnabled()
            }
        })

        myEtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                setMyButtonEnabled()
            }
        })

        binding.apply {
            btnRegister.setOnClickListener {
                lifecycleScope.launch {
                    registerViewModel.register(
                        edRegisterNama.text.toString(),
                        edRegisterEmail.text.toString(),
                        edRegisterPassword.text.toString(),
                    ).observe(this@RegisterActivity) {
                        if (it != null) {
                            when (it) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        resources.getString(R.string.register_success),
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        resources.getString(R.string.register_error),
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
            login.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(1000)
        val sambutan = ObjectAnimator.ofFloat(binding.tvSambutan, View.ALPHA, 1f).setDuration(1000)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(1000)
        val etName = ObjectAnimator.ofFloat(binding.edRegisterNama, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(1000)
        val etEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(1000)
        val etPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(1000)
        val registerBtn = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(1000)
        val haveAccount = ObjectAnimator.ofFloat(binding.haveAccount, View.ALPHA, 1f).setDuration(1000)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(title, sambutan, name, etName, email, etEmail, password, etPassword, registerBtn, haveAccount, login)
            startDelay = 1000
        }.start()
    }
}
