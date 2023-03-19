package eg.gov.iti.jets.weatherapp

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import eg.gov.iti.jets.weatherapp.databinding.ActivitySpalshScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySpalshScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setUpLottieAnimationView()

        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun setUpLottieAnimationView() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation: ValueAnimator ->
            binding.splashAnimationView.progress = animation.animatedValue as Float
        }
        animator.start()
    }
}