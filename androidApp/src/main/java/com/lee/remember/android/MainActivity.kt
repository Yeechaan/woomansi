package com.lee.remember.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.io.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Napier.base(DebugAntilog())
        firebaseAnalytics = Firebase.analytics

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // MainApp
                    val controller = rememberNavController()
                    MainApp(controller)

                    controller.addOnDestinationChangedListener { _, destination, _ ->
                        val params = Bundle()
                        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, destination.route)
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
                    }
                }
            }
        }
    }
}

data class Contract(
    val id: String,
    val name: String,
    val number: String,
    var isChecked: Boolean = false,
) : Serializable

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val controller = rememberNavController()
        MainApp(controller)
    }
}
