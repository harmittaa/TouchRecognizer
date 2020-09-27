package com.github.harmittaa.touchobserver.screens.game

import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebSettings.RenderPriority
import androidx.fragment.app.Fragment
import com.github.harmittaa.touchobserver.MainActivity
import com.github.harmittaa.touchobserver.databinding.ScreenGameBinding
import com.github.harmittaa.touchobserver.view.TouchWebView
import java.util.*

class GameFragment : Fragment(), TouchWebView.Listener {
    private lateinit var binding: ScreenGameBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? MainActivity)?.let {
            it.storeEvents = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenGameBinding.inflate(inflater, container, false)
        //binding.gameView.listener = this

        // Don't show an action bar or title

        // TODO
        // Don't show an action bar or title
        //requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Enable hardware acceleration

        // Enable hardware acceleration
        // TODO
/*
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
*/

        // Apply previous setting about showing status bar or not

        // Apply previous setting about showing status bar or not
        //applyFullScreen(isFullScreen())

        // Check if screen rotation is locked in settings

        // TODO
        // Check if screen rotation is locked in settings
/*
        var isOrientationEnabled = false
        try {
            isOrientationEnabled = Settings.System.getInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION
            ) == 1
        } catch (e: SettingNotFoundException) {
            Log.d(MainActivity.MAIN_ACTIVITY_TAG, "Settings could not be loaded")
        }


        // If rotation isn't locked and it's a LARGE screen then add orientation changes based on sensor

        // If rotation isn't locked and it's a LARGE screen then add orientation changes based on sensor
        val screenLayout = (resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
        if ((screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE
                    || screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            && isOrientationEnabled
        ) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        }

        setContentView(R.layout.activity_main)



        val changeLog: DialogChangeLog = DialogChangeLog.newInstance(this)
        if (changeLog.isFirstRun()) {
            changeLog.getLogDialog().show()
        }

        // Load webview with game

        // Load webview with game

 */
        val settings: WebSettings = binding.gameView.getSettings()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setRenderPriority(RenderPriority.HIGH)
        settings.databasePath = requireContext().filesDir.parentFile.path + "/databases"

        // If there is a previous instance restore it in the webview

        // If there is a previous instance restore it in the webview
        if (savedInstanceState != null) {
            binding.gameView.restoreState(savedInstanceState)
        } else {
            // Load webview with current Locale language
            binding.gameView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().language)
        }

       // Toast.makeText(getApplication(), R.string.toggle_fullscreen, Toast.LENGTH_SHORT).show()
        // Set fullscreen toggle on webview LongClick
        // Set fullscreen toggle on webview LongClick
/*
        binding.gameView.setOnTouchListener(OnTouchListener { v: View?, event: MotionEvent ->
            // Implement a long touch action by comparing
            // time between action up and action down
            val currentTime = System.currentTimeMillis()
            if (event.action == MotionEvent.ACTION_UP
                && Math.abs(currentTime - mLastTouch) > MainActivity.mTouchThreshold
            ) {
                val toggledFullScreen = !isFullScreen()
                saveFullScreen(toggledFullScreen)
                applyFullScreen(toggledFullScreen)
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                mLastTouch = currentTime
            }
            false
        })

        pressBackToast = Toast.makeText(
            getApplicationContext(), R.string.press_back_again_to_exit,
            Toast.LENGTH_SHORT
        )

*/


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.gameView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().language)
    }

    override fun onTouchEvent(ev: MotionEvent) {
        (activity as MainActivity).onTouchEvent(ev)
    }

    // TODO
/*
    fun onSaveInstanceState(outState: Bundle?) {
        binding.gameView.saveState(outState)
    }
*/

    /**
     * Saves the full screen setting in the SharedPreferences
     *
     * @param isFullScreen boolean value
     */
/*
    private fun saveFullScreen(isFullScreen: Boolean) {
        // save in preferences
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean(MainActivity.IS_FULLSCREEN_PREF, isFullScreen)
        editor.apply()
    }

    private fun isFullScreen(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            MainActivity.IS_FULLSCREEN_PREF,
            true
        )
    }
*/

    /**
     * Toggles the activity's fullscreen mode by setting the corresponding window flag
     *
     * @param isFullScreen boolean value
     */
/*
    private fun applyFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
*/

    /**
     * Prevents app from closing on pressing back button accidentally.
     * mBackPressThreshold specifies the maximum delay (ms) between two consecutive backpress to
     * quit the app.
     */
/*
    fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - mLastBackPress) > MainActivity.mBackPressThreshold) {
            pressBackToast.show()
            mLastBackPress = currentTime
        } else {
            pressBackToast.cancel()
            super.onBackPressed()
        }
    }
*/


}