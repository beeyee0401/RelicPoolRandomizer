package patches

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet
import com.megacrit.cardcrawl.relics.OddMushroom
import com.megacrit.cardcrawl.relics.RedMask
import com.megacrit.cardcrawl.rooms.AbstractRoom
import config.Config
import helpers.RelicPools

@Suppress("unused")
@SpirePatch(clz = AbstractRoom::class, method = "addRelicToRewards", paramtypez = [AbstractRelic::class])
object CombatEventRelicPatch {
    private val combatEventRelics = listOf(OddMushroom.ID, RedMask.ID, Circlet.ID)

    @SpirePrefixPatch
    @JvmStatic
    fun replaceSpecialRelicToUsePool(__instance: AbstractRoom, @ByRef relic: Array<AbstractRelic>) {
        if (RelicPoolRandomizer.config.getBooleanKey(Config.shouldRandomizeSpecialRelicsKey) &&
            combatEventRelics.contains(relic[0].relicId) && __instance.phase == AbstractRoom.RoomPhase.EVENT){
            relic[0] = RelicPools.getSpecialRelic()
        }
    }
}