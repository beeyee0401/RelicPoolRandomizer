package config

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import java.io.IOException

class Config {
    private lateinit var spireConfig: SpireConfig

    init {
        try {
            spireConfig = SpireConfig("RelicPoolRandomizer", "config")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setBooleanKey(key: String, enabled: Boolean) {
        spireConfig.setBool(key, enabled)
        try {
            spireConfig.save()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getBooleanKey(key: String): Boolean {
        return spireConfig.getBool(key)
    }

    companion object {
        const val shouldRandomizeSpecialRelicsKey = "shouldRandomizeSpecialRelics"
    }
}