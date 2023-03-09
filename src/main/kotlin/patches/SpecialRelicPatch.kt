package patches

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.city.Addict
import com.megacrit.cardcrawl.events.exordium.BigFish
import com.megacrit.cardcrawl.events.exordium.ScrapOoze
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain
import com.megacrit.cardcrawl.neow.NeowRoom
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import config.Config
import helpers.RelicPools

@Suppress("unused")
@SpirePatch(clz = AbstractRoom::class, method = "spawnRelicAndObtain")
object SpecialRelicPatch {
    private val randomRelicSpawnEvents = listOf(WeMeetAgain.ID, BigFish.ID, ScrapOoze.ID, Addict.ID)

    @SpirePrefixPatch
    @JvmStatic
    fun replaceSpecialRelicToUsePool(__instance: AbstractRoom, x: Float, y: Float, @ByRef relic: Array<AbstractRelic>) {
        val isInNeowRoom = __instance is NeowRoom
        // Don't change the events already giving a random relic
        val isValidEventRandomization = __instance.phase == AbstractRoom.RoomPhase.EVENT && !randomRelicSpawnEvents.contains(EventHelperPatch.eventName)
        if (RelicPoolRandomizer.config.getBooleanKey(Config.shouldRandomizeSpecialRelicsKey) && !isInNeowRoom &&
            relic[0].tier === AbstractRelic.RelicTier.SPECIAL && isValidEventRandomization) {
            relic[0] = RelicPools.getSpecialRelic()
        }
    }
}