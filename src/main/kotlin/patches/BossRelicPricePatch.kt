package patches

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.relics.AbstractRelic

@Suppress("unused")
@SpirePatch(clz = AbstractRelic::class, method = "getPrice")
object BossRelicPricePatch {
    @SpirePostfixPatch
    @JvmStatic
    fun updateBossRelicPrice(price: Int, __instance: AbstractRelic): Int {
        if (__instance.tier == AbstractRelic.RelicTier.BOSS) {
            return 650
        }
        return price
    }
}