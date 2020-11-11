package jay.kotlin.projects.retirementcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.utils.async.AppCenterConsumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(application, "313461b4-f1dd-44cd-9165-a21b8487da0e",
                Analytics::class.java, Crashes::class.java)

        val future = Crashes.hasCrashedInLastSession()
        future.thenAccept(AppCenterConsumer {
            if(it){
                Toast.makeText(this, "Oops! Sorry about that crash!", Toast.LENGTH_LONG).show()
            }
        })

        calculateButton.setOnClickListener {
            // Crashes.generateTestCrash()
            try {
                val interestRate = interestEditText.text.toString().toFloat()
                val currentAge = ageEditText.text.toString().toInt()
                val retirementAge = retirementEditText.text.toString().toInt()
                val monthly = monthlySavingsEditText.text.toString().toFloat()
                val current = currentEditText.text.toString().toFloat()

                val properties:HashMap<String, String> = HashMap<String, String>()
                properties["interest_rate"] = interestRate.toString()
                properties["current_age"] = currentAge.toString()
                properties["retirement_age"] = retirementAge.toString()
                properties["monthly_savings"] = monthly.toString()
                properties["current_savings"] = current.toString()

                if (interestRate <= 0) {
                    Analytics.trackEvent("wrong_interest_rate", properties)
                }
                if (retirementAge <= currentAge) {
                    Analytics.trackEvent("wrong_age", properties)
                }

                val futureSavings = calculateRetirement(interestRate, current, monthly, (retirementAge - currentAge)*12)

                resultTextView.text = "At the current rate of $interestRate%, saving \$$monthly a month you will have \$${String.format("%f", futureSavings)} by $retirementAge."
            } catch(ex: Exception){
                Analytics.trackEvent(ex.message)
            }
        }
    }

    fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float {
        var futureSavings = currentSavings * (1+(interestRate/100/12)).pow(numMonths)

        for (i in 1..numMonths) {
            futureSavings += monthly * (1+(interestRate/100/12)).pow(i)
        }

        return  futureSavings
    }
}