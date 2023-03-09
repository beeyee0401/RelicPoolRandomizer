package patches

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import helpers.RelicPools

@Suppress("unused")
@SpirePatch(clz = RelicLibrary::class, method = "populateRelicPool")
object PopulateRelicPoolPatch {
    @SpirePostfixPatch
    @JvmStatic
    fun replacePool(pool: ArrayList<String>, tier: AbstractRelic.RelicTier, c: AbstractPlayer.PlayerClass){
        if (AbstractDungeon.isPlayerInDungeon()){
            val relicList: MutableList<AbstractRelic> = when (tier){
                AbstractRelic.RelicTier.COMMON -> RelicPools.commonList
                AbstractRelic.RelicTier.UNCOMMON -> RelicPools.uncommonList
                AbstractRelic.RelicTier.RARE -> RelicPools.rareList
                AbstractRelic.RelicTier.BOSS -> RelicPools.bossList
                AbstractRelic.RelicTier.SHOP -> RelicPools.shopList
                else -> return
            }

            pool.clear()
            pool.addAll(relicList.map { r -> r.relicId })
        }
    }
}