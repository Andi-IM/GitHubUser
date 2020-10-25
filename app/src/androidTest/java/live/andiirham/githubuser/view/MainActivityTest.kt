package live.andiirham.githubuser.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import live.andiirham.githubuser.R
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    private val dummyQuery = "andi"

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertGetQuery() {
        onView(withId(R.id.search)).perform()
    }
}