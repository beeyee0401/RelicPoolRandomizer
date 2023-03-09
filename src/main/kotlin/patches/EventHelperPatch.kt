package patches

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.helpers.EventHelper

@SpirePatch(clz = EventHelper::class, method = "getEvent")
object EventHelperPatch {
    var eventName = ""

    @JvmStatic
    @SpirePrefixPatch
    fun patch(key: String) {
        eventName = key
    }
}