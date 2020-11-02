package jay.kotlin.projects.retirementcalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(application, "530fcb95-6290-43b0-81fd-aa6820f9ed79",
                Analytics::class.java, Crashes::class.java)

        calculateButton.setOnClickListener {
            //throw Exception("Something went wrong")
            //Crashes.generateTestCrash()
            val interestRate = interestEditText.text.toString().toFloat()
            val currentAge = ageEditText.text.toString().toInt()
            val retirementAge = retirementEditText.text.toString().toInt()

            val monthly = monthlySavingsEditText.text.toString().toFloat()
            val current = currentEditText.text.toString().toFloat()

            val properties: HashMap<String, String> = HashMap<String, String>()
            properties["interest _rate"] = interestRate.toString()
            properties["current_age"] = currentAge.toString()
            properties["retirement_age"] = retirementAge.toString()
            properties["monthly_savings"] = monthly.toString()
            properties["current_savings"] = current.toString()


            try{
                if(interestRate <= 0){
                    Analytics.trackEvent("Interest rate cannot be less than 0", properties)
                    Toast.makeText(this, "Interest rate cannot be less than 0", Toast.LENGTH_LONG).show()
                }
                else if(retirementAge <= currentAge){
                    Analytics.trackEvent("Current age cannot be greater than retirement age", properties)
                    Toast.makeText(this, "Current age cannot be greater than retirement age", Toast.LENGTH_LONG).show()
                }
            }
            catch (e:Exception){
                e.printStackTrace()
                Analytics.trackEvent(e.message)
            }

        }
    }
}