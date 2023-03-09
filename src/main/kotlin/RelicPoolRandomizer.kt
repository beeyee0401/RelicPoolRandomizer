import basemod.BaseMod
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import config.Config

@Suppress("unused")
@SpireInitializer
class RelicPoolRandomizer {
    companion object Statics {
        lateinit var config: Config

        @JvmStatic
        fun initialize() {
            BaseMod.subscribe(RelicPoolRandomizerInit())
            config = Config()
        }
    }
}