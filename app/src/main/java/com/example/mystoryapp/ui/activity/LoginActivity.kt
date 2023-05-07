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
import com.example.mystoryapp.data.model.LoginResponse
import com.example.mystoryapp.data.model.StorySession
import com.example.mystoryapp.data.network.Result
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.di.ViewModelFactory
import com.example.mystoryapp.ui.view.EditTextEmail
import com.example.mystoryapp.ui.view.EditTextPassword
import com.example.mystoryapp.ui.view.MyButton
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var myButton: MyButton
    private lateinit var myEtEmail: EditTextEmail
    private lateinit var myEtPassword: EditTextPassword

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        val login = StorySession(this).isLoggedIn()
        if (login) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setMyButtonEnabled() {
        myButton = binding.btnLogin
        myButton.isEnabled = !binding.edLoginEmail.text.isNullOrEmpty() && !binding.edLoginPassword.text.isNullOrEmpty() && binding.edLoginPassword.error.isNullOrEmpty()
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

    private fun setupAction() {
        myEtEmail = binding.edLoginEmail
        myEtPassword = binding.edLoginPassword

        binding.progressBar.visibility = View.GONE

        myEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        myEtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        val pref = StorySession(this)
        binding.apply {
            btnLogin.setOnClickListener {
                lifecycleScope.launch {
                    loginViewModel.login(myEtEmail.text.toString(), myEtPassword.text.toString()).observe(this@LoginActivity) {
                        if (it != null) {
                            when (it) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    pref.saveUser(
                                        LoginResponse(
                                            name = it.data?.name,
                                            token = it.data?.token,
                                            isLogin = true,
                                        ),
                                    )

                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()

                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_success),
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_error),
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }

            register.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(1000)
        val sambutan = ObjectAnimator.ofFloat(binding.tvSambutan, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(1000)
        val etEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(1000)
        val edPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(1000)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val dontHaveAccount = ObjectAnimator.ofFloat(binding.dontHaveAccount, View.ALPHA, 1f).setDuration(1000)
        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(title, sambutan, email, etEmail, password, edPassword, login, dontHaveAccount, register)
            startDelay = 1000
        }.start()
    }
}
