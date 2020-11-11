package jay.kotlin.projects.retirementcalculator


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.microsoft.appcenter.espresso.Factory
import com.microsoft.appcenter.espresso.ReportHelper
import org.junit.After

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var reportHelper: ReportHelper = Factory.getReportHelper()

    @Test
    fun mainActivityTest() {
        val appCompatEditText = onView(
                allOf(withId(R.id.monthlySavingsEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("1000"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.interestEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("5"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.ageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("30"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
                allOf(withId(R.id.retirementEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()))
        appCompatEditText4.perform(replaceText("60"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.currentEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("10000"), closeSoftKeyboard())

        val materialButton = onView(
                allOf(withId(R.id.calculateButton), withText("Calculate"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        materialButton.perform(click())

        val textView = onView(
                allOf(withId(R.id.resultTextView), withText("At the current rate of 5.0%, saving $1000.0"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()))
        textView.check(matches(withText("At the current rate of 5.0%, saving $1000.0")))
    }

    @After
    fun tearDown(){
        reportHelper.label("Finishing test")
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
