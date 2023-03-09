package helpers

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic
import java.util.ArrayList

class RelicPools {
    companion object {
        @JvmStatic
        var commonList: ArrayList<AbstractRelic> = ArrayList()
        @JvmStatic
        var uncommonList: ArrayList<AbstractRelic> = ArrayList()
        @JvmStatic
        var rareList: ArrayList<AbstractRelic> = ArrayList()
        @JvmStatic
        var bossList: ArrayList<AbstractRelic> = ArrayList()
        @JvmStatic
        var shopList: ArrayList<AbstractRelic> = ArrayList()
        @JvmStatic
        var specialList: ArrayList<AbstractRelic> = ArrayList()

        fun getSpecialRelic(): AbstractRelic {
            var relic: AbstractRelic?
            do {
                if (specialList.isNotEmpty()){
                    relic = specialList.removeFirst()
                } else {
                    relic = null
                    break
                }
            } while (AbstractDungeon.player.hasRelic(relic?.relicId))
            if (relic == null) {
                relic = uncommonList.removeFirst()
            }
            return relic
        }
    }
}