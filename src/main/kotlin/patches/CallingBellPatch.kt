package patches

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.core.OverlayMenu
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.CallingBell
import com.megacrit.cardcrawl.rooms.ShopRoom
import com.megacrit.cardcrawl.screens.CombatRewardScreen
import javassist.CtBehavior

@Suppress("unused")
@SpirePatch(clz = CallingBell::class, method = "update")
object CallingBellPatch {
    @SpireInsertPatch(
        locator = OpenRewardScreenLocator::class,
    )
    @JvmStatic
    fun addBackButtonToCallingBellInShop(__instance: CallingBell) {
        if (AbstractDungeon.getCurrRoom() is ShopRoom){
            AbstractDungeon.combatRewardScreen.open(CombatRewardScreen.TEXT[0])
        } else {
            AbstractDungeon.combatRewardScreen.open()
        }
    }

    @Suppress("unused")
    class OpenRewardScreenLocator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior?): IntArray {
            val matcher = Matcher.MethodCallMatcher(CombatRewardScreen::class.java, "open")
            return LineFinder.findAllInOrder(ctBehavior, matcher)
        }
    }

    @SpireInsertPatch(
        locator = ProceedButtonLocator::class,
    )
    @JvmStatic
    fun removeProceedButton(__instance: CallingBell) {
        if (AbstractDungeon.getCurrRoom() is ShopRoom){
            AbstractDungeon.overlayMenu.proceedButton.hide()
        }
    }

    @Suppress("unused")
    class ProceedButtonLocator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior?): IntArray {
            val matcher = Matcher.FieldAccessMatcher(OverlayMenu::class.java, "proceedButton")
            val lines = LineFinder.findAllInOrder(ctBehavior, matcher)
            lines[0]++
            return lines
        }
    }
}